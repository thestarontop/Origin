package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Timer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;

import java.awt.*;
import java.util.LinkedList;

public class Blink extends Module {
    public Blink(){super("Blink","bzd", Module.Category.MOVEMENT);}
    private int tick;
    LinkedList<Packet<?>> packets = new LinkedList<>();
    private boolean disablelogger = true;

    @EventTarget
    public void onPacket(PacketEvent event){
        if (mc.player == null || disablelogger)
            return;

        Packet<?> packet = event.getPacket();
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
        if (Origin.getInstance().getModuleManager().getModule("HUD").isEnabled()) {
            Screen.fill(new PoseStack(),70, 50, Math.min(deltaX, 170),70, new Color(255,255,255,255).getRGB());
            Screen.fill(new PoseStack(),70, 50,170,70, new Color(160,160,160,120).getRGB());
        }
    }
    public void blink(){
        try {
            disablelogger = true;
            while (!packets.isEmpty()){
                PacketUtils.sendPacketNoEvent(packets.get(0));
                packets.remove(0);
            }
        }catch (Error e){
            e.printStackTrace();
        }

    }
    public void onDisable() {
        tick=0;
        ChatManager.sendHotBarChat(ChatFormatting.RED + "Blink Was Disabled");
        blink();
        Origin.getInstance().getEventManager().unregister(this);
    }
    public void onEnable() {
        tick=0;
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Blink Was Enabled");
        disablelogger = false;
        Origin.getInstance().getEventManager().register(this);
    }
}