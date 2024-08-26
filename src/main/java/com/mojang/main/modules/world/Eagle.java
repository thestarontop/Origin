package com.mojang.main.modules.world;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class Eagle extends Module {

    public Eagle() {
        super("Eagle","idk", Module.Category.WORLD);
    }
    private boolean lastsneak = false;
    private int sneakCount = 0;


    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;


        boolean isAir = mc.level.getBlockState(new BlockPos(mc.player.position().x, mc.player.position().y - 1, mc.player.position().z)).getBlock() == Blocks.AIR;

        mc.options.keyShift.setDown(isAir);
        mc.options.keyUp.setDown(!isAir);
        mc.options.keyDown.setDown(isAir);


        boolean sneak = mc.options.keyShift.isDown();


        if (sneak != lastsneak) {
            if (sneak) {
                sneakCount++;
                if (sneakCount == 1) {
                    mc.player.setYRot(mc.player.getYRot() - 1F);
                } else if (sneakCount == 2) {
                    mc.player.setYRot(mc.player.getYRot() + 1F);
                    sneakCount = 0; 
                }
            }
            lastsneak = sneak;
            mc.player.setYRot(mc.player.getYRot() + 180F);
        }

    }
    @Override
    public void onEnable() {
        madebystarontopandfml.getInstance().getEventManager().register(this);
        lastsneak = true;
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Eagle Was Enabled");
    }
    @Override
    public void onDisable() {
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        mc.options.keyShift.setDown(true);
        mc.options.keyUp.setDown(false);
        mc.options.keyDown.setDown(false);
        ChatManager.sendHotBarChat(ChatFormatting.RED + "Eagle Was Disabled");
    }

}
