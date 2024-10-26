package net.java.main.event.events;

import net.java.main.event.impl.Event;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class RenderNamePlateEvent implements Event {
    private Entity entity;
    private MultiBufferSource bufferSource;
    private PoseStack posestack;
    public RenderNamePlateEvent(Entity entity,PoseStack posestack, MultiBufferSource bufferSource){
        this.entity = entity;
        this.posestack = posestack;
        this.bufferSource = bufferSource;
    }

    public Entity getEntity() {
        return entity;
    }

    public MultiBufferSource getBufferSource() {
        return bufferSource;
    }

    public PoseStack getPosestack() {
        return posestack;
    }
}
