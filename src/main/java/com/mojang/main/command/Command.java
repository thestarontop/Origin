package com.mojang.main.command;

public class Command {

    String name;
    String usage;

    public Command(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public String getName() {
        return this.name;
    }

    public String getUsage() {return this.usage;}
}
