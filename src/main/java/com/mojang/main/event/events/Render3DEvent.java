package com.mojang.main.event.events;

import com.mojang.main.event.impl.Event;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;

public class Render3DEvent implements Event {
    PoseStack poseStack;
    Camera camara;
    float tickcounter;
    public Render3DEvent(PoseStack poseStack, float tickcounter){
        this.poseStack = poseStack;
        this.tickcounter = tickcounter;
    }



    public float getTickcounter() {
        return tickcounter;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }


    public void setTickcounter(float tickcounter) {
        this.tickcounter = tickcounter;
    }
}
