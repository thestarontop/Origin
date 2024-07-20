package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.MSTimer;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SnowballItem;

public class ThrowAbleAura extends Module {
    public ThrowAbleAura(){super("ThrowAbleAura","bzd", Category.COMBAT);}
    private MSTimer timer = new MSTimer();
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (timer.hasTimePassed(500)) {
            for (Entity entity :mc.level.entitiesForRendering()){
                if (!(entity instanceof Player)) continue;

                if (mc.player.distanceTo(entity) >= 3.21 && mc.player.distanceTo(entity) <= 7.0){
                    for (int i = 0;i<8;i++){
                        if (mc.player.inventoryMenu.getSlot(i+36).getItem().getItem() instanceof SnowballItem){
                            if (RotationUtils.targetRotation == null){
                                PacketUtils.sendPacketNoEvent(new ServerboundSetCarriedItemPacket(i));
                                Rotation rotation = RotationUtils.lockView(entity.getBoundingBox(),false,false,true,false,4).getRotation();
                                RotationUtils.setTargetRotation(rotation);
                                PacketUtils.sendPacketNoEvent(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                                PacketUtils.sendPacketNoEvent(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
                                timer.reset();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
