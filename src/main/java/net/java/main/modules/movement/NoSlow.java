package net.java.main.modules.movement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.events.*;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.utils.MSTimer;
import net.java.main.utils.PacketUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.Foods;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class NoSlow extends Module {
    public NoSlow(){
        super("NoSlow","bzd",Category.MOVEMENT);
    }
    public static boolean shouldnoslow = true;
    public static boolean reported = false;

    @EventTarget
    public void onPacket(PacketEvent event) {
        //todo: use offhand instead
        if (event.getPacket() instanceof ServerboundUseItemPacket packet){
            if (isUsable(mc.player.getItemInHand(packet.getHand()))){
                shouldnoslow = false;
                mc.getConnection().send(new ServerboundChatPacket("/report"));
                reported = true;
            }
        }
        if (event.getPacket() instanceof ClientboundOpenScreenPacket packet){
            shouldnoslow = true;
            if (reported){
                boolean sprinting = mc.player.isSprinting();
                event.cancelEvent();
                if (sprinting) {
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                }
                mc.getConnection().send(new ServerboundContainerClosePacket(packet.getContainerId()));
                if (sprinting) {
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                }
                reported = false;
            }
        }
    }
    public static boolean isUsable(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            boolean isFood = item.getFoodProperties() != null;
            boolean isShield = item == Items.SHIELD;
            boolean isBow = item instanceof BowItem;
            boolean isCrossBow = item instanceof CrossbowItem;
            boolean isPotion = item == Items.POTION;

            return isFood || isShield || isBow || isCrossBow || isPotion;
        } else {
            return false;
        }
    }

    @EventTarget
    public void onSlow(SlowDownEvent event) {
        if (shouldnoslow) {
            event.setMovementForward(1.0f);
            event.setMovementStrafe(1.0f);
        }

    }

}
