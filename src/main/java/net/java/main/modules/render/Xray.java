package net.java.main.modules.render;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.utils.BlockUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Xray extends Module {
    public Xray(){super("Xray","render blocks",Category.RENDER);}
    public static final List<BlockPos> visitedPositions = new ArrayList<>();
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;
        var BlockMap = BlockUtils.searchBlocks(4);
        for (Map.Entry<BlockPos, Block> entry : BlockMap.entrySet()) {
            BlockPos key = entry.getKey();
            Block value = entry.getValue();
            if(value != Blocks.AIR && value != Blocks.GRASS_BLOCK && value != Blocks.DIRT) {
                if(!visitedPositions.contains(key)) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, key, Direction.DOWN));
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, key, Direction.DOWN));
                    visitedPositions.add(key);
                    return;
                }
            }
        }
    }
    @Override
    public void onEnable() {
        super.onEnable();
        visitedPositions.clear();
    }
}
