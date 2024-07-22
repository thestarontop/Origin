package com.heypixel.heypixel.origin.main.modules.player;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.InputEvent;

public class AutoTool extends Module {
    public AutoTool(){super("AutoTool","bzd",Category.PLAYER);}

    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mc.player != null && ) {
            switchSlot();
        }
    }

    @Override
    public void onEnable(){
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "AutoTool Was Enabled");
    }
    public void onDisable() {
        Origin.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + "AutoTool Was Disabled");
    }
    public void switchSlot(BlockPos blockPos) {
        float bestSpeed = 1F;
        int bestSlot = -1;

        Block block = mc.level.getBlockState(blockPos).getBlock();
        for (int i = 0; i <= 8; i++) {
            ItemStack item = mc.player.inventoryMenu.getSlot(i).getItem();
            if (item == null) continue;
            float speed = item.getDestroySpeed(block.defaultBlockState());

            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        if (bestSlot != -1) {
            mc.player.getInventory().selected = bestSlot;
        }
    }
}
