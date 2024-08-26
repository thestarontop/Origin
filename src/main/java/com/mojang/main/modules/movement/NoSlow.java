package com.mojang.main.modules.movement;

import com.google.common.collect.Lists;
import com.mojang.main.event.events.MotionEvent;
import com.mojang.main.event.events.SlowDownEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import com.mojang.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","bzd",Category.MOVEMENT);}
    public static boolean shouldnoslow = true;
    @EventTarget
    public void onMotion(MotionEvent event){
        if (event.getPre()){
            if (mc.player.isUsingItem() && mc.player.getUsedItemHand() == InteractionHand.OFF_HAND){
                shouldnoslow = true;
                //powered by mojang
                AbstractContainerMenu abstractcontainermenu = mc.player.inventoryMenu;
                NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
                int i = nonnulllist.size();
                List<ItemStack> list = Lists.newArrayListWithCapacity(i);
                Iterator var10 = nonnulllist.iterator();

                while(var10.hasNext()) {
                    Slot slot = (Slot)var10.next();
                    list.add(slot.getItem().copy());
                }

                Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

                for(int j = 0; j < i; ++j) {
                    ItemStack itemstack = list.get(j);
                    ItemStack itemstack1 = nonnulllist.get(j).getItem();
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        int2objectmap.put(j, itemstack1.copy());
                    }
                }
                mc.getConnection().send(new ServerboundContainerClickPacket(0,-1,mc.player.getInventory().selected+36,0,ClickType.PICKUP,mc.player.getInventory().getSelected(),int2objectmap));
                mc.getConnection().send(new ServerboundContainerClickPacket(0,-1,mc.player.getInventory().selected+36,0,ClickType.PICKUP,mc.player.getInventory().getSelected(),int2objectmap));
                mc.getConnection().send(new ServerboundContainerClosePacket(0));
            }
        }
        if (!mc.player.isUsingItem() || mc.player.getUsedItemHand() == InteractionHand.MAIN_HAND){
            shouldnoslow = false;
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        /*if(event.getPacket() instanceof ServerboundUseItemPacket){
            shouldnoslow = false;
        }
        if(event.getPacket() instanceof ServerboundPlayerActionPacket && ((ServerboundPlayerActionPacket) event.getPacket()).getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
            shouldnoslow = false;
        }
        if (event.getPacket() instanceof ServerboundInteractPacket && (!shouldnoslow && mc.player.isUsingItem())){
            event.cancelEvent();
        }
        if (event.getPacket() instanceof ServerboundSwingPacket && (!shouldnoslow && mc.player.isUsingItem())){
            event.cancelEvent();
        }*/
    }

    @EventTarget
    public void onSlowDown(SlowDownEvent event){
        if (shouldnoslow) {
            event.setMovementStrafe(1F);
            event.setMovementForward(1F);
        }
    }
}
