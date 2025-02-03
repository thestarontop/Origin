package net.java.main.modules.movement;

import com.mojang.authlib.GameProfile;
import net.java.main.event.events.Render3DEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.UpdateEvent;
import net.java.main.command.ChatManager;
import net.java.main.event.events.Render2DEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.utils.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.utils.RenderUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.ListValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.AABB;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Blink extends Module {
    public Blink(){
    super("Blink","bzd", Module.Category.MOVEMENT);
    this.addValues(antiaim);
    }
    private int tick;
    LinkedBlockingQueue<Packet<?>> packets = new LinkedBlockingQueue<>();
    BooleanValue antiaim = new BooleanValue("AntiAim",false);
    private boolean disablelogger = true;
    AABB box = null;

    @EventTarget
    public void onPacket(PacketEvent event){
        if (mc.player == null || disablelogger)
            return;

        Packet packet = event.getPacket();
        if (packet instanceof ServerboundInteractPacket ||
                packet instanceof ServerboundSwingPacket || packet instanceof ServerboundPlayerCommandPacket ||
                packet instanceof ServerboundPlayerActionPacket ||
                packet instanceof ServerboundUseItemOnPacket ||
                packet instanceof ServerboundMovePlayerPacket ||
                packet instanceof ServerboundPongPacket ||
                packet instanceof ServerboundPingRequestPacket ||
                packet instanceof ServerboundSetCarriedItemPacket ||
                packet instanceof ServerboundCustomPayloadPacket ||
                packet instanceof ServerboundPlayerAbilitiesPacket ||
                packet instanceof ServerboundPlayerInputPacket ||
                packet instanceof ServerboundKeepAlivePacket
        ){
            event.cancelEvent();
            packets.add(packet);
        }
        if (packet instanceof ServerboundUseItemPacket){
            if (antiaim.getValue()) {
                event.cancelEvent();
                blink();
                PacketUtils.sendPacketNoEvent(packet);
            }else{
                event.cancelEvent();
                packets.add(packet);
            }
        }

        if (packet instanceof ServerboundPlayerActionPacket packet1 && packet1.getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
            if (antiaim.getValue()){
                blink();
            }
        }

    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (antiaim.getValue()) {
            if (distanceTo(box) >= 5) {
                blink();
            }
            for (Entity entity : mc.level.entitiesForRendering()){
                if (((entity instanceof Arrow || entity instanceof Snowball || entity instanceof Player) && entity.getId() != mc.player.getId()) && distanceTo(box) <= 6){
                    blink();
                    break;
                }
            }
        }

        tick++;
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        int deltaX = 70+tick/4;
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("HUD").isEnabled()) {
            Screen.fill(new PoseStack(),70, 50, Math.min(deltaX, 170),70, new Color(255,255,255,255).getRGB());
            Screen.fill(new PoseStack(),70, 50,170,70, new Color(160,160,160,120).getRGB());
        }
    }
    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (box != null) {
            RenderUtils.renderBoundingBox(event.getPoseStack(), box, 1F, 0.7529F, 0.7961F);
        }
    }
    public void blink(){
        try {
            disablelogger = true;
            while (!packets.isEmpty()){
                Packet packet = packets.take();
                PacketUtils.sendPacketNoEvent(packet);
            }
            box = mc.player.getBoundingBox();
            disablelogger = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public float distanceTo(AABB arg) {
        float f = (float)(mc.player.getX() - arg.minX);
        float f1 = (float)(mc.player.getY() - arg.minY);
        float f2 = (float)(mc.player.getZ() - arg.minZ);
        return Mth.sqrt(f * f + f1 * f1 + f2 * f2);
    }
    public void onDisable() {
        tick=0;
        blink();
        super.onDisable();
    }
    public void onEnable() {
        box = mc.player.getBoundingBox();
        tick=0;
        disablelogger = false;
        super.onEnable();
    }
}