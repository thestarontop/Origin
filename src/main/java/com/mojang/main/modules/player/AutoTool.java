package com.mojang.main.modules.player;

import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.ClickBlockEvent;
import com.mojang.main.event.events.Render2DEvent;
import com.mojang.main.modules.Module;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AutoTool extends Module {
    public AutoTool(){super("AutoTool","bzd",Category.PLAYER);}
    private int previtem = 0;
    private boolean mining = false;
    private int bestSlot = 0;
    private int tickDelay = 0;
    @EventTarget
    public void onClickBlock(ClickBlockEvent event){
        if (tickDelay < 3) return;
        switchSlot(event.getBlockpos());
    }
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (mc.mouseHandler.isLeftPressed()  && mc.hitResult.getType() == BlockHitResult.Type.BLOCK) {
            tickDelay++;
        } else tickDelay = 0;
        if (!mining && mc.mouseHandler.isLeftPressed()) {
            previtem = mc.player.getInventory().selected;
            mining = true;
        }
        if (mining && !mc.mouseHandler.isLeftPressed()) {
            mc.player.getInventory().selected = previtem;
            mining = false;
        }
    }
    public void switchSlot(BlockPos blockPos) {
        float bestSpeed = 1;

        BlockState block = mc.level.getBlockState(blockPos);

        for (int i =0;i<=8;i++) {
            ItemStack item = mc.player.getInventory().getItem(i);
            float speed = item.getDestroySpeed(block);

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }

            if (bestSlot != -1) {
                mc.player.getInventory().selected = bestSlot;
            }
        }

    }

}
