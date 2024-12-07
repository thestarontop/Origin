package net.java.main.modules;

import net.java.main.file.FileManager;
import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.modules.client.ClickGui;
import net.java.main.modules.client.HUD;
import net.java.main.utils.MinecraftInstance;
import net.java.main.value.Value;
import net.minecraft.ChatFormatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Module extends MinecraftInstance {


    String name;
    String description;
    Category category;
    boolean isEnabled;

    int bindKeyCode;
    public boolean isKeyListening = false;
    private List<Value<?>> values = new ArrayList<>();
    public Module(String name, String description, Category category,int key) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.bindKeyCode = key;
    }
    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.bindKeyCode = GLFW.GLFW_KEY_UNKNOWN;
    }

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public void onEnable() {
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + this.getName() + " Was Enabled");
    }

    public void onDisable() {
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + this.getName() + " Was Disabled");
    }



        public void toggle() {
            /*if (this.getCategory() == Category.CLIENT) {
                if(!(this instanceof ClickGui) && !(this instanceof HUD)) {
                    if (this.isEnabled()) {
                        HUD.elements.remove((Element) this);
                    } else {
                        HUD.elements.add((Element) this);
                    }
                }
            }*/

            if (this.isEnabled) {
                setEnable(false);
            } else {
                setEnable(true);
            }
        }


    public boolean isEnabled() {
        return isEnabled;
    }
    public void setEnable(boolean enable) {
        this.isEnabled = enable;

        if (enable) {
            if (!madebystarontopandfml.getInstance().isStarting) {
                //Notification.addNotification(new Noti("✔", ChatFormatting.WHITE + this.getName() + ChatFormatting.WHITE + " is Enabled",30, NotiType.SUCCESS));
            }
            onEnable();
        } else {
            if (!madebystarontopandfml.getInstance().isStarting) {
                //Notification.addNotification(new Noti("✘", ChatFormatting.WHITE + this.getName() + ChatFormatting.WHITE + " is Disabled", 30, NotiType.ERROR));
            }
            onDisable();
        }
        FileManager.saveConfig(FileManager.modulesConfig);
    }

    public String getName() {
        return this.name;
    }

    public int getBindKeyCode() {return this.bindKeyCode;}

    public void setBindKeyCode(int bindKeyCode) {this.bindKeyCode = bindKeyCode;}

    public Category getCategory() {return this.category;}
    public String getDescription() {return this.description;}
    public int getKey() {
        return bindKeyCode;
    }
    public void setKey(int key) {
        this.bindKeyCode = key;
    }
    public List<Value<?>> getValues() {
        return values;
    }
    protected void addValues(Value<?>... values) {
        for (Value<?> value : values) {
            getValues().add(value);
        }
    }

    public static enum Category {
        COMBAT,
        CLIENT,
        MISC,
        RENDER,
        WORLD,
        MOVEMENT,
        EXPLOIT,
        PLAYER,
    }

}
