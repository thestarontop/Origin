package com.mojang.main.gui;

public class ClickUtils {
    public static boolean isInRect(float mouseX,float mouseY,float rectX,float rectY,float rectX2,float rectY2) {
        if (mouseX > rectX && mouseX < rectX2) {
            if (mouseY > rectY && mouseY < rectY2) {
                return true;
            }
        }
        return false;
    }
    public static boolean isInLine(float value,float min,float max) {
        if (value > min && value < max) {
            return true;
        }
        return false;
    }
}
