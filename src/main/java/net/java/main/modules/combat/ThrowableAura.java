package net.java.main.modules.combat;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class ThrowableAura extends Module {
    public ThrowableAura(){super("ThrowableAura","bzd", Category.COMBAT);}
    private int ticks = 0;
    @EventTarget
    public void onUpdate(UpdateEvent event){
            ticks++;
            if (ticks >= 10) {
                if(KillAura.target == null) return;
                for (Entity entity : mc.level.entitiesForRendering()) {
                    if (!(entity instanceof Player) || entity.getId() == mc.player.getId()) continue;

                        for (int i = 0; i <= 8; i++) {
                            if (mc.player.inventoryMenu.getSlot(i + 36).getItem().getItem() == Items.SNOWBALL) {
                                mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                                mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                                mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
                                ticks = 0;
                                return;
                            }
                        }
                }
            }


    }
}
