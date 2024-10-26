package net.java.main.modules.render;

import net.java.main.event.events.MotionEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.utils.RotationUtils;

public class Rotation extends Module {
    public Rotation(){super("Rotation","Render Rotations",Category.RENDER);}
    @EventTarget
    public void onMotion(MotionEvent event){
            mc.player.setYHeadRot(RotationUtils.serverRotation.getYaw());
            mc.player.setYBodyRot(mc.player.getYHeadRot());
    }
}
