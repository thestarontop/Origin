package com.heypixel.heypixel.origin.main.modules;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.utils.BlockUtils;
import com.heypixel.heypixel.origin.main.utils.MinecraftInstance;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class Module extends MinecraftInstance {


    String name;
    String description;
    Category category;
    boolean isEnabled;
    KeyboardHandler keyboardHandler;
    int bindKeyCode = -1;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        //isEnabled = false;

    }

    public Module(String name, Category category) {
        this.name = name;
        this.category = category;

    }

    public void onEnable() {
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + this.getName() + " Was Enabled");
    }

    public void onDisable() {
        Origin.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED + this.getName() + " Was Disabled");
    }

    public void enable() {
        this.isEnabled = true;
        this.onEnable();
    }

    public void disable() {
        this.isEnabled = false;
        this.onDisable();
    }

    public void toggle() {
        if (!this.isEnabled) this.enable();
        else this.disable();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getName() {
        return this.name;
    }

    public int getBindKeyCode() {return this.bindKeyCode;}

    public void setBindKeyCode(int bindKeyCode) {this.bindKeyCode = bindKeyCode;}

    public Category getCategory() {return this.category;}
    public String getDescription() {return this.description;}

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
