package net.java.main.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.java.main.command.ChatManager;
import net.java.main.madebystarontopandfml;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.lang.reflect.Field;

public class FileManager {
    public static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    static File dir = new File(System.getProperty("user.home"),"Origin-1.18");
    public static FileConfig modulesConfig = new ModulesConfig(new File(dir,"modules.json"));
    public static FileConfig moduleslog = new ModulesConfig(new File(dir,"logs.json"));
    public static FileConfig hudConfig = new HudConfig(new File(dir,"hud.json"));

    public void load() {
        dir.mkdir();


    }

    public static void saveConfig(FileConfig config) {
        saveConfig(config, false);
    }
    static void saveConfig(FileConfig config, boolean ignoreStarting) {
        if (!ignoreStarting && madebystarontopandfml.getInstance().isStarting) return;
        try {
            if (!config.hasConfig()) config.createConfig();
            config.saveConfig();
        } catch (Throwable t) {
            ChatManager.sendChat("§cSaveConfig Error: " + t);
        }
    }
    public void loadConfig(FileConfig config) {
        if (!config.hasConfig()) {
            saveConfig(config, true);
            return;
        }
        try {
            config.loadConfig();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public void saveAllConfigs() {
        // 获取所有字段
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getType() == FileConfig.class) {
                try {
                    if (!field.isAccessible()) field.setAccessible(true);
                    FileConfig fileConfig = (FileConfig) field.get(this);
                    saveConfig(fileConfig);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
