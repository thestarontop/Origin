package com.heypixel.heypixel.origin.main;

import com.heypixel.heypixel.origin.main.event.EventManager;
import com.heypixel.heypixel.origin.main.gui.NewClickGui;
import com.heypixel.heypixel.origin.main.Commonds.Bind.BindManager;
import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.heypixel.heypixel.origin.main.utils.MinecraftInstance;
import com.heypixel.heypixel.origin.main.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.spongepowered.asm.mixin.Mixin;

@Mod("origin")
public class Origin extends MinecraftInstance{

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME = "Origin_Client";
    private static Origin instance;
    public static final String VERSION = "R1.0";
    public static ModuleManager moduleManager;
    public static ChatManager chatManager;
    public static BindManager bindManager;
    public static EventManager eventManager;
    public static RotationUtils rotationutils;
    public static Commands commands;
    public static NewClickGui gui;

    public Origin() {
        instance = this;
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        chatManager = new ChatManager();
        bindManager = new BindManager();
        rotationutils = new RotationUtils();
        commands = new Commands();
        gui = new NewClickGui();
        moduleManager.getModule("clickgui").setBindKeyCode(BindManager.getKeyInt("rshift"));
    }


    public static Origin getInstance(){
        return instance;
    }
    public EventManager getEventManager(){
        return eventManager;
    }

    public  ModuleManager getModuleManager() {
        return moduleManager;
    }
}
