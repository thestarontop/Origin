package com.heypixel.heypixel.origin.main.modules.movement;

import com.google.common.collect.Lists;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.SlowDownEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;


public class NoSlow extends Module {
    public NoSlow(){super("NoSlow","Thanks to CCBlueX",Category.MOVEMENT);}
    private boolean shouldnoslow = true;
    @EventTarget
    public void onPacket(PacketEvent event){
        if(event.getPacket() instanceof ClientboundContainerSetSlotPacket){
            shouldnoslow = true;
        }
        if(event.getPacket() instanceof ServerboundUseItemPacket){
            for (int i = 0; i <= 45;i++){
                if (mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.AIR){
                    NonNullList<Slot> nonnulllist = mc.player.containerMenu.slots;
                    int b = nonnulllist.size();
                    List<ItemStack> list = Lists.newArrayListWithCapacity(i);

                    for (Slot slot : nonnulllist) {
                        list.add(slot.getItem().copy());
                    }
                    Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap<>();

                    for(int j = 0; j < b; ++j) {
                        ItemStack itemstack = list.get(j);
                        ItemStack itemstack1 = nonnulllist.get(j).getItem();
                        if (!ItemStack.matches(itemstack, itemstack1)) {
                            int2objectmap.put(j, itemstack1.copy());
                        }
                    }
                    mc.getConnection().send(new ServerboundContainerClickPacket(0,i,i,0,ClickType.PICKUP,new ItemStack(Items.BEDROCK),int2objectmap));
                    break;
                }
            }
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
