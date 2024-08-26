package com.mojang.main.modules.client;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.gui.NewClickGui;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.KeyPressEvent;
import com.mojang.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
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
        madebystarontopandfml.getInstance().getEventManager().register(this);
    }

    @EventTarget
    public void keyboardListener(KeyPressEvent event) {

        madebystarontopandfml.LOGGER.info(event.getKey());
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
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
    }
}
