package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;

public class Render3DEvent implements Event {
    Matrix4f matrix4f;
    Camera camara;
    float tickcounter;
    public Render3DEvent(Matrix4f matrix4f, Camera camera, float tickcounter){
        this.matrix4f = matrix4f;
        this.camara = camera;
        this.tickcounter = tickcounter;
    }

    public Camera getCamara() {
        return camara;
    }

    public float getTickcounter() {
        return tickcounter;
    }

    public Matrix4f getMatrix4f() {
        return matrix4f;
    }

    public void setCamara(Camera camara) {
        this.camara = camara;
    }

    public void setPosestack(Matrix4f posestack) {
        this.matrix4f = matrix4f;
    }

    public void setTickcounter(float tickcounter) {
        this.tickcounter = tickcounter;
    }
}
