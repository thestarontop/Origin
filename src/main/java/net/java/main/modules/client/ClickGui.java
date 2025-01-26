package net.java.main.modules.client;


import net.java.main.madebystarontopandfml;
import org.lwjgl.glfw.GLFW;
import net.java.main.modules.Module;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", "bzd",Category.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        setEnable(false);
        mc.setScreen(madebystarontopandfml.getInstance().clickGUi);

    }

}
