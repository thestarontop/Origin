package com.mojang.main.command.commands;

import com.mojang.main.command.ChatManager;
import com.mojang.main.command.Command;

public class Commands extends Command {

    public Commands() {
        super("Commands", "How the fuck did u mess this up retard");
    }

    public void Command(String[] command) {
        try {
            StringBuilder message = new StringBuilder();
            for (Command e : ChatManager.commandList) {
                message.append(e.getName()).append(", ");
            }
            ChatManager.sendChat(message.toString());
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + this.getUsage());

        }

        }

}
