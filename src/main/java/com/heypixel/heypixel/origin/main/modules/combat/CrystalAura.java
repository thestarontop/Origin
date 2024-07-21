package com.heypixel.heypixel.origin.main.modules.combat;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.BlockUtils;
import com.heypixel.heypixel.origin.main.utils.Rotation;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;

public class CrystalAura extends Module {
    public CrystalAura(){super("CrystalAura","bzd",Category.COMBAT);}
    private LinkedList<BlockPos> blockslist = new LinkedList<>();
    private LinkedList<BlockPos> placedblocklist = new LinkedList<>();
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var blocks = BlockUtils.searchBlocks(3);
        for (Entity entity :mc.level.entitiesForRendering()) {
            if (entity instanceof AbstractClientPlayer && entity.getId() != mc.player.getId()) {
                if (entity.distanceTo(mc.player) > 5){
                    placedblocklist.clear();
                    blockslist.clear();
                }else {
                    blocks.forEach((key, value) -> {
                        if ((value == Blocks.BEDROCK || value == Blocks.OBSIDIAN)) {
                            if (!blockslist.contains(key)) {
                                blockslist.add(key);
                            }
                        }else{
                            if (blockslist.contains(key)){
                                blockslist.remove(key);
                            }
                        }
                    });
                }
            }
            if (entity instanceof EndCrystal && entity.distanceTo(mc.player) <= 3.2){
                Rotation rotation = RotationUtils.lockView(entity.getBoundingBox(),false,false,false,false,4F).getRotation();
                RotationUtils.setTargetRotation(rotation);
                mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity,false));
                mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                return;
            }
        }
        for (BlockPos block : blockslist){
            if (placedblocklist.contains(block)) {continue;}
            if (mc.player.distanceToSqr(block.getX(),block.getY(),block.getZ()) >5){
                blockslist.remove(block);
                if (placedblocklist.contains(block)){
                    placedblocklist.remove(block);
                }
                continue;
            }
            Rotation.VecRotation rotation = RotationUtils.faceBlock(block);
            RotationUtils.setTargetRotation(rotation.getRotation());
            for (int i = 0;i<9;i++) {
            if (mc.player.inventoryMenu.getSlot(i+36).getItem().getItem() == Items.END_CRYSTAL){
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                break;
            }
            }
            mc.gameMode.useItemOn(mc.player,mc.level, InteractionHand.MAIN_HAND, new BlockHitResult(rotation.getVec(),Direction.DOWN,block,true));
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            placedblocklist.add(block);
            break;
        }
        if (placedblocklist.size() >= blockslist.size()){
            placedblocklist.clear();
        }
    }

}
