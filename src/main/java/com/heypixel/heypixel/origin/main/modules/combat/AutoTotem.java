package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.protocol.game.ServerboundContainerButtonClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem","Switch the totem to your offhand", Module.Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null || mc.player.inventoryMenu.getSlot(45).getItem().getItem() == Items.TOTEM_OF_UNDYING) return;

        for (var i = 0;i <= 44; i++){
            if(mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.TOTEM_OF_UNDYING){
                mc.getConnection().send(new ServerboundContainerClickPacket(0, i - 100, i, 0, ClickType.PICKUP, new ItemStack(Items.TOTEM_OF_UNDYING),Int2ObjectMaps.emptyMap()));
                mc.getConnection().send(new ServerboundContainerClickPacket(0, i - 101, 45, 0, ClickType.PICKUP, new ItemStack(Items.TOTEM_OF_UNDYING), Int2ObjectMaps.emptyMap()));
                break;
            }
        }
    }
}
