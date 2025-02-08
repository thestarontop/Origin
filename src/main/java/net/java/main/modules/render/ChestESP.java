package net.java.main.modules.render;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render3DEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.RenderUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;


public class ChestESP extends Module {
    public ChestESP(){super("ChestESP","bzd",Category.RENDER);}
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        var BlockMap = BlockUtils.searchBlocks2(20);
        BlockMap.forEach((key, value) -> {
            if(value == Blocks.CHEST) {
                RenderUtils.renderBoundingBox(event.getPoseStack(),key,1F,0.5F,0F);
            }
        });
    }
}
