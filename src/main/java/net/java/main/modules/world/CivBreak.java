package net.java.main.modules.world;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.java.main.utils.RenderUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class CivBreak extends Module {
    public CivBreak(){super("CivBreak","bzd",Category.WORLD);}
    BlockPos blockpos = null;
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundPlayerActionPacket packet1 && packet1.getAction().equals(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK)){
            blockpos = packet1.getPos();
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (blockpos != null){
            Block block = mc.level.getBlockState(blockpos).getBlock();
            if (block == Blocks.AIR || block == Blocks.LIGHT || block == Blocks.WATER || block == Blocks.LAVA || block == Blocks.BUBBLE_COLUMN || block == Blocks.MOVING_PISTON || block == Blocks.FIRE)
                return;
            PacketUtils.sendPacketNoEvent(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK,blockpos, Direction.DOWN));
        }
    }
    @Override
    public void onEnable(){
        super.onEnable();
        blockpos = null;
    }
    @EventTarget
    public void onRender3D(Render3DEvent event){
        if (blockpos != null) {
            RenderUtils.renderBoundingBox(event.getPoseStack(), blockpos, 1F, 0F, 0F);
        }
    }
}
