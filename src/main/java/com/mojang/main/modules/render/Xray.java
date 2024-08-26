package com.mojang.main.modules.render;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.utils.BlockUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.Blocks;


import java.util.ArrayList;
import java.util.List;


public class Xray extends Module {
    public Xray(){super("Xray","render blocks",Category.RENDER);}
    public static final List<BlockPos> visitedPositions = new ArrayList<>();
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mc.level == null || mc.player == null) return;
        var BlockMap = BlockUtils.searchBlocks(4);
        BlockMap.forEach((key, value) -> {
            if(value != Blocks.AIR && value != Blocks.GRASS_BLOCK && value != Blocks.DIRT) {
                if(!visitedPositions.contains(key)) {
                    mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, key, Direction.DOWN));
                    visitedPositions.add(key);
                }
            }
        });
    }
    @Override
    public void onEnable() {
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + this.getName() + " Was Enabled");
        visitedPositions.clear();
    }
}
