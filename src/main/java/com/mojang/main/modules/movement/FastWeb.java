package com.mojang.main.modules.movement;

import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.utils.MovementUtils;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.BlockUtils;

import static net.minecraft.world.level.block.Blocks.COBWEB;

public class FastWeb extends Module {
    public FastWeb(){super("FastWeb","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var searchs = BlockUtils.searchBlocks(2);
        searchs.forEach((key,value)->{
            if (value == COBWEB){
                if (mc.level.getBlockState(key).getCollisionShape(mc.level,key).bounds().intersects(mc.player.getBoundingBox())){
                    MovementUtils.strafe(0.3436f);
                    mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,0,mc.player.getDeltaMovement().z);
                    if (mc.options.keyJump.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,1.2199,0));
                        return;
                    }
                    if (mc.options.keyShift.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0,3.1798,0));
                        return;
                    }
                }
            }
        });

    }
}
