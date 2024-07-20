package com.heypixel.heypixel.origin.main.Commonds;


import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;

public class Toggle2 extends Command {

    public Toggle2() {
        super("t", ".t (module)");
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
            ChatManager.sendChat("Command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }

}
