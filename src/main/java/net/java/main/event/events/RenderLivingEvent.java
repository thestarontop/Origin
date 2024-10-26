package net.java.main.event.events;

import net.java.main.event.impl.Event;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;

public class RenderLivingEvent implements Event {
        private float partialticks;
        private PoseStack posestack;
        private MultiBufferSource multibuffersource;
        private Entity entity;
        public RenderLivingEvent(Entity entity,PoseStack posestack,MultiBufferSource multibuffersource,float partialticks){
                this.partialticks = partialticks;
                this.entity = entity;
                this.posestack = posestack;
                this.multibuffersource = multibuffersource;
        }

        public PoseStack getPosestack() {
                return posestack;
        }

        public Entity getEntity() {
                return entity;
        }

        public float getPartialticks() {
                return partialticks;
        }

        public MultiBufferSource getMultibuffersource() {
                return multibuffersource;
        }
}
