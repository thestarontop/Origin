package com.mojang.main.modules.movement;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.Render2DEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import com.mojang.main.utils.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
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
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("HUD").isEnabled()) {
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
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
    }
    public void onEnable() {
        tick=0;
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Blink Was Enabled");
        disablelogger = false;
        madebystarontopandfml.getInstance().getEventManager().register(this);
    }
}