package net.java.main.modules.client;


import net.java.main.madebystarontopandfml;
import net.java.main.newgui.ClickGUIScreen;
import org.lwjgl.glfw.GLFW;
import net.java.main.modules.Module;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", "点击界面",Category.CLIENT, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    @Override
    public void onEnable() {
        setEnable(false);
    //    mc.setScreen(madebystarontopandfml.getInstance().gui);
        mc.setScreen(ClickGUIScreen.INSTANCE);

    }

}
