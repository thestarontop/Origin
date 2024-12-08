package net.java.main.modules.combat;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.value.FloatValue;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class Stealer extends Module {
    public Stealer(){super("Stealer","bzd", Module.Category.COMBAT);}
    private MSTimer timer = new MSTimer();
    private Float nextDelay = (float) 0;
    private List<Item> blackItemList = List.of(Items.DIAMOND_SHOVEL,Items.STONE_SHOVEL,Items.IRON_SHOVEL,Items.GOLDEN_SHOVEL,Items.NETHERITE_SHOVEL,Items.COBWEB,Items.EGG,Items.BOOK,Items.CHEST,Items.FISHING_ROD,Items.LAVA_BUCKET,Items.CROSSBOW,Items.EXPERIENCE_BOTTLE,Items.WATER_BUCKET,Items.SADDLE,Items.FLINT,Items.FLINT_AND_STEEL,Items.COMPASS,Items.SNOWBALL);

    public FloatValue delay = new FloatValue("Delay", 100f, 0.0f, 1000.0f);

    @EventTarget
    public void onMotion(PacketEvent event) {
        if (event.getPacket() instanceof ClientboundContainerSetContentPacket packet) {

            if (mc.screen == null /*|| mc2.player.openContainer == mc2.player.inventoryContainer*/)
                return;
            Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

            if (mc.screen instanceof ContainerScreen) {
                for (int i = 0; i <= 2; i++) {
                    Slot slot = mc.player.inventoryMenu.getSlot(i);
                    if (slot.getItem() == Items.AIR.getDefaultInstance()) {
                        if (timer.hasTimePassed(nextDelay.byteValue())) {

                        mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), slot.index, slot.index, 1, ClickType.QUICK_MOVE, packet.getItems().get(slot.index), int2objectmap));


                        nextDelay = delay.getValue();
                        timer.reset();
                        }
                    }
                }

            }

            for (int i = 0; i <= 2; i++) {
                Slot slot = mc.player.inventoryMenu.getSlot(i);
                if (slot.getItem() == Items.AIR.getDefaultInstance()) {
                    if (timer.hasTimePassed(nextDelay.byteValue())) {
                    mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), slot.index, slot.index, 1, ClickType.QUICK_MOVE, packet.getItems().get(slot.index), int2objectmap));

                    nextDelay = delay.getValue();

                    timer.reset();
                        }
                }
            }

            if (mc.screen instanceof ContainerScreen) {
                for (int i = 0; i < 72; ++i) {
                    Slot slot = mc.player.inventoryMenu.getSlot(i);
                    if (slot.getItem() == Items.AIR.getDefaultInstance()) {
                        if (timer.hasTimePassed(nextDelay.byteValue()) && blackItemList.contains(packet.getItems().get(slot.index).getItem())) {
//                            for (int j = 0; j < 21; ++j) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), slot.index, slot.index, 1, ClickType.QUICK_MOVE, packet.getItems().get(slot.index), int2objectmap));
//                            }
                            nextDelay = delay.getValue();
                            timer.reset();
                        }
                    }
                }
            }
        }
    }










}

