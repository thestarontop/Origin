package com.heypixel.heypixel.origin.main.Commonds;


import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.heypixel.heypixel.origin.main.modules.Module;
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
            ChatManager.sendChat("Command Used Incorrectly!" + " Use it like " + this.getUsage());

        }
    }

}
