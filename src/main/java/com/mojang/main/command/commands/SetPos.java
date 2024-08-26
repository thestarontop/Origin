package com.mojang.main.command.commands;


import com.mojang.main.command.ChatManager;
import com.mojang.main.command.Command;
import net.minecraft.client.Minecraft;

public class SetPos extends Command {

    public SetPos() {
        super("SetPos", ".Setpos (x) (y) (z)");
    }

    public void Command(String[] command) {
        try {

            Minecraft.getInstance().player.setPos(Double.parseDouble(command[1]), Double.parseDouble(command[2]), Double.parseDouble(command[3]));
            ChatManager.sendChat("Woooosh! You teleported to " + command[1] + " " + command[2] + " " + command[3]);

        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }
}
