package com.mojang.main;

import com.mojang.main.event.EventManager;
import com.mojang.main.gui.NewClickGui;
import com.mojang.main.command.commands.Bind.BindManager;
import com.mojang.main.command.ChatManager;
import com.mojang.main.modules.ModuleManager;
import com.mojang.main.utils.MinecraftInstance;
import com.mojang.main.utils.RotationUtils;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("madebystarontopandfml")
public class madebystarontopandfml extends MinecraftInstance{

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME = "Origin_Client";
    private static madebystarontopandfml instance;
    public static final String VERSION = "R1.0";
    public static ModuleManager moduleManager;
    public static ChatManager chatManager;
    public static BindManager bindManager;
    public static EventManager eventManager;
    public static RotationUtils rotationutils;
    public static Commands commands;
    public static NewClickGui gui;

    public madebystarontopandfml() {
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


    public static madebystarontopandfml getInstance(){
        return instance;
    }
    public EventManager getEventManager(){
        return eventManager;
    }

    public  ModuleManager getModuleManager() {
        return moduleManager;
    }
}
