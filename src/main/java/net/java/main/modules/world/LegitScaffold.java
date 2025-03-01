package net.java.main.modules.world;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.TickEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.*;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.openjdk.nashorn.internal.objects.Global.Infinity;
import static org.openjdk.nashorn.internal.objects.Global.getArrayBuffer;

public class LegitScaffold extends  Module {
    public LegitScaffold() {
        super("LegitScaffold","bzd", Module.Category.WORLD);
        addValues(keeplength,eagle,airtick);
    }
    public FloatValue airtick = new FloatValue("AirTick",3f,0f,10f);

    public FloatValue keeplength = new FloatValue("KeepLength", 0f, 0.0f, 20f);
    public BooleanValue eagle = new BooleanValue("Eagle",false);

    private boolean canTellyPlace;
    public int onGroundTicks, offGroundTicks;
    BlockPos block = null;
    double starty;
    public PlaceInfo placeInfo;

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
            canTellyPlace = offGroundTicks >= airtick.getValue();
        }
        if (!canTellyPlace) return;
        search();

        if(block == null) return;
        Rotation rotation = RotationUtils.getRotationBlock(block,0f);
        RotationUtils.setTargetRotation(rotation,keeplength.getValue().byteValue());

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
        mc.player.getInventory().selected = currentslot;
        InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(block.getX(),block.getY(),block.getZ()), RotationUtils.getBlockPlacementDirection(block), block, true));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        if (!mc.options.keyJump.isDown()) {
            mc.player.setSprinting(RotationUtils.targetRotation == null);
        }
        if (mc.options.keyJump.isDown()) {
            mc.player.setSprinting(!canTellyPlace);
        }else {
            mc.player.setSprinting(RotationUtils.targetRotation == null);
        }
    }


    public void search() {
        Map<BlockPos, Block> blockMap = findBlock(3);
        Vec3 playerPos = Minecraft.getInstance().player.getEyePosition(4);
        findClosestBlock(blockMap, playerPos);
    }

    private Map<BlockPos, Block> findBlock(int radius) {
        Map<BlockPos, Block> blocks = new HashMap<>();
        int playerX = (int) Minecraft.getInstance().player.getX();
        int playerY = (int) Minecraft.getInstance().player.getY();
        int playerZ = (int) Minecraft.getInstance().player.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos blockPos = new BlockPos(playerX + x, playerY + y, playerZ + z);
                    blocks.put(blockPos, getBlock(blockPos));
                }
            }
        }
        return blocks;
    }

    private void findClosestBlock(Map<BlockPos, Block> blockMap, Vec3 playerPos) {
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(Double.MAX_VALUE);

        blockMap.forEach((key, value) -> {
            if (value != Blocks.AIR) {
                int playerY = (int) Minecraft.getInstance().player.getY() - 1;
                if (key.getY() <= playerY) {
                    double blockCenterX = key.getX() + 0.5;
                    double blockCenterY = key.getY() + 0.5;
                    double blockCenterZ = key.getZ() + 0.5;
                    Vec3 blockCenter = new Vec3(blockCenterX, blockCenterY, blockCenterZ);
                    double currentDistance = playerPos.distanceTo(blockCenter);
                    if (currentDistance < closestDistance.get()) {
                        closestDistance.set(currentDistance);
                        closestBlockPos.set(key);
                    }
                }
            }
        });

        BlockPos block = closestBlockPos.get();
        // Assuming there's a variable 'block' to store the result
        this.block = block;
    }

    public static Block getBlock(final BlockPos blockPos) {
        return mc.level.getBlockState(blockPos).getBlock();
    }


    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (block != null){
            RenderUtils.renderBoundingBox(event.getPoseStack(),block,255,0.0F,0.0F);
        }
    }

}
