package net.java.main.command.commands;

import net.java.main.command.ChatManager;
import net.java.main.command.Command;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.Module;
import net.minecraft.ChatFormatting;

public class Modules extends Command {

    public Modules() {
        super("Modules", "How the fuck did u mess this up retard");
    }

    public void Command(String[] command) {
        try {
        StringBuilder message = new StringBuilder();
        for (Module e : ModuleManager.modules) {
            if (e.isEnabled()) message.append(ChatFormatting.GREEN);
            else message.append(ChatFormatting.WHITE);
            message.append(e.getName()).append(", ");
            break;
        }
        ChatManager.sendChat(message.toString());

        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + this.getUsage());

        }


    }
}
