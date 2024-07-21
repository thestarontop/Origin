package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.MotionEvent;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.SlowDownEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","Thanks to CCBlueX",Category.MOVEMENT);}
    private boolean shouldnoslow = true;

    ItemStack itemstack = null;
    int item = 36;
    @EventTarget
    public void onMotion(MotionEvent event){
        if (mc.player.isUsingItem() && event.getPre()){
            var hand = mc.player.getUsedItemHand();

            if (hand == InteractionHand.MAIN_HAND) {
                var slot = mc.player.getInventory().selected;


                for (int i = 36; i < 45; i++) {
                    itemstack = mc.player.getInventory().getItem(i);
                    if (!itemstack.isEmpty() && itemstack.getItem() != Items.AIR && itemstack.getCount() > 1) {
                        item = i;
                        break;
                    }
                }
                if(itemstack != null) {
                    mc.player.connection.send(new ServerboundContainerClickPacket(0, 0, item, 1, ClickType.PICKUP, itemstack, Int2ObjectMaps.emptyMap()));
                    mc.player.connection.send(new ServerboundContainerClickPacket(1, 0, item, 0, ClickType.PICKUP, itemstack, Int2ObjectMaps.emptyMap()));
                }
            }else{
                shouldnoslow = false;
            }
        }
        if (!mc.player.isUsingItem()){
            shouldnoslow = false;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event){
        if(event.getPacket() instanceof ClientboundContainerSetSlotPacket){
            shouldnoslow = true;
        }
        if(event.getPacket() instanceof ServerboundUseItemPacket){
            shouldnoslow = false;
            mc.player.connection.send(new ServerboundContainerClickPacket(1, 0, item, 0, ClickType.PICKUP, itemstack, Int2ObjectMaps.emptyMap()));
        }
        if(event.getPacket() instanceof ServerboundPlayerActionPacket && ((ServerboundPlayerActionPacket) event.getPacket()).getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
            shouldnoslow = false;
            mc.player.connection.send(new ServerboundContainerClickPacket(1, 0, item, 0, ClickType.PICKUP, itemstack, Int2ObjectMaps.emptyMap()));
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
