package net.java.main.modules.render;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render3DEvent;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;


public class ChestESP extends Module {
    public ChestESP(){super("ChestESP","bzd",Category.RENDER);}
    @EventTarget
    public void onRender3D(Render3DEvent event) {

    }
}
