package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.utils.RotationUtils;
import net.java.main.value.FloatValue;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;

public class AutoRod extends Module {
    public AutoRod() {
        super("AutoRod","bitch", Category.COMBAT);
    }
    public FloatValue delay = new FloatValue("Delay", 500f, 1, 5000);
    public FloatValue range = new FloatValue("Min Range", 7f, 1, 10);

    MSTimer timer = new MSTimer();


    int lastSlot;;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        Iterable<Entity> entitiylist = mc.level.entitiesForRendering();
        for (Entity entity : entitiylist) {
            if (mc.player.distanceTo(entity) < 4) {
           return;
            }
        }

        if (mc.level == null || mc.player == null )
            return;

        if (timer.hasTimePassed(delay.getValue().longValue())) {
            for (var i = 36; i <= 45; i++) {
                if (mc.player.inventoryMenu.getSlot(i).getItem().getItem()==Items.FISHING_ROD) {
                    if(i != mc.player.getInventory().selected) lastSlot = mc.player.getInventory().selected;
                    mc.player.getInventory().selected=i-36;
                    mc.gameMode.useItem(mc.player,mc.level,InteractionHand.MAIN_HAND);
                    return;
                }
            }
            lastSlot = mc.player.getInventory().selected;

        }
        else mc.player.getInventory().selected=lastSlot;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(mc.player!=null)
            lastSlot = mc.player.getInventory().selected;
    }
}







