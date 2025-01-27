package net.java.main;

import net.java.main.event.EventManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.ClientStartEvent;
import net.java.main.file.FileManager;
import net.java.main.gui.ClickGUi;
import net.java.main.gui.NewClickGui;
import net.java.main.command.commands.Bind.BindManager;
import net.java.main.command.ChatManager;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.client.HUD;
import net.java.main.utils.BasicUtils;
import net.java.main.utils.MinecraftInstance;
import net.java.main.utils.RotationUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod("madebystarontopandfml")
public class madebystarontopandfml extends MinecraftInstance {
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String NAME = "Origin_Client";
    private static madebystarontopandfml instance;
    public static final String VERSION = "b27";
    public static ModuleManager moduleManager;
    public static ChatManager chatManager;
    public static BindManager bindManager;
    public static EventManager eventManager;
    public static RotationUtils rotationutils;
    public static Commands commands;
    public static BasicUtils bu;
    private final FileManager fileManager;
    public ClickGUi clickGUi;
    public boolean isStarting;
    public madebystarontopandfml() {
        instance = this;
        isStarting = true;
        clickGUi = new ClickGUi();
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        chatManager = new ChatManager();
        bindManager = new BindManager();
        rotationutils = new RotationUtils();
        commands = new Commands();
        bu = new BasicUtils();
        fileManager = new FileManager();
        fileManager.load();
        fileManager.loadConfig(FileManager.modulesConfig);
        isStarting = false;

    }
    public void closeClient() {
        fileManager.saveAllConfigs();
        FileManager.saveConfig(FileManager.modulesConfig);
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
