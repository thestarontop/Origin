package com.mojang.main.modules.combat;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.Rotation;
import com.mojang.main.utils.RotationUtils;
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
        if (RotationUtils.targetRotation == null) {
            ticks++;
            if (ticks >= 10) {
                for (Entity entity : mc.level.entitiesForRendering()) {
                    if (!(entity instanceof Player) || entity.getId() == mc.player.getId()) continue;

                    if (mc.player.distanceTo(entity) >= 3.21 && mc.player.distanceTo(entity) <= 7.0) {
                        for (int i = 0; i <= 8; i++) {
                            if (mc.player.inventoryMenu.getSlot(i + 36).getItem().getItem() == Items.SNOWBALL) {
                                mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                                Rotation rotation = RotationUtils.searchCenter(entity.getBoundingBox(), false, false, true, false, 7).getRotation();
                                RotationUtils.setTargetRotation(rotation, 2);
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

    }
}
