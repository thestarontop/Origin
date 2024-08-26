package com.mojang.main.utils;

public class MSTimer {
    long time = -1L;

    public boolean hasTimePassed(long MS) {
        return System.currentTimeMillis() >= time + MS;
    }

    public long hasTimeLeft(long MS){
        return MS + time - System.currentTimeMillis();
    }

    public long timePassed() {
        return System.currentTimeMillis() - time;
    }

    long reachedTime = System.currentTimeMillis() - time;

    public void zero() {
        time = -1L;
    }

    public void reset() {
        time = System.currentTimeMillis();
    }
}