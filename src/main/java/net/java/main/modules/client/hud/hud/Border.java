package net.java.main.modules.client.hud.hud;


import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.utils.RenderUtils;

import java.awt.*;

public class Border {
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    private float x;
    private float y;
    private float x2;
    private float y2;

    public Border(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void draw(PoseStack poseStack) {
        RenderUtils.drawRect(poseStack,x, y, x2, y2, new Color(255,255,255,50).getRGB());
    }
}