package net.java.main.modules.combat;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.java.main.utils.MovementUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class InvManager extends Module {
    public InvManager(){super("InvManager","bzd",Category.COMBAT);}
    private int i = 4;


    private List<Item> blackItemList = List.of(Items.DIAMOND_SHOVEL,Items.STONE_SHOVEL,Items.IRON_SHOVEL,Items.GOLDEN_SHOVEL,Items.NETHERITE_SHOVEL,Items.COBWEB,Items.EGG,Items.BOOK,Items.CHEST,Items.FISHING_ROD,Items.LAVA_BUCKET,Items.CROSSBOW,Items.EXPERIENCE_BOTTLE,Items.WATER_BUCKET,Items.SADDLE,Items.FLINT,Items.FLINT_AND_STEEL,Items.COMPASS);
    private boolean haswindow = false;
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if(MovementUtils.isMoving()) return;
        i++;
        if (mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.AIR && i != 45){
            i++;
        }
            ItemStack itemStack = mc.player.inventoryMenu.getSlot(i).getItem();
        if (i > 8) {
            if (itemStack.getItem() instanceof ArmorItem armorItem) {
                ItemStack headarmor = mc.player.inventoryMenu.getSlot(5).getItem();
                ItemStack chestarmor = mc.player.inventoryMenu.getSlot(6).getItem();
                ItemStack legsarmor = mc.player.inventoryMenu.getSlot(7).getItem();
                ItemStack feetarmor = mc.player.inventoryMenu.getSlot(8).getItem();
                if (armorItem.getSlot() == EquipmentSlot.HEAD) {
                    if (mc.player.inventoryMenu.getSlot(5).getItem().getItem() != Items.AIR) {
                        if (getDefense(itemStack) > getDefense(headarmor)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, 5, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));

                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }else{
                        mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                        mc.getConnection().send(new ServerboundContainerClosePacket(0));
                    }

                }
                if (armorItem.getSlot() == EquipmentSlot.CHEST) {
                    if (mc.player.inventoryMenu.getSlot(6).getItem().getItem() != Items.AIR) {
                        if (getDefense(itemStack) > getDefense(chestarmor)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, 6, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }else{
                        mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                        mc.getConnection().send(new ServerboundContainerClosePacket(0));
                    }

                }
                if (armorItem.getSlot() == EquipmentSlot.LEGS) {
                    if (mc.player.inventoryMenu.getSlot(7).getItem().getItem() != Items.AIR) {
                        if (getDefense(itemStack) > getDefense(legsarmor)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, 7, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }else{
                        mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                        mc.getConnection().send(new ServerboundContainerClosePacket(0));
                    }

                }
                if (armorItem.getSlot() == EquipmentSlot.FEET) {
                    if (mc.player.inventoryMenu.getSlot(8).getItem().getItem() != Items.AIR) {
                        if (getDefense(itemStack) > getDefense(feetarmor)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, 8, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }else{
                        mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.QUICK_MOVE, itemStack, Int2ObjectMaps.emptyMap()));
                        mc.getConnection().send(new ServerboundContainerClosePacket(0));
                    }

                }
            }
        }
                if (itemStack.getItem() instanceof SwordItem swordItem) {
                    if (i != 36) {
                        Item item = mc.player.inventoryMenu.getSlot(36).getItem().getItem();
                        if (!(item instanceof SwordItem currsword)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, i, 0, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            if (swordItem.getDamage() > currsword.getDamage()) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i, 36, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 0, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                            } else {
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                            }
                        }
                    }
                }
                if (itemStack.getItem() instanceof BowItem) {
                    if (i != 37) {
                        if (!(mc.player.inventoryMenu.getSlot(37).getItem().getItem() instanceof BowItem)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 1, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 0, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }

                }
                if (itemStack.getItem() instanceof BlockItem) {
                    if (i != 38) {
                        if (!(mc.player.inventoryMenu.getSlot(38).getItem().getItem() instanceof BlockItem)) {
                            if (itemStack.getItem() == Items.OAK_PLANKS || itemStack.getItem() == Items.STONE) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 2, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                            }else{
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 1, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                            }
                        }
                    }
                }
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    if (i != 40) {
                        if (!(mc.player.inventoryMenu.getSlot(40).getItem().getItem() == Items.ENDER_PEARL)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 4, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }
                }
                if (itemStack.getItem() instanceof AxeItem item) {
                    if (i != 43) {
                        if (!(mc.player.inventoryMenu.getSlot(43).getItem().getItem() instanceof AxeItem)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 7, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            if (item != Items.GOLDEN_AXE) {
                                mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 1, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                            }
                        }
                    }
                }
                if (itemStack.getItem() instanceof PickaxeItem) {
                    if (i != 44) {
                        if (!(mc.player.inventoryMenu.getSlot(44).getItem().getItem() instanceof PickaxeItem)) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, i, 8, ClickType.SWAP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 1, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            mc.getConnection().send(new ServerboundContainerClosePacket(0));
                        }
                    }
                }
                if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                    if (i != 45) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i, i, 0, ClickType.PICKUP, itemStack, Int2ObjectMaps.emptyMap()));
                            mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 1, 45, 0, ClickType.PICKUP, itemStack, Int2ObjectMaps.emptyMap()));
                        mc.getConnection().send(new ServerboundContainerClosePacket(0));
                    }
                }
                if (blackItemList.contains(itemStack.getItem())) {
                    mc.getConnection().send(new ServerboundContainerClickPacket(0, i + 2, i, 1, ClickType.THROW, itemStack, Int2ObjectMaps.emptyMap()));
                    mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                    mc.getConnection().send(new ServerboundContainerClosePacket(0));
                }
                if (i == 45) {
                    i = 4;
                }


    }


    public static double getDefense(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ArmorItem armorItem) {
                return armorItem.getDefense() + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION,itemStack)*0.00000001;
        }
        return -11111111;
    }
    public static double getDamge(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SwordItem swordItemItem) {
            return swordItemItem.getDamage() + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS,itemStack)*0.00000001;
        }
        return -11111111;
    }
    private float damage(final ItemStack stack) {
        final SwordItem sword = (SwordItem) stack.getItem();
        final int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, stack);
        return (float) (sword.getDamage() + level * 1.25);
    }
    @Override
    public void onEnable(){
        i = 4;
        super.onEnable();
    }
    @Override
    public void onDisable(){
        i = 4;
        mc.getConnection().send(new ServerboundContainerClosePacket(0));
        super.onDisable();
    }

}
