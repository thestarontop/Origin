package com.mojang.main.command.commands.Bind;


import com.mojang.main.command.Command;
import com.mojang.main.command.ChatManager;
import com.mojang.main.modules.ModuleManager;
import net.minecraft.ChatFormatting;
import com.mojang.main.modules.Module;

public class Bind extends Command {

    public Bind() {
        super("Bind", ".Bind (module) (key)");
    }

    //if the command was .bind T T
    // command basically sees this (.bind) (T) (T) all the spaces split the command up into pieces so .bind is command[0]
    public void Command(String[] command) {
        // try catch is used for if the player sends a command that is shorter then what we check. ie if they sent ".bind t" we'd run command[2] and crash.
        try {
            for (Module m : ModuleManager.modules) {
                if (m.getName().equalsIgnoreCase(command[1])) {
                    m.setBindKeyCode(BindManager.getKeyInt(command[2]));
                    //debug stuff ChatManager.sendChat(String.valueOf(BindManager.getKeyInt(command[2])));
                    ChatManager.sendHotBarChat(ChatFormatting.DARK_PURPLE + command[1] + " Was Just Binded to " + command[2]);
                    break;
                }
            }
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }





}
