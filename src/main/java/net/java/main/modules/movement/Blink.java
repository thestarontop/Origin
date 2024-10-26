package net.java.main.modules.movement;

import com.mojang.authlib.GameProfile;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.UpdateEvent;
import net.java.main.command.ChatManager;
import net.java.main.event.events.Render2DEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.utils.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;

import java.awt.*;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Blink extends Module {
    public Blink(){super("Blink","bzd", Module.Category.MOVEMENT);}
    private int tick;
    LinkedBlockingQueue<Packet<?>> packets = new LinkedBlockingQueue<>();
    private boolean disablelogger = true;

    @EventTarget
    public void onPacket(PacketEvent event){
        if (mc.player == null || disablelogger)
            return;

        Packet packet = event.getPacket();
        if (packet instanceof ServerboundInteractPacket ||
                packet instanceof ServerboundSwingPacket ||
                packet instanceof ServerboundPlayerCommandPacket ||
                packet instanceof ServerboundPlayerActionPacket ||
                packet instanceof ServerboundUseItemOnPacket ||
                packet instanceof ServerboundUseItemPacket ||
                packet instanceof ServerboundMovePlayerPacket ||
                packet instanceof ServerboundPongPacket ||
                packet instanceof ServerboundSetCarriedItemPacket ||
                packet instanceof ServerboundCustomPayloadPacket ||
                packet instanceof ServerboundPlayerAbilitiesPacket ||
                packet instanceof ServerboundPlayerInputPacket
        ){
            event.cancelEvent();
            packets.add(packet);
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
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
    public void blink(){
        try {
            disablelogger = true;
            while (!packets.isEmpty()){
                Packet packet = packets.take();
                PacketUtils.sendPacketNoEvent(packet);
            }
            disablelogger = false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public void onDisable() {
        tick=0;
        blink();
        super.onDisable();
    }
    public void onEnable() {
        tick=0;
        disablelogger = false;
        super.onEnable();
    }
}