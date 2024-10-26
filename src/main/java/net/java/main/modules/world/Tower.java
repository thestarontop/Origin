package net.java.main.modules.world;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicReference;

public class Tower extends Module {
    public Tower(){super("Tower","not work",Category.WORLD);}
    double lastY;
    boolean needLag;
    @EventTarget
    public void onMotion(MotionEvent event){
            if (mc.player.isOnGround()) {
                lastY = mc.player.getY();
            }
            if (mc.player.getY() >= lastY + 1.0) {
                needLag = true;
            }
            mc.player.setXRot(90);
            if (event.getPre()) {
                mc.player.awardStat(Stats.JUMP);
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().x,0.42,mc.player.getDeltaMovement().z);
            }
    }

    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundMovePlayerPacket  && needLag){
            lastY = mc.player.getY();
            PacketUtils.sendPacketNoEvent(new ServerboundMovePlayerPacket.Pos(mc.player.getX() + mc.player.getX()<0 ? 1000.0 : -1000.0, 0.0, 0.0, false));
            event.cancelEvent();
            needLag = false;
        }
        if (event.getPacket() instanceof ClientboundPlayerPositionPacket){
            place();
        }
    }
    private void place(){
        if (mc.level.getBlockState(new BlockPos(mc.player.getX(),mc.player.getY()-1,mc.player.getZ())).getBlock() != Blocks.AIR) return;
        var BlockMap = BlockUtils.searchBlocks(3);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(10000.0);
        BlockMap.forEach((key, value) -> {
            if (value != Blocks.AIR) {
                var pos = mc.player.getY();
                if (key.getY() < pos) {
                    Vec3 addvalue = new Vec3(0,-1,0);
                    double currentDistance = mc.player.position().add(addvalue).distanceToSqr(new Vec3(key.getX(), key.getY(), key.getZ()));
                    if (currentDistance < closestDistance.get()) {
                        closestDistance.set(currentDistance);
                        closestBlockPos.set(key);
                    }
                }
            }
        });
        BlockPos block = closestBlockPos.get();
        if(block == null) return;
        int maxstack = 0;
        int currentslot = -1;
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
        if (currentslot == -1) return;
        mc.player.getInventory().selected = currentslot;
        InteractionResult result = mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(block.getX(),block.getY(),block.getZ()), RotationUtils.getBlockPlacementDirection(block), block, true));
        if ((result == InteractionResult.SUCCESS)) {
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }

}
