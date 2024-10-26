package net.java.main.modules.combat;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Iterator;
import java.util.List;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem","Switch the totem to your offhand", Module.Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null || mc.player.inventoryMenu.getSlot(45).getItem().getItem() == Items.TOTEM_OF_UNDYING) return;

        for (int a = 0;a <= 44; a++){
            if(mc.player.inventoryMenu.getSlot(a).getItem().getItem() == Items.TOTEM_OF_UNDYING){
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
                        if (j == 0){
                            int2objectmap.put(j, new ItemStack(Items.BEDROCK));
                        }else {
                            int2objectmap.put(j, itemstack1.copy());
                        }
                    }
                }
                mc.getConnection().send(new ServerboundContainerClickPacket(0,a+100,a,40,ClickType.SWAP,new ItemStack(Items.TOTEM_OF_UNDYING),int2objectmap));
                mc.getConnection().send(new ServerboundContainerClosePacket(0));
                break;
            }
        }
    }
}
