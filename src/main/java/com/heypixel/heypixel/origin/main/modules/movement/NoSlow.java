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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.util.profiling.jfr.event.PacketReceivedEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.Foods;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.EnchantedGoldenAppleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.lwjgl.system.CallbackI;

import java.util.LinkedList;


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
                for (int i = 36; i < 45; i++) {
                    itemstack = mc.player.getInventory().getItem(i);
                    if (!itemstack.isEmpty() &&   itemstack != null && itemstack.getItem() != Items.AIR && itemstack.getCount() > 1) {
                        item = i;
                        break;
                    }
                }


                if(itemstack != null) {
                    mc.player.connection.send(new ServerboundContainerClickPacket(0, 0, item, 0, ClickType.SWAP, itemstack, Int2ObjectMaps.emptyMap()));
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
        }
        if(event.getPacket() instanceof ServerboundPlayerActionPacket && ((ServerboundPlayerActionPacket) event.getPacket()).getAction() == ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
            shouldnoslow = false;
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
