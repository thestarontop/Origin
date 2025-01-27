package net.java.main.modules.world;


import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.utils.*;
import net.java.main.value.BooleanValue;
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

import static net.java.main.utils.BlockUtils.*;

public class Scaffold extends Module {
    public Scaffold() {
        super("Scaffold","bzd", Category.WORLD);
        addValues(silentrotation,silentautoblock,samey);
    }
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);
    public BooleanValue silentautoblock = new BooleanValue("SilentAutoBlock",false);
    public BooleanValue samey = new BooleanValue("SameY",false);
    BlockPos block = null;
    double starty;
    @EventTarget
    public void onUpdate(UpdateEvent event) {

        mc.player.setSprinting(RotationUtils.targetRotation == null);
        var BlockMap = BlockUtils.searchBlocks(3);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(100.0);
        BlockMap.forEach((key, value) -> {
            if (value != Blocks.AIR && value != Blocks.GLASS) {
                if (!samey.getValue()) {
                    var playerY = mc.player.getY() -1;
                    if (key.getY() <= playerY) {
                        double blockCenterX = key.getX() + 0.5;
                        double blockCenterY = key.getY() + 0.5;
                        double blockCenterZ = key.getZ() + 0.5;
                        double currentDistance = mc.player.position().distanceToSqr(new Vec3(blockCenterX, blockCenterY, blockCenterZ));
                        if (currentDistance < closestDistance.get()) {
                            closestDistance.set(currentDistance);
                            closestBlockPos.set(key);
                        }
                    }
                }else{
                    var playerY = starty -1;
                    if (key.getY()+0.5 <= playerY) {
                        double blockCenterX = key.getX() + 0.5;
                        double blockCenterY = key.getY() + 0.5;
                        double blockCenterZ = key.getZ() + 0.5;
                        double currentDistance = mc.player.position().distanceToSqr(new Vec3(blockCenterX, blockCenterY, blockCenterZ));
                        if (currentDistance < closestDistance.get()) {
                            closestDistance.set(currentDistance);
                            closestBlockPos.set(key);
                        }
                    }
                }
            }
        });
        block = closestBlockPos.get();
        if (mc.level.getBlockState(new BlockPos(mc.player.getX(),mc.player.getY()-1,mc.player.getZ())).getBlock() != Blocks.AIR){
            return;
        }
        if(block == null) return;
        Rotation rotation = RotationUtils.getBlockPlacementRotation(block);
        if (silentrotation.getValue()) {
            RotationUtils.setTargetRotation(rotation);
        }else{
            rotation.toPlayer(mc.player);
        }
        int maxstack = 0;
        int currentslot = -1;
        ItemStack currentstack = mc.player.getMainHandItem();
        ItemStack blockstack = null;
        for (int i = 0; i < 9; i++) {
            ItemStack itemstack = mc.player.inventoryMenu.getSlot(i + 36).getItem();
            Item item =itemstack.getItem();
            if (item instanceof BlockItem) {
                if (itemstack.getCount() > maxstack){
                    maxstack = itemstack.getCount();
                    currentslot = i;
                    blockstack = itemstack;
                }
            }
        }
        if (currentslot == -1) return;
        if (silentautoblock.getValue()) {
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(currentslot));
            mc.player.setItemInHand(InteractionHand.MAIN_HAND, blockstack);
        }else {
            mc.player.getInventory().selected = currentslot;
        }

        InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(block.getX(),block.getY(),block.getZ()), RotationUtils.getBlockPlacementDirection(block), block, true));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        if (silentautoblock.getValue()) {
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            mc.player.setItemInHand(InteractionHand.MAIN_HAND, currentstack);
        }
        mc.player.setSprinting(RotationUtils.targetRotation == null);
    }
    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (block != null){
            RenderUtils.renderBoundingBox(event.getPoseStack(),block,0.5F,0.0F,0.5F);
        }
    }
    @Override
    public void onEnable(){
        super.onEnable();
        starty = mc.player.position().y;
    }

}
