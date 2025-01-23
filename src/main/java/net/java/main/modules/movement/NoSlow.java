package net.java.main.modules.movement;

import com.google.common.collect.Lists;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.SlowDownEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.utils.PacketUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.Foods;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","bzd",Category.MOVEMENT);}
    public static boolean shouldnoslow = true;
    public boolean shouldstartsprint = false;
    @EventTarget
    public void onPacket(PacketEvent event){
        if (mc.getConnection() == null || mc.player == null) return;
        if (!shouldnoslow && mc.player.isUsingItem()) {
            if (event.getPacket() instanceof ServerboundInteractPacket) {
                event.cancelEvent();
            }
            if (event.getPacket() instanceof ServerboundSwingPacket) {
                event.cancelEvent();
            }
        }
        if(event.getPacket() instanceof ServerboundUseItemPacket packet){
            ItemStack item = mc.player.getItemInHand(packet.getHand());
            if (!isUsable(item)){
                return;
            }
            shouldnoslow = false;
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
            boolean sprinting = mc.player.isSprinting();
            if (sprinting){
                shouldstartsprint = false;
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
            }
            mc.getConnection().send(new ServerboundContainerClickPacket(0,-32767,mc.player.getInventory().selected+36,0,ClickType.PICKUP, mc.player.getInventory().getSelected(),int2objectmap));
            mc.getConnection().send(new ServerboundContainerClosePacket(0));
            //第二解决方案：副手
        }
        if(event.getPacket() instanceof ClientboundContainerSetSlotPacket packet){
            if (packet.getSlot() == mc.player.getInventory().selected +36 && packet.getContainerId() == 0){
                if (!shouldstartsprint) {
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                }
                shouldnoslow = true;
                shouldstartsprint = true;
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player.isUsingItem() && shouldstartsprint != mc.player.isSprinting()){
                mc.player.setSprinting(shouldstartsprint);
        }
    }
    public static boolean isUsable(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty()) {
            boolean hasArrow = false;
            for (int i=0;i<=45;i++){
                if (mc.player.inventoryMenu.getSlot(i).getItem().getItem() instanceof ArrowItem){
                    hasArrow = true;
                    break;
                }
            }
            Item item = itemStack.getItem();
            boolean isFood = item.getFoodProperties() != null;
            boolean isShield = item == Items.SHIELD;
            boolean isBow = item instanceof BowItem && hasArrow;
            boolean isCrossBow = item instanceof CrossbowItem && hasArrow;
            boolean isPotion = item == Items.POTION;
            return isFood || isShield || isBow || isCrossBow || isPotion;
        } else {
            return false;
        }
    }
    @EventTarget
    public void onSlowDown(SlowDownEvent event){
        if (shouldnoslow) {
            event.setMovementStrafe(1F);
            event.setMovementForward(1F);
        }else{
            event.setMovementStrafe(0.2F);
            event.setMovementForward(0.2F);
        }
    }
}
