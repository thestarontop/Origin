package net.java.main.modules.player;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class GApple extends Module {
    public GApple(){super("GApple","bzd",Category.PLAYER);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (KillAura.target != null && mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE){
            if (mc.player.getHealth() <= 18 && !mc.player.isUsingItem()){
                mc.gameMode.useItem(mc.player,mc.level,InteractionHand.OFF_HAND);
            }
        }
    }
}
