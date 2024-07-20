package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class AutoSoup extends Module {
    public AutoSoup() {
        super("AutoSoup","okay", Module.Category.COMBAT);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null || mc.player.inventoryMenu.getSlot(mc.player.getInventory().selected+36).getItem().getItem() == Items.MUSHROOM_STEW || mc.player.getHealth() > 10) return;
        var a = mc.player.getInventory().selected;
        for (var i = 36;i <= 45; i++){
            if(mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.MUSHROOM_STEW && i != a +36){
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(i-36));
                mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.DROP_ITEM, BlockPos.ZERO, Direction.DOWN));
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(a));
                break;
            }
        }
    }
}
