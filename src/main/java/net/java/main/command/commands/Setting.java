package net.java.main.command.commands;

import net.java.main.command.ChatManager;
import net.java.main.command.Command;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.ModuleManager;
import net.java.main.value.Value;
import net.minecraft.client.Minecraft;

public class Setting extends Command {
    public Setting() {
        super("Setting", ".module setting value");
    }
    public void Command(String[] command) {
        try {
            for(Value value : madebystarontopandfml.getInstance().getModuleManager().getModule(command[0]).getValues()){
                if (value.getName().equalsIgnoreCase(command[1])){
                    value.setValue(command[2]);
                }
            }
            ChatManager.sendChat(command[2] + " just set to" + command[3]);
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }
}
