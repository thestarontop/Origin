package net.java.main.event.events;

import net.java.main.event.impl.Event;

public class MotionEvent implements Event {

    private boolean isPre;
    private boolean isPost;
    private boolean onGround;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    public MotionEvent(double x,double y,double z,float yaw,float pitch,boolean isPre ,boolean isPost,boolean onGround) {
    this.isPre = isPre;
    this.isPost = isPost;
    this.onGround = onGround;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
    }

    public boolean getPre(){
        return isPre;
    }
    public boolean getPost(){
        return isPost;
    }
    public boolean getGround(){
        return onGround;
    }



    public float getYaw() {
        return yaw;
    }
    public float getPitch() {
        return pitch;
    }
    public double getX(){
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
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
}
