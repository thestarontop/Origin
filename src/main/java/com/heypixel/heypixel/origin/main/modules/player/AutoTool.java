package com.heypixel.heypixel.origin.main.modules.player;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.ClickBlockEvent;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

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
