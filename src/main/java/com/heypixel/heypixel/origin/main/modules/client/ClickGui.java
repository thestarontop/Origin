package com.heypixel.heypixel.origin.main.modules.client;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.KeyPressEvent;
import com.heypixel.heypixel.origin.main.gui.NewClickGui;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.modules.Module;
import org.lwjgl.glfw.GLFW;

public class ClickGui extends Module {

    public ClickGui() {
        super("ClickGui","omg", Module.Category.CLIENT);
    }

    public static boolean guiEnabled = false;

    public void onEnable() {
        //Phase.LOGGER.info("Enable Working");
        mc.setScreen(new NewClickGui());
        guiEnabled = true;
        Origin.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void keyboardListener(KeyPressEvent event) {

        Origin.LOGGER.info(event.getKey());
        //below action 1 is when the key is pressed down action 2 should be when key is released and if no action is used it will spam
        // Phase.LOGGER.info(event.getKey() + " Was Just Pressed" + event.getScanCode());
            // keybinding debug stuff ChatManager.sendChat("key just pressed " + event.getKey());
                if (event.getKey() == GLFW.GLFW_KEY_ESCAPE) {
                    this.disable();
                    guiEnabled = false;
            }

    }

    public void onDisable() {
        guiEnabled = false;
        Origin.getInstance().getEventManager().unregister(this);
    }
}
