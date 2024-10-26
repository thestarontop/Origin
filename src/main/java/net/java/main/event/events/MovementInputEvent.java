package net.java.main.event.events;


import net.java.main.event.impl.Event;

public class MovementInputEvent implements Event {
    private float forward;
    private float strafe;
    public MovementInputEvent(float forward,float strafe){
        this.forward = forward;
        this.strafe = strafe;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getForward() {
        return forward;
    }
    public void setForward(float forward){
        this.forward = forward;
    }
    public void setStrafe(float strafe){
        this.strafe = strafe;
    }

}
