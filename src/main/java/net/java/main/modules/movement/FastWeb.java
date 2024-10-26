package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.utils.MovementUtils;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.minecraft.world.phys.AABB;

import static net.minecraft.world.level.block.Blocks.COBWEB;

public class FastWeb extends Module {
    public FastWeb(){super("FastWeb","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var searchs = BlockUtils.searchBlocks(2);
        searchs.forEach((key,value)->{
            if (value == COBWEB){
                if (new AABB(key.getX(), key.getY(), key.getZ(),
                        key.getX() + 1, key.getY() + 1, key.getZ() + 1).intersects(mc.player.getBoundingBox())){
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,0,mc.player.getDeltaMovement().z);
                    MovementUtils.strafe(0.3436f);
                    if (mc.options.keyJump.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,1.2199,0));
                        return;
                    }
                    if (mc.options.keyShift.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,-3.1798,0));
                        return;
                    }
                }
            }
        });

    }
}
