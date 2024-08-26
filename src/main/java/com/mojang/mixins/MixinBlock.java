package com.mojang.mixins;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(Block.class)
public class MixinBlock {

    @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
    private static void onRenderChunkLayer(BlockState arg, BlockGetter arg2, BlockPos arg3, Direction arg4, BlockPos arg5, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = arg2.getBlockState(arg5);
        Block block = state.getBlock();

        final List<Block> ORE_BLOCKS = Arrays.asList(
                Blocks.COAL_ORE,
                Blocks.IRON_ORE,
                Blocks.GOLD_ORE,
                Blocks.REDSTONE_ORE,
                Blocks.EMERALD_ORE,
                Blocks.LAPIS_ORE,
                Blocks.DIAMOND_ORE,
                Blocks.NETHER_QUARTZ_ORE,
                Blocks.NETHER_GOLD_ORE,
                Blocks.DEEPSLATE_COAL_ORE,
                Blocks.DEEPSLATE_IRON_ORE,
                Blocks.DEEPSLATE_GOLD_ORE,
                Blocks.DEEPSLATE_REDSTONE_ORE,
                Blocks.DEEPSLATE_EMERALD_ORE,
                Blocks.DEEPSLATE_LAPIS_ORE,
                Blocks.DEEPSLATE_DIAMOND_ORE
        );
        if(!ORE_BLOCKS.contains(block)) {
            Module xray = madebystarontopandfml.getInstance().getModuleManager().getModule("xray");
            cir.setReturnValue(!xray.isEnabled());
        }else{
            cir.setReturnValue(true);
        }
    }
}