package com.heypixel.heypixel.origin.main.modules.movement;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class NoWeb extends Module {
    public NoWeb(){super("NoWeb","bzd",Category.MOVEMENT);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var searchBlocks = BlockUtils.searchBlocks(4);
        var water2 = BlockUtils.searchBlocks(12);
        searchBlocks.forEach((key, value) -> {
            if(value == Blocks.COBWEB) {
                mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, key, Direction.DOWN));
                mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, key, Direction.DOWN));
                mc.level.setBlock(key,mc.level.getBlockState(new BlockPos(0,114514,0)), 4);
            }
        });
        water2.forEach((key, value) -> {
            if(value == Blocks.WATER || value == Blocks.LAVA) {
                if (mc.player.distanceToSqr(new Vec3(key.getX(),key.getY(),key.getZ())) >= 8.0) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, key, Direction.DOWN));
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, key, Direction.DOWN));
                    mc.level.setBlock(key,mc.level.getBlockState(new BlockPos(0,114514,0)), 4);
                }
            }
        });
    }
}
