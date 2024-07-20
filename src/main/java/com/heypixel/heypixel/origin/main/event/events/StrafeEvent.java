package com.heypixel.heypixel.origin.main.event.events;

import com.heypixel.heypixel.origin.main.event.impl.Event;
import com.mojang.math.Vector3d;
import net.minecraft.world.phys.Vec3;

public class StrafeEvent implements Event {
    private float yaw;
    private Vec3 movementinput;
    private float speed;
    private Vec3 velocity;
    public StrafeEvent(Vec3 movementinput ,float speed, float yaw,Vec3 velocity){
        this.yaw = yaw;
        this.movementinput = movementinput;
        this.speed = speed;
        this.velocity = velocity;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vec3 getMovementinput() {
        return movementinput;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public void setMovementinput(Vec3 movementinput) {
        this.movementinput = movementinput;
    }

    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }
}
