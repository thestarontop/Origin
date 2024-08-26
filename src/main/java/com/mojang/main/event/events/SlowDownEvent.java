package com.mojang.main.event.events;

import com.mojang.main.event.impl.Event;

public class SlowDownEvent implements Event {
    private float movementforward;
    private float movementstrafe;
    public SlowDownEvent(float movementforward,float movementstrafe) {
        this.movementforward = movementforward;
        this.movementstrafe = movementstrafe;
    }
    public float getMovementForward(){
        return movementforward;
    }
    public float getMovementStrafe(){
        return movementstrafe;
    }
    public void setMovementForward(float movementforward){
        this.movementforward = movementforward;
    }
    public void setMovementStrafe(float movementstrafe){
        this.movementstrafe = movementstrafe;
    }
}
