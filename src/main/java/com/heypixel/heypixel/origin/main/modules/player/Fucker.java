package com.heypixel.heypixel.origin.main.modules.player;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.BlockUtils;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Fucker extends Module {
    public Fucker() {super("Fucker","auto dig bed",Category.PLAYER);}
    public static final LinkedList<BlockPos> bedList = new LinkedList<>();
    public static final LinkedList<BlockPos> canDig = new LinkedList<>();

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.level == null || mc.player == null) return;

        var blockMap = BlockUtils.searchBlocks(5);
        blockMap.forEach((key, value) -> {
            if(value == Blocks.BLACK_BED || value == Blocks.BLUE_BED || value == Blocks.BROWN_BED || value == Blocks.CYAN_BED || value == Blocks.GRAY_BED || value == Blocks.GREEN_BED || value == Blocks.LIGHT_BLUE_BED || value == Blocks.LIGHT_GRAY_BED || value == Blocks.LIME_BED || value == Blocks.MAGENTA_BED || value == Blocks.ORANGE_BED || value == Blocks.PINK_BED || value == Blocks.PURPLE_BED || value == Blocks.RED_BED || value == Blocks.WHITE_BED || value == Blocks.YELLOW_BED) {
                if (!bedList.contains(key)){
                    bedList.add(key);
                }
            }
        });
        for(BlockPos bed : bedList){
            var b1 = bed.north(1);
            var b2 = bed.north(-1);
            var b3 = bed.south(1);
            var b4 = bed.south(-1);
            var b5 = bed.east(1);
            var b6 = bed.east(-1);
            var b7 = bed.west(1);
            var b8 = bed.west(-1);
            var b9 = bed.above(1);
            var b10 = bed.above(1);
            if (mc.level.getBlockState(b1).getBlock() == Blocks.AIR || mc.level.getBlockState(b2).getBlock() == Blocks.AIR || mc.level.getBlockState(b3).getBlock() == Blocks.AIR || mc.level.getBlockState(b4).getBlock() == Blocks.AIR || mc.level.getBlockState(b5).getBlock() == Blocks.AIR || mc.level.getBlockState(b6).getBlock() == Blocks.AIR || mc.level.getBlockState(b7).getBlock() == Blocks.AIR || mc.level.getBlockState(b8).getBlock() == Blocks.AIR || mc.level.getBlockState(b9).getBlock() == Blocks.AIR || mc.level.getBlockState(b10).getBlock() == Blocks.AIR)
                return;

            var block1 = mc.level.getBlockState(b1).getBlock();
            var block9 = mc.level.getBlockState(b9).getBlock();
            PacketUtils.sendPacketNoEvent(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK,b9,mc.player.getDirection()));

        }
    }
}
