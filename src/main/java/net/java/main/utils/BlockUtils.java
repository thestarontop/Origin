package net.java.main.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

public class BlockUtils extends MinecraftInstance{
    public static Map<BlockPos, Block> searchBlocks(int radius) {
        Map<BlockPos, Block> blocks = new HashMap<>();

        LocalPlayer thePlayer = Minecraft.getInstance().player;
        if (thePlayer == null) {
            return blocks;
        }

        int playerPosX = (int) thePlayer.position().x;
        int playerPosY = (int) thePlayer.position().y;
        int playerPosZ = (int) thePlayer.position().z;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos blockPos = new BlockPos(playerPosX + x, playerPosY + y, playerPosZ + z);
                    Block block = getBlock(blockPos);
                    if (block != null) {
                        blocks.put(blockPos, block);
                    }
                }
            }
        }

        return blocks;
    }
    public static Direction getBlockDirection(LocalPlayer player, BlockPos blockPos) {
        Vec3 playerPosition = player.position();
        Vec3 playerDirection = player.getLookAngle();

        double blockPosX = blockPos.getX() + 0.5;
        double blockPosY = blockPos.getY() + 0.5;
        double blockPosZ = blockPos.getZ() + 0.5;

        Vec3 blockVector = new Vec3(blockPosX - playerPosition.x, blockPosY - playerPosition.y, blockPosZ - playerPosition.z);
        Vec3 normalizedBlockVector = blockVector.normalize();

        double dotForward = playerDirection.dot(normalizedBlockVector);
        double dotRight = playerDirection.cross(normalizedBlockVector).y;

        if (Math.abs(normalizedBlockVector.x) > Math.abs(normalizedBlockVector.y) && Math.abs(normalizedBlockVector.x) > Math.abs(normalizedBlockVector.z)) {
            return normalizedBlockVector.x > 0 ? Direction.EAST : Direction.WEST;
        } else if (Math.abs(normalizedBlockVector.y) > Math.abs(normalizedBlockVector.x) && Math.abs(normalizedBlockVector.y) > Math.abs(normalizedBlockVector.z)) {
            return normalizedBlockVector.y > 0 ? Direction.UP : Direction.DOWN;
        } else {
            return normalizedBlockVector.z > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
    public static Direction getOppositeBlockDirection(LocalPlayer player, BlockPos blockPos) {
        Direction direction = getBlockDirection(player, blockPos);
        return direction.getOpposite();
    }


    public static List<Pair<BlockPos, Block>> getBlocksInRadius(int radius) {

        ArrayList<Pair<BlockPos, Block>> blocks = new ArrayList<>();

        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null) {
            return null;
        }

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    double x1 = Minecraft.getInstance().player.getX();
                    double y1 = Minecraft.getInstance().player.getY();
                    double z1 = Minecraft.getInstance().player.getZ();

                    BlockPos blockPos = new BlockPos(x + x1, y + y1, z + z1);
                    blocks.add(new Pair<>(blockPos, Minecraft.getInstance().level.getBlockState(blockPos).getBlock()));

                }
            }

        }
        return blocks;
    }

    public static Block getBlock(BlockPos blockPos) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return null;
        }
        BlockState blockState = world.getBlockState(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
        return blockState.getBlock();
    }
    public static boolean isReplaceable(BlockPos blockPos) {
        return getMaterial(blockPos).isReplaceable();
    }
    public static Material getMaterial(BlockPos blockPos) {
        return getBlock(blockPos).defaultBlockState().getMaterial();
    }
    public static boolean canBeClicked(BlockPos blockPos) {
        return isPathfindable(getBlock(blockPos).defaultBlockState(), Minecraft.getInstance().level,blockPos,PathComputationType.LAND) &&
                Minecraft.getInstance().level.getWorldBorder().isWithinBounds(blockPos);
    }
    public static boolean isPathfindable(BlockState arg, BlockGetter arg2, BlockPos arg3, PathComputationType arg4) {
        switch (arg4) {
            case LAND -> {
                return !arg.isCollisionShapeFullBlock(arg2, arg3);
            }
            case WATER -> {
                return arg2.getFluidState(arg3).is(FluidTags.WATER);
            }
            case AIR -> {
                return !arg.isCollisionShapeFullBlock(arg2, arg3);
            }
        }
        return false;
    }

}
