package net.java.main.modules.movement;

import net.java.main.event.events.UpdateEvent;
import net.java.main.utils.MovementUtils;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.value.FloatValue;
import net.minecraft.world.phys.AABB;

import static net.minecraft.world.level.block.Blocks.COBWEB;

public class FastWeb extends Module {
    public FastWeb(){
    super("FastWeb","bzd",Category.MOVEMENT);
    this.addValues(xz,up,down);
    }
    public FloatValue xz = new FloatValue("xzmotion",0.1f,0.1f,0.5f);
    public FloatValue up = new FloatValue("upmotion",0.1f,0.1f,0.5f);
    public FloatValue down = new FloatValue("downmotion",0.1f,0.1f,0.5f);
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var searchs = BlockUtils.searchBlocks(2);
        searchs.forEach((key,value)->{
            if (value == COBWEB){
                if (new AABB(key.getX(), key.getY(), key.getZ(), key.getX() + 1, key.getY() + 1, key.getZ() + 1).intersects(mc.player.getBoundingBox())){
                    mc.player.setDeltaMovement(0,0,0);
                    MovementUtils.strafe(xz.getValue());
                    if (mc.options.keyJump.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,up.getValue(),mc.player.getDeltaMovement().z);
                        return;
                    }
                    if (mc.options.keyShift.isDown()){
                        mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,down.getValue(),mc.player.getDeltaMovement().z);
                        return;
                    }
                }
            }
        });

    }
}
