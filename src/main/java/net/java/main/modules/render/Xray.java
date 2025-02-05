package net.java.main.modules.render;

import net.java.main.event.events.Render3DEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.RenderUtils;
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
            if(value == Blocks.DIAMOND_ORE || value == Blocks.IRON_ORE) {
                if(!visitedPositions.contains(key)) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, key, Direction.DOWN));
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, key.east(1), Direction.DOWN));
                    visitedPositions.add(key);
                    break;
                }
            }
        }
    }
    @EventTarget
    public void onRender3D(Render3DEvent event){
        var BlockMap = BlockUtils.searchBlocks(4);
        for (Map.Entry<BlockPos, Block> entry : BlockMap.entrySet()) {
            BlockPos key = entry.getKey();
            Block value = entry.getValue();
            if(value == Blocks.IRON_ORE || value == Blocks.DEEPSLATE_IRON_ORE) {
                RenderUtils.renderBoundingBox(event.getPoseStack(),key,1,0,0);
            }
            if (value == Blocks.DIAMOND_ORE || value == Blocks.DEEPSLATE_DIAMOND_ORE){
                RenderUtils.renderBoundingBox(event.getPoseStack(),key,0,0,1);
            }
        }
    }
    @Override
    public void onEnable() {
        super.onEnable();
        visitedPositions.clear();
    }
}
