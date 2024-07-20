package com.heypixel.heypixel.origin.main.modules.world;


import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.MotionEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.*;
import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.heypixel.heypixel.origin.main.utils.BlockUtils.isReplaceable;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold","bzd", Category.WORLD);
    }

    private int lastGroundY = 0;
    private PlaceInfo targetPlace = null;
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        /*if (mc.player.isOnGround()) {
            lastGroundY = (int)mc.player.getY();
        }
        findBlock();
        place();*/
        if (mc.level.getBlockState(new BlockPos(mc.player.getX(),mc.player.getY()-1,mc.player.getZ())).getBlock() != Blocks.AIR){
            return;
        }
        var BlockMap = BlockUtils.searchBlocks(5);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(10000.0);

        BlockMap.forEach((key, value) -> {
            if (value != Blocks.AIR) {
                    if (key.getY() <= mc.player.getY()) {
                        double currentDistance = mc.player.position().distanceTo(new Vec3(key.getX(), key.getY(), key.getZ()));
                        if (currentDistance < closestDistance.get()) {
                            closestDistance.set(currentDistance);
                            closestBlockPos.set(key);
                        }
                    }

            }
        });
    BlockPos block = closestBlockPos.get();
            Rotation.VecRotation rotation = RotationUtils.faceBlock(block);
            if (rotation != null) {
                RotationUtils.setTargetRotation(rotation.getRotation());
                for (int i = 0; i < 9; i++) {
                    if (mc.player.inventoryMenu.getSlot(i + 36).getItem().getItem() instanceof BlockItem) {
                        mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                        break;
                    }
                }
                InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(rotation.getVec(), RotationUtils.getPlacementDirection(), block, true));
                if ((result == InteractionResult.SUCCESS)) {
                    mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                }
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            }


    }

    private void place() {
        if (targetPlace == null) {
            return;
        }
        if (lastGroundY - 1 != (int)targetPlace.getVec3().y) {
            return;
        }

        for (int i = 0;i<9;i++){
            if (mc.player.inventoryMenu.getSlot(i+36).getItem().getItem() instanceof BlockItem){
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                break;
            }
        }
       InteractionResult result =  mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND,new BlockHitResult(targetPlace.getVec3(),targetPlace.getEnumFacing(),targetPlace.getBlockPos(),false));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));

        // Reset
        targetPlace = null;
    }

    private void findBlock() {
        BlockPos blockPosition;
         if (lastGroundY <= mc.player.getY()) {
            blockPosition = new BlockPos(mc.player.getX(), lastGroundY - 1.0, mc.player.getZ());
        } else {
            blockPosition = new BlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ()).below();
        }

        if ((!BlockUtils.isReplaceable(blockPosition) || search(blockPosition, false))) {
            return;
        }

        for (int dist = 1; dist <= 6; dist++) {
            for (int x = -dist; x <= dist; x++) {
                for (int z = -dist; z <= dist; z++) {
                    if (search(blockPosition.offset(x, 0, z), true)) {
                        return;
                    }
                }
            }
        }
    }
    private boolean search(BlockPos blockPosition, boolean checks) {
        if (!isReplaceable(blockPosition)) return false;
        Vec3 eyesPos = new Vec3(
                mc.player.getX(),
                mc.player.getBoundingBox().minY + mc.player.getEyeHeight(),
                mc.player.getZ()
        );
        Rotation.PlaceRotation bestPlaceRotation = null;
        double bestRotationDifference = Double.MAX_VALUE;

        for (Direction side : Direction.values()) {
            var neighbor = blockPosition.offset(side.getNormal());
            if (!BlockUtils.canBeClicked(neighbor)) continue;

            var dirVec = Vec3.atCenterOf(side.getNormal());
            var hitVecTemplate = new Vec3(dirVec.x * 0.5, dirVec.y * 0.5, dirVec.z * 0.5);

            for (double xSearch = 0.1; xSearch < 0.9; xSearch += 0.1) {
                for (double ySearch = 0.1; ySearch < 0.9; ySearch += 0.1) {
                    for (double zSearch = 0.1; zSearch < 0.9; zSearch += 0.1) {
                        var posVec = new Vec3(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ()).add(xSearch, ySearch, zSearch);
                        var distanceSqPosVec = eyesPos.distanceToSqr(posVec);
                        var hitVec = posVec.add(hitVecTemplate);

                        if (checks) {
                            double distanceToHitVec = eyesPos.distanceToSqr(hitVec);
                            double distanceToPosVecPlusDir = eyesPos.distanceToSqr(posVec.add(dirVec));
                            var clipResult = mc.level.clip(new ClipContext(eyesPos, hitVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, null));

                            if (distanceToHitVec > 18.0 || distanceSqPosVec > distanceToPosVecPlusDir || clipResult.getType() != HitResult.Type.MISS) {
                                continue;
                            }
                        }

                        var diffX = hitVec.x - eyesPos.x;
                        var diffY = hitVec.y - eyesPos.y;
                        var diffZ = hitVec.z - eyesPos.z;
                        var diffXZ = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
                        var rotation = new Rotation(
                                (float) Mth.wrapDegrees(Math.toDegrees(Mth.atan2(diffZ, diffX)) - 90f),
                                (float) Mth.wrapDegrees((-Math.toDegrees(Mth.atan2(diffY, diffXZ))))
                        );
                        var rotationVector = RotationUtils.getVectorForRotation(rotation);
                        var vector = eyesPos.add(
                                rotationVector.x * 4,
                                rotationVector.y * 4,
                                rotationVector.z * 4
                        );

                        var obj = mc.level.clip(new ClipContext(eyesPos, vector, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, null));
                        mc.player.sendMessage(Component.nullToEmpty(obj.getType().toString()), mc.player.getUUID());

                        if (obj.getType() != HitResult.Type.BLOCK || !obj.getBlockPos().equals(neighbor)) {
                            continue;
                        }

                        double rotationDifference = RotationUtils.getRotationDifference(rotation);
                        if (rotationDifference < bestRotationDifference) {
                            bestPlaceRotation = new Rotation.PlaceRotation(new PlaceInfo(neighbor, side.getOpposite(), hitVec), rotation);
                            bestRotationDifference = rotationDifference;
                        }
                    }
                }
            }
        }

        if (bestPlaceRotation == null) return false;

        int calcyaw = Math.round((RotationUtils.movingYaw() - 180) / 45) * 45;
        float calcpitch = (calcyaw % 90 == 0) ? 82f : 78f;
        Rotation lockRotation = new Rotation(calcyaw, calcpitch);

        RotationUtils.setTargetRotation(lockRotation);

        targetPlace = bestPlaceRotation.getPlaceInfo();
        return true;
    }

}
