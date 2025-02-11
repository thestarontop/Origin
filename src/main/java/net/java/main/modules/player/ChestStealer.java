package net.java.main.modules.player;

import com.google.common.collect.Lists;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.utils.MSTimer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Iterator;
import java.util.List;

public class ChestStealer extends Module {
    public ChestStealer(){super("ChestStealer","bzd",Category.COMBAT);}

    private int windowid = 0;
    private boolean hasWindow = false;
    private int ticks = 0;
    private boolean isfull = false;

    private List<Item> blackItemList = List.of(Items.DIAMOND_SHOVEL,Items.STONE_SHOVEL,Items.IRON_SHOVEL,Items.GOLDEN_SHOVEL,Items.NETHERITE_SHOVEL,Items.COBWEB,Items.EGG,Items.BOOK,Items.CHEST,Items.FISHING_ROD,Items.LAVA_BUCKET,Items.CROSSBOW,Items.EXPERIENCE_BOTTLE,Items.WATER_BUCKET,Items.SADDLE,Items.FLINT,Items.FLINT_AND_STEEL,Items.COMPASS,Items.SNOWBALL);
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundOpenScreenPacket packet){
            hasWindow = true;
            windowid = packet.getContainerId();
        }
        if (event.getPacket() instanceof ClientboundContainerSetContentPacket packet) {
            isfull = true;
            for (int i = 9; i <= 36; i++) {
                if (mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.AIR) {
                    isfull = false;
                    break;
                }
            }
            if (packet.getContainerId() != windowid || isfull) return;

            //powered by mojang
            AbstractContainerMenu abstractcontainermenu = mc.player.containerMenu;
            NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
            int i = nonnulllist.size();
            List<ItemStack> list = Lists.newArrayListWithCapacity(i);
            Iterator var10 = nonnulllist.iterator();

            while (var10.hasNext()) {
                Slot slot = (Slot) var10.next();
                list.add(slot.getItem().copy());
            }

            Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = list.get(j);
                ItemStack itemstack1 = nonnulllist.get(j).getItem();
                if (!ItemStack.matches(itemstack, itemstack1)) {
                    int2objectmap.put(j, itemstack1.copy());
                }
            }

                if (packet.getItems().size() == 63) {
                    for (int index = 0; index <= 26; index++) {
                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                            if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                            } else {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                            }

                    }

                if (packet.getItems().size() == 90) {
                    for (int index = 0; index <= 53; index++) {
                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;


                            if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                            } else {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                            }
                    }
                }
                if (packet.getItems().size() == 39) {
                    for (int index = 0; index <= 2; index++) {
                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                            if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                            } else {
                                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                            }
                    }
                }
                if (packet.getItems().size() == 41) {
                    for (int index = 0; index <= 2; index++) {


                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                        if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                        }
                    }
                }
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (hasWindow){
            ticks ++;
            if (ticks == 5){
                mc.player.closeContainer();
                hasWindow = false;
                ticks = 0;
            }
        }
    }
 }


