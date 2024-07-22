package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public class ClickBlockEvent implements Event {
    private BlockPos blockpos;
    public ClickBlockEvent(BlockPos blockpos){
        this.blockpos = blockpos;
    }

    public BlockPos getBlockpos() {
        return blockpos;
    }
}
