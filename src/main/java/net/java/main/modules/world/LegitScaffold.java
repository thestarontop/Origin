package net.java.main.modules.world;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.TickEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.RenderUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.openjdk.nashorn.internal.objects.Global.Infinity;
import static org.openjdk.nashorn.internal.objects.Global.getArrayBuffer;

public class LegitScaffold extends  Module {
    public LegitScaffold() {
        super("LegitScaffold","bzd", Module.Category.WORLD);
        addValues(keeplength,eagle);
    }
    public FloatValue keeplength = new FloatValue("KeepLength", 0f, 0.0f, 20f);
    public BooleanValue eagle = new BooleanValue("Eagle",false);
    private boolean canTellyPlace;
    public int onGroundTicks, offGroundTicks;

    BlockPos block = null;
    double starty;
    @Override
    public void onEnable(){
        super.onEnable();
        starty = mc.player.position().y;
        canTellyPlace = false;

    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.options.keyShift.setDown(false);
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        //motionevent他妈的每tick触发两次
        if (mc.player.isOnGround()) {
            offGroundTicks = 0;
            onGroundTicks++;
        } else {
            onGroundTicks = 0;
            offGroundTicks++;
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(mc.level.getBlockState(new BlockPos(mc.player.position().x, mc.player.position().y - 1, mc.player.position().z)).getBlock() != Blocks.AIR) return;

        if (!mc.options.keyJump.isDown()){
            canTellyPlace = true;
        }
        if (mc.options.keyJump.isDown()) {
            mc.player.setSprinting(!canTellyPlace);
        }else {
            mc.player.setSprinting(RotationUtils.targetRotation == null);
        }
        if (eagle.getValue()){
        boolean isAir = mc.level.getBlockState(new BlockPos(mc.player.position().x, mc.player.position().y - 1, mc.player.position().z)).getBlock() == Blocks.AIR;
        mc.options.keyShift.setDown(isAir);
        }
        if (mc.options.keyJump.isDown()) {
            canTellyPlace = offGroundTicks >= 2;
        }
        if (!canTellyPlace) return;
        var BlockMap = BlockUtils.searchBlocks(3);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(100.0);
        BlockMap.forEach((key, value) -> {
            if (value != Blocks.AIR ) {
                    var playerY = mc.player.getY() -1;
                    if (key.getY() <= playerY) {
                        double blockCenterX = key.getX() + 0.5;
                        double blockCenterY = key.getY() + 0.5 ;
                        double blockCenterZ = key.getZ() + 0.5;

                        double currentDistance = mc.player.position().distanceToSqr(new Vec3(blockCenterX, blockCenterY, blockCenterZ));
                        if (currentDistance < closestDistance.get()) {
                            closestDistance.set(currentDistance);
                            closestBlockPos.set(key);
                        }
                    }
                }
              });
        block = closestBlockPos.get();
        if(block == null) return;
        Rotation rotation = RotationUtils.getBlockPlacementRotation(block);
        RotationUtils.setTargetRotation(rotation);

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
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(currentslot));
            mc.player.setItemInHand(InteractionHand.MAIN_HAND, blockstack);

        InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(block.getX(),block.getY(),block.getZ()), RotationUtils.getBlockPlacementDirection(block), block, true));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            mc.player.setItemInHand(InteractionHand.MAIN_HAND, currentstack);
        if (!mc.options.keyJump.isDown()) {
            mc.player.setSprinting(RotationUtils.targetRotation == null);
        }
        if (mc.options.keyJump.isDown()) {
            mc.player.setSprinting(!canTellyPlace);
        }else {
            mc.player.setSprinting(RotationUtils.targetRotation == null);
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (block != null){
            RenderUtils.renderBoundingBox(event.getPoseStack(),block,0.5F,0.0F,0.5F);
        }
    }


}
