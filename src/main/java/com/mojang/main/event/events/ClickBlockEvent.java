package com.mojang.main.event.events;

import com.mojang.main.event.impl.Event;
import net.minecraft.core.BlockPos;

public class ClickBlockEvent implements Event {
    private BlockPos blockpos;
    public ClickBlockEvent(BlockPos blockpos){
        this.blockpos = blockpos;
    }

    public BlockPos getBlockpos() {
        return blockpos;
    }
}
