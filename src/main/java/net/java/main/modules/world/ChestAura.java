package net.java.main.modules.world;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.MSTimer;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.Map;

public class ChestAura extends Module {
    public ChestAura(){super("ChestAura","bzd",Category.WORLD);}
    public LinkedList<BlockPos> clickedblock = new LinkedList<>();
    private MSTimer mstimer = new MSTimer();
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mstimer.hasTimePassed(250)) {
            var BlockMap = BlockUtils.searchBlocks(4);
            for (Map.Entry<BlockPos, Block> entry : BlockMap.entrySet()) {
                BlockPos key = entry.getKey();
                Block value = entry.getValue();

                if (value == Blocks.CHEST) {
                    if (!clickedblock.contains(key)) {
                        Rotation rotation = RotationUtils.getBlockPlacementRotation(key);
                        RotationUtils.setTargetRotation(rotation);
                        mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()), RotationUtils.getBlockPlacementDirection(key), key, false)));
                        mc.getConnection().send(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                        clickedblock.add(key);
                        break;
                    }
                }
            }
            mstimer.reset();
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundUseItemOnPacket){
            clickedblock.add(((ServerboundUseItemOnPacket) event.getPacket()).getHitResult().getBlockPos());
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        clickedblock.clear();
    }
}
