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
    public static int currentslot;

    @EventTarget
    public void onClickBlock(ClickBlockEvent event){
        var bestspeed = 0.0;
        int bestSlot = -1;
        BlockState block;
            block = mc.level.getBlockState(event.getBlockpos());
            for (int i = 0; i <= 8; i++) {
                ItemStack item = mc.player.inventoryMenu.getSlot(i + 36).getItem();
                var speed = item.getDestroySpeed(block);
                if (speed > bestspeed) {
                    bestspeed = speed;
                    bestSlot = i;
                }
                if (bestSlot != -1 && i != mc.player.getInventory().selected) {
                    mc.getConnection().send(new ServerboundSetCarriedItemPacket(i));
                }
            }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundSetCarriedItemPacket packet){
            if (!event.isCancelled){
                currentslot = packet.getSlot();
            }
        }
        if (event.getPacket() instanceof ClientboundSetCarriedItemPacket packet){
            currentslot = packet.getSlot();
        }
    }
    @Override
    public void onEnable(){
        Origin.getInstance().getEventManager().register(this);
        currentslot = mc.player.getInventory().selected;
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "AutoTool Was Enabled");
    }
}
