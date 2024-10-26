package net.java.main.command.commands;

import net.java.main.command.ChatManager;
import net.java.main.command.Command;
import net.java.main.modules.misc.MidClick;

public class Friend extends Command {
    public Friend() {
        super("Friend", ".狐朋狗友 add/remove 玩家id");
    }
    public void Command(String[] command) {
        try {
            if (command[1].contains("add")) {
                if(!MidClick.friend.contains(command[2])){
                    MidClick.friend.add(command[2]);
                }
                ChatManager.sendChat("added "+command[2]);
            }else if(command[1].contains("remove")){
                if (MidClick.friend.contains(command[2])) {
                    MidClick.friend.remove(command[2]);
                }
                ChatManager.sendChat("removed "+command[2]);
            }
        } catch (Exception e) {
            ChatManager.sendChat("command Used Incorrectly!" + this.getUsage());

        }


    }
}
