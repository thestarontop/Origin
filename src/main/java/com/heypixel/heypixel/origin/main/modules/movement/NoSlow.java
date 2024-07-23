package com.heypixel.heypixel.origin.main.modules.movement;

import com.google.common.collect.Lists;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.SlowDownEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","bzd",Category.MOVEMENT);}
    private boolean shouldnoslow = true;
    @EventTarget
    public void onPacket(PacketEvent event){
        if(event.getPacket() instanceof ClientboundContainerSetSlotPacket){
            shouldnoslow = true;
        }
        if(event.getPacket() instanceof ServerboundUseItemPacket){
            shouldnoslow = false;
        }
        if(event.getPacket() instanceof ServerboundPlayerActionPacket && ((ServerboundPlayerActionPacket) event.getPacket()).getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
            shouldnoslow = false;
        }
        if (event.getPacket() instanceof ServerboundInteractPacket && (!shouldnoslow && mc.player.isUsingItem())){
            event.cancelEvent();
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
