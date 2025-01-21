package net.java.main.modules.world;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.RotationUpdateEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.player.AutoTool;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.MSTimer;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.ListValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static net.java.main.utils.BlockUtils.getBlock;
import static net.java.main.utils.RotationUtils.toRotation;

public class Breaker extends Module {
    public Breaker(){
        super("Breaker","bzd",Category.WORLD);
        this.addValues(mode);
    }
    public ListValue mode = new ListValue("Mode", new String[]{"Normal", "Heypixel"}, "Normal");
    private BlockPos pos = null;
    private float currentDamage = 0F;
    // Surroundings
    private boolean areSurroundings = false;
    @EventTarget
    public void onRotationUpdate(RotationUpdateEvent event) {
        if (pos == null || (!(getBlock(pos) instanceof BedBlock)) || getCenterDistance(pos) > 5) {
            pos = find();
        }

        // Reset current breaking when there is no target block
        if (pos == null) {
            currentDamage = 0F;
            areSurroundings = false;
            return;
        }

        var currentPos = pos;


            if(mode.getValue().equals("Heypixel")) {
                BlockPos blockPos = new BlockPos(currentPos.getX(), currentPos.getY() + 1, currentPos.getZ());

                if (getBlock(blockPos) != Blocks.AIR) {
                    pos = blockPos;
                }
            }
        areSurroundings = true;
    }
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if(pos == null) return;
        LocalPlayer player = mc.player;
                ClientLevel world = mc.level;

                MultiPlayerGameMode controller = mc.gameMode;

                BlockPos currentPos = pos;
            // Destroy block
            if(areSurroundings) {
                // Auto Tool
                AutoTool.switchSlot(currentPos);


                // Minecraft block breaking
                Block block = BlockUtils.getBlock(currentPos);

                if (currentDamage == 0F) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, pos, Direction.DOWN));
                }

                mc.player.swing(InteractionHand.MAIN_HAND);

                currentDamage += block.defaultBlockState().getDestroyProgress(player, world, currentPos);
                world.destroyBlockProgress(player.getId(),currentPos, (int) (currentDamage * 10F) - 1);

                if (currentDamage >= 1F) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.DOWN));
                    controller.stopDestroyBlock();
                    currentDamage = 0F;
                    pos = null;
                    areSurroundings = false;
                }
            }



    }
    /**
     * Find new target block by targetID
     */
    private BlockPos find() {
        var BlockMap = BlockUtils.searchBlocks(6);
        AtomicReference<BlockPos> closestBlockPos = new AtomicReference<>(null);
        AtomicReference<Double> closestDistance = new AtomicReference<>(10.0);

        BlockMap.forEach((key, value) -> {
            if (value instanceof BedBlock) {
                    double currentDistance = mc.player.position().distanceToSqr(new Vec3(key.getX(), key.getY(), key.getZ()));
                    if (currentDistance < closestDistance.get()) {
                        closestDistance.set(currentDistance);
                        closestBlockPos.set(key);
                    }
            }
        });
        return closestBlockPos.get();
    }
    private double getCenterDistance(BlockPos blockPos) {
        return mc.player.distanceToSqr(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ()+ 0.5);
    }
    @Override
    public void onEnable(){
        super.onEnable();
        if (pos != null && !mc.player.isCreative()) {
            mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
        }

        currentDamage = 0F;
        pos = null;
        areSurroundings = false;
    }
    @Override
    public void onDisable(){
        super.onDisable();
        if (pos != null && !mc.player.isCreative()) {
            mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, pos, Direction.DOWN));
        }

        currentDamage = 0F;
        pos = null;
        areSurroundings = false;
    }
}
