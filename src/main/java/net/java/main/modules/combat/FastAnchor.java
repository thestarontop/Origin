package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FastAnchor extends Module {
    public FastAnchor(){
        super("FastAnchor","bzd",Category.COMBAT);
        addValues(silent);
    }
    public BooleanValue silent = new BooleanValue("silentRotation",true);
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var blocks = BlockUtils.searchBlocks(4);
        for (Map.Entry<BlockPos, Block> entry : blocks.entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            double blockCenterX = key.getX() + 0.5;
            double blockCenterY = key.getY() + 0.5;
            double blockCenterZ = key.getZ() + 0.5;
            if (value instanceof RespawnAnchorBlock && mc.player.distanceToSqr(blockCenterX,blockCenterY,blockCenterZ) >= 2.0){
                if (mc.level.getBlockState(key).getValue(RespawnAnchorBlock.CHARGE) <= 0){
                    int currentslot = -1;
                    for (int i = 0; i < 9; i++) {
                        ItemStack itemstack = mc.player.inventoryMenu.getSlot(i + 36).getItem();
                        Item item =itemstack.getItem();
                        if (item == Items.GLOWSTONE) {
                                currentslot = i;
                        }
                    }
                    if (currentslot == -1) return;
                    Rotation rotation = RotationUtils.getBlockPlacementRotation(key);
                    if (silent.getValue()) {
                        RotationUtils.setTargetRotation(rotation);
                    }else{
                        rotation.toPlayer(mc.player);
                    }
                    mc.getConnection().send(new ServerboundSetCarriedItemPacket(currentslot));
                    mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND,new BlockHitResult(new Vec3(blockCenterX,blockCenterY,blockCenterZ), RotationUtils.getBlockPlacementDirection(key),key,true)));
                    mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
                    mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND,new BlockHitResult(new Vec3(blockCenterX,blockCenterY,blockCenterZ), RotationUtils.getBlockPlacementDirection(key),key,true)));
                    break;
                }else{
                    Rotation rotation = RotationUtils.getBlockPlacementRotation(key);
                    if (silent.getValue()) {
                        RotationUtils.setTargetRotation(rotation);
                    }else{
                        rotation.toPlayer(mc.player);
                    }
                    mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND,new BlockHitResult(new Vec3(blockCenterX,blockCenterY,blockCenterZ), RotationUtils.getBlockPlacementDirection(key),key,true)));
                    break;
                }
            }
        }
    }
}
