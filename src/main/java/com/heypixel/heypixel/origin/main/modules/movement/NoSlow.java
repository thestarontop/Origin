package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.MotionEvent;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.SlowDownEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.Foods;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedList;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","bzd",Category.MOVEMENT);}
    private boolean shouldnoslow;
    private LinkedList<Packet<?>> packets = new LinkedList<>();
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundUseItemPacket packet){
            shouldnoslow = false;
            mc.getConnection().send(new ServerboundChatPacket("/report"));
        }
        if (event.getPacket() instanceof ClientboundOpenScreenPacket packet){
            shouldnoslow = true;
            event.cancelEvent();
        }
        if (event.getPacket() instanceof ServerboundPlayerActionPacket packet){
            if (packet.getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
                if (shouldnoslow && !packets.isEmpty()){
                    try {
                        while (!packets.isEmpty()){
                            PacketUtils.sendPacketNoEvent(packets.get(0));
                            packets.remove(0);
                        }
                    }catch (Error e){
                        e.printStackTrace();
                    }
                }
                shouldnoslow = false;
            }
        }
        if (event.getPacket() instanceof ServerboundPongPacket packet){
            packets.add(packet);
        }
    }

    @EventTarget
    public void onSlowDown(SlowDownEvent event){
        if (shouldnoslow) {
            event.setMovementStrafe(1F);
            event.setMovementForward(1F);
        }
    }   
}
