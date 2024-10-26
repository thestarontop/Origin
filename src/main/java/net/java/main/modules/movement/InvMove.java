package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.ScreenEvent;
import net.java.main.modules.Module;

public class InvMove extends Module {
    public InvMove(){super("InvMove","bzd",Category.MOVEMENT);}
    private void updateKeyState() {
            mc.options.keyUp.setDown(mc.options.keyUp.isDown());
            mc.options.keyDown.setDown(mc.options.keyDown.isDown());
            mc.options.keyRight.setDown(mc.options.keyRight.isDown());
            mc.options.keyLeft.setDown(mc.options.keyLeft.isDown());
            mc.options.keyJump.setDown(mc.options.keyJump.isDown());
            mc.options.keySprint.setDown(mc.options.keySprint.isDown());
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        updateKeyState();
    }

    @EventTarget
    public void onScreen(ScreenEvent event) {
        updateKeyState();
    }
}
