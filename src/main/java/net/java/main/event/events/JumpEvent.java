package net.java.main.event.events;

import net.java.main.event.impl.Event;

public class JumpEvent implements Event {
    private float yaw;
    public JumpEvent(float yaw){
        this.yaw = yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }
}
