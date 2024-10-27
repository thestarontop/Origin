package net.java.main.modules.player;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.java.main.modules.movement.NoSlow;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;

public class GApple extends Module {
    public GApple() {
        super("GApple", "bzd", Category.PLAYER);
    }
    private int ticks = 0;
    private boolean eating = false;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (eating){
            ticks ++;
        }
        if (ticks >= 30){
            eating = false;
            ticks = 0;
        }
        if (KillAura.target != null && mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.GOLDEN_APPLE) {
            ChatManager.sendChat("有金苹果");
            if (mc.player.getHealth() <= 18 && !mc.player.isUsingItem() && NoSlow.shouldnoslow && !eating) {
                ChatManager.sendChat("吃上了");
                mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.OFF_HAND));
            }
        }
        if (!NoSlow.shouldnoslow){
            eating = true;
        }
    }
}
