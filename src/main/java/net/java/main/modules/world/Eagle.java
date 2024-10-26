package net.java.main.modules.world;

import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class Eagle extends Module {

    public Eagle() {
        super("Eagle","idk", Module.Category.WORLD);
    }



    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;
        boolean isAir = mc.level.getBlockState(new BlockPos(mc.player.position().x, mc.player.position().y - 1, mc.player.position().z)).getBlock() == Blocks.AIR;
        mc.options.keyShift.setDown(isAir);
    }

}
