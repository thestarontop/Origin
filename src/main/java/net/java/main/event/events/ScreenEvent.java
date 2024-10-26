package net.java.main.event.events;

import net.java.main.event.impl.Event;
import net.minecraft.client.gui.screens.Screen;

public class ScreenEvent implements Event {
    private Screen screen;
    public ScreenEvent(Screen screen){
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
