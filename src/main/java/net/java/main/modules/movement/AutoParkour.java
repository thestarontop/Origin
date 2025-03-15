package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class AutoParkour extends Module {
    public AutoParkour(){super("AutoParkour","搭路练习",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var BlockMap = BlockUtils.searchBlocks(4);
        for (Map.Entry<BlockPos, Block> entry : BlockMap.entrySet()) {
            BlockPos key = entry.getKey();
            Block value = entry.getValue();
            if (value != Blocks.AIR) {
                double x = key.getX() + 0.5;
                double y = key.getY() + 0.5;
                double z = key.getZ() + 0.5;
                mc.player.setPos(x, y + 1, z);
                mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(x, y+1, z, mc.player.isOnGround()));
            }
        }
    }
}
