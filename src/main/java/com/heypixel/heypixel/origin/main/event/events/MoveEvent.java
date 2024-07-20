package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;

public class MoveEvent implements Event {
    public boolean isCancelled = false;
    private double x;
    private double y;
    private double z;
    public MoveEvent(double x,double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void cancelEvent() {
        isCancelled = true;
    }
}
