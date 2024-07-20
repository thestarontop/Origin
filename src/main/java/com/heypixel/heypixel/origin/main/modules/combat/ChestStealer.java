package com.heypixel.heypixel.origin.main.modules.combat;

import com.google.common.collect.Lists;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ChestStealer extends Module {
    public ChestStealer(){super("ChestStealer","bzd",Category.COMBAT);}

    private int delay = 0;
    boolean hasItem=true;
    private List<Item> blackItemList = List.of(Items.DIAMOND_SHOVEL,Items.STONE_SHOVEL,Items.IRON_SHOVEL,Items.GOLDEN_SHOVEL,Items.NETHERITE_SHOVEL,Items.COBWEB,Items.EGG,Items.BOOK,Items.CHEST,Items.FISHING_ROD,Items.LAVA_BUCKET,Items.CROSSBOW,Items.EXPERIENCE_BOTTLE,Items.WATER_BUCKET,Items.SADDLE,Items.FLINT,Items.FLINT_AND_STEEL,Items.COMPASS);
    @EventTarget
    public void onUpdate(UpdateEvent event){
            hasItem = false;
            delay ++;

            if (delay >= 1){
                if (mc.screen instanceof ContainerScreen container){
                    for (ItemStack item : container.getMenu().getItems()){
                        if (item.getItem() != Items.AIR){
                            hasItem = true;
                        }
                    }
                    if (mc.player !=null && !hasItem){
                        mc.player.closeContainer();
                    }
                    for (Slot slot : container.getMenu().slots){
                        if (slot.getItem().getItem() != Items.AIR){
                            if (blackItemList.contains(slot.getItem().getItem())){
                                mc.getConnection().send(new ServerboundContainerClickPacket(container.getMenu().containerId, slot.getSlotIndex() -10, slot.getSlotIndex(), 1, ClickType.THROW, slot.getItem(), Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            }else {
                                mc.getConnection().send(new ServerboundContainerClickPacket(container.getMenu().containerId, slot.getSlotIndex() - 10, slot.getSlotIndex(), 0, ClickType.QUICK_MOVE, slot.getItem(), Int2ObjectMaps.emptyMap()));
                            }
                            delay = 0;
                            return;
                        }
                    }
                }
            }
    }
 }


