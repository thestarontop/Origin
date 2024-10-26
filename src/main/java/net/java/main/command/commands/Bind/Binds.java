package net.java.main.command.commands.Bind;

import net.java.main.command.ChatManager;
import net.java.main.command.Command;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;

public class Binds extends Command {
    public Binds() {
        super("Binds", ".Binds");
    }
    public void Command(String[] command) {
        // try catch is used for if the player sends a command that is shorter then what we check. ie if they sent ".bind t" we'd run command[2] and crash.
        try {
            for (Module m : ModuleManager.modules) {
                if (m.getBindKeyCode() != -1 && m.getBindKeyCode() != -189321754) {
                    ChatManager.sendChat("§b "+m.getName()+" §l "+BindManager.getKeyName(m.getBindKeyCode()) );
                }
            }
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + " Use it like " + this.getUsage());
        }
    }
}
