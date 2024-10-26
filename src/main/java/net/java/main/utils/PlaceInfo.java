package net.java.main.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PlaceInfo {
    private final BlockPos blockPos;
    private final Direction enumFacing;
    private Vec3 vec3;

    public PlaceInfo(BlockPos blockPos, Direction enumFacing) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public PlaceInfo(BlockPos blockPos, Direction enumFacing, Vec3 vec3) {
        this.blockPos = blockPos;
        this.enumFacing = enumFacing;
        this.vec3 = vec3;
    }

    public static PlaceInfo get(BlockPos blockPos) {
        if (BlockUtils.canBeClicked(blockPos.below())) {
            return new PlaceInfo(blockPos.below(), Direction.UP);
        } else if (BlockUtils.canBeClicked(blockPos.north())) {
            return new PlaceInfo(blockPos.north(), Direction.SOUTH);
        } else if (BlockUtils.canBeClicked(blockPos.south())) {
            return new PlaceInfo(blockPos.south(), Direction.NORTH);
        } else if (BlockUtils.canBeClicked(blockPos.west())) {
            return new PlaceInfo(blockPos.west(), Direction.EAST);
        } else if (BlockUtils.canBeClicked(blockPos.east())) {
            return new PlaceInfo(blockPos.east(), Direction.WEST);
        } else {
            return new PlaceInfo(blockPos, Direction.UP); // Default case
        }
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Direction getEnumFacing() {
        return enumFacing;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }
}