package com.mojang.main.command.commands;


import com.mojang.main.command.ChatManager;
import com.mojang.main.command.Command;
import com.mojang.main.modules.ModuleManager;
import com.mojang.main.modules.Module;
public class Toggle extends Command {

    public Toggle() {
        super("Toggle", ".Toggle (module)");
    }
    public void Command(String[] command) {
        try {
            for (Module m : ModuleManager.modules) {
                if (m.getName().equalsIgnoreCase(command[1])) {
                    m.toggle();
                    ChatManager.sendChat(command[1] + " is enabled " + m.isEnabled());
                    break;
                }
            }


        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }

}
