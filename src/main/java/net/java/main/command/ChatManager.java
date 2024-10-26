package net.java.main.command;

import net.java.main.command.commands.Bind.*;
import net.java.main.command.commands.*;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;

public class ChatManager {

    public static ArrayList<Command> commandList;

    public ChatManager() {
        initCommands();
    }

    public static String commands = "";


    public static String prefix = ".";

    public static void sentCommand(String[] command) {
        String startcommand = command[0].toLowerCase();
        // below you'll switch the words below with what ever command your doing ik its chinese but i like it
        for (Module module: ModuleManager.modules) {
            if (startcommand.contains(module.getName())){
                Setting c = new Setting();
                c.Command(command);
                return;
            }
        }
        if (startcommand.contains("commands") || startcommand.contains("help") || startcommand.contains("?")) {
            Commands c = new Commands();
            c.Command(command);
            return;
        }
        if (startcommand.contains("setpos")) {
            SetPos c = new SetPos();
            c.Command(command);
            return;
        }
        if (startcommand.contains("toggle")) {
            Toggle c = new Toggle();
            c.Command(command);
            return;
        }
        if (startcommand.contains("t")) {
            Toggle2 c = new Toggle2();
            c.Command(command);
            return;
        }
        if (startcommand.contains("prefix")) {
            Prefix c = new Prefix();
            c.Command(command);
            return;
        }
        if (startcommand.contains("modules")) {
            Modules c = new Modules();
            c.Command(command);
            return;
        }
        if (startcommand.contains("binds")) {
            Binds c = new Binds();
            c.Command(command);
            return;
        }
        if (startcommand.contains("bind")) {
            Bind c = new Bind();
            c.Command(command);
            return;
        }
        if (startcommand.contains("狐朋狗友")  || startcommand.contains("friend")) {
            Friend c = new Friend();
            c.Command(command);
            return;
        }
    }

    public static void sendChat(String message) {
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().player.displayClientMessage(Component.nullToEmpty(message), false);
    }

    // uses the new minecraft chat above the players hotbar for messages
    public static void sendHotBarChat(String message) {
        if(Minecraft.getInstance().player != null){
            Minecraft.getInstance().player.displayClientMessage(Component.nullToEmpty(message), true);
        }
    }

    public void initCommands() {
        commandList = new ArrayList<Command>();
        commandList.add(new Prefix());
        commandList.add(new SetPos());
        commandList.add(new Toggle());
        commandList.add(new Modules());
        commandList.add(new Bind());
        commandList.add(new Binds());
        commandList.add(new Toggle2());
        commandList.add(new Friend());
    }

}
