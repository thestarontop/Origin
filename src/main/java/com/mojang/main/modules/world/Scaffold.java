package com.mojang.main.modules.world;


import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.utils.*;
import com.mojang.main.utils.BlockUtils;
import com.mojang.main.utils.PlaceInfo;
import com.mojang.main.utils.Rotation;
import com.mojang.main.utils.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicReference;

import static com.mojang.main.utils.BlockUtils.*;

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
        mc.player.setSprinting(RotationUtils.targetRotation == null);
        if (mc.level.getBlockState(new BlockPos(mc.player.getX(),mc.player.getY()-1,mc.player.getZ())).getBlock() != Blocks.AIR){
            return;
        }
        var BlockMap = BlockUtils.searchBlocks(4);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(10000.0);

        BlockMap.forEach((key, value) -> {
            if (value != Blocks.AIR) {
                var pos = mc.player.getY();
                if (mc.options.keyJump.isDown()){
                    pos = pos-1;
                }
                    if (key.getY() <= pos) {
                        double currentDistance = mc.player.position().add(new Vec3(0,-1,0)).distanceToSqr(new Vec3(key.getX(), key.getY(), key.getZ()));
                        if (currentDistance < closestDistance.get()) {
                            closestDistance.set(currentDistance);
                            closestBlockPos.set(key);
                        }
                    }

            }
        });
    BlockPos block = closestBlockPos.get();
        Rotation rotation = RotationUtils.getBlockPlacementRotation(block);
        RotationUtils.setTargetRotation(rotation);
        int maxstack = 0;
        int currentslot = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack itemstack = mc.player.inventoryMenu.getSlot(i + 36).getItem();
            Item item =itemstack.getItem();
            if (item instanceof BlockItem) {
                if (itemstack.getCount() > maxstack){
                    maxstack = itemstack.getCount();
                    currentslot = i;
                }
            }
        }
        mc.player.getInventory().selected = currentslot;
        InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(block.getX(),block.getY(),block.getZ()), RotationUtils.getBlockPlacementDirection(block), block, true));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        mc.level.setBlock(block,mc.level.getBlockState(block),4);
        mc.player.setSprinting(RotationUtils.targetRotation == null);
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
