package net.java.main.modules.misc;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.RotationUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class FastBuilder extends Module {
    public FastBuilder(){super("FastBuilder","bzd",Category.MISC);}
    private Map<BlockPos, Block> map;

    @Override
    public void onEnable() {
        super.onEnable();
        map = BlockUtils.searchBlocks(8);
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        for (Map.Entry<BlockPos, Block> entry : map.entrySet()) {
            BlockPos key = entry.getKey();
            Block value = entry.getValue();
            
            if (value == mc.level.getBlockState(key).getBlock() ||value == Blocks.AIR || mc.player.distanceToSqr(new Vec3(key.getX(), key.getY(), key.getZ())) > 5) continue;
            

            for (int i = 0; i < 9; i++) {
                Item item = mc.player.inventoryMenu.getSlot(i + 36).getItem().getItem();
                if (item == value.asItem()) {
                    mc.player.getInventory().selected = i;
                    mc.gameMode.useItemOn(mc.player, mc.level, InteractionHand.MAIN_HAND,
                            new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()),
                                    RotationUtils.getBlockPlacementDirection(key),
                                    key, false));
                    return;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        map.clear();
    }

}
