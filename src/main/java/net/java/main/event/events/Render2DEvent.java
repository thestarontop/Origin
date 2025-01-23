package net.java.main.event.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.impl.Event;

public class Render2DEvent implements Event {
    float tickcounter;
    public Render2DEvent(float tickcounter){
        this.tickcounter = tickcounter;
    }
    public float getTickcounter() {
        return tickcounter;
    }
}
