package net.java.main.modules.client.hud.hud;


import com.mojang.blaze3d.vertex.PoseStack;
import org.lwjgl.glfw.GLFW;
import net.java.main.modules.Module;

public abstract class Element extends Module {

    public int renderX = 0,renderY = 0;

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float scale = 1;
    final String name;
    public boolean dragging = false;

    public Border border = null;

    protected Element(String name) {
        super(name,"hud", Category.CLIENT, GLFW.GLFW_KEY_UNKNOWN);
        this.name = name;
    }

    public abstract Border draw(PoseStack poseStack);

    public abstract void update();

}
