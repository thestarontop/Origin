package net.java.main.modules.combat;

import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
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
