package net.java.main.modules.combat;

import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ThrowableAura extends Module {
    public ThrowableAura(){super("ThrowableAura","bzd", Category.COMBAT);}

    MSTimer mstimer = new MSTimer();
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mstimer.hasTimePassed(250)) {
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (!KillAura.isEnemy(entity) || entity.getId() == mc.player.getId() ) continue;

                for (int i = 0; i <= 8; i++) {
                    if (mc.player.inventoryMenu.getSlot(i + 36).getItem().getItem() == Items.SNOWBALL) {
                        mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                        mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                        var boundingBox = entity.getBoundingBox();
                        boundingBox = boundingBox.expandTowards(0.0,2.14,0.0);

                        if(mc.player.getY() - entity.getY() <= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,-3.0,0.0);


                        if(mc.player.getY() - entity.getY() >= 0.25)
                            boundingBox = boundingBox.expandTowards(0.0,0.1,0.0);

                        Rotation.VecRotation prevrotation = RotationUtils.lockView(boundingBox,false,true,true,false,4F);
                        if(prevrotation == null) return;
                        Rotation rotation = prevrotation.getRotation();
                        RotationUtils.setTargetRotation(rotation);
                        mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
                        mstimer.reset();
                        return;
                    }
                }
            }
        }


    }
}
