package com.mojang.main.command.commands.Bind;

import com.mojang.main.command.ChatManager;
import com.mojang.main.command.Command;
import com.mojang.main.modules.Module;
import com.mojang.main.modules.ModuleManager;

public class Binds extends Command {
    public Binds() {
        super("Binds", ".Binds");
    }
    public void Command(String[] command) {
        // try catch is used for if the player sends a command that is shorter then what we check. ie if they sent ".bind t" we'd run command[2] and crash.
        try {
            for (Module m : ModuleManager.modules) {
                if (m.getBindKeyCode() != -1) {
                    ChatManager.sendChat("§b "+m.getName()+" §l "+BindManager.getKeyName(m.getBindKeyCode()) );
                }
            }
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());
        }
    }
}
