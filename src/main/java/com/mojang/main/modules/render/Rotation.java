package com.mojang.main.modules.render;

import com.mojang.main.event.events.MotionEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.utils.RotationUtils;

public class Rotation extends Module {
    public Rotation(){super("Rotation","Render Rotations",Category.RENDER);}
    @EventTarget
    public void onMotion(MotionEvent event){
            mc.player.setYHeadRot(RotationUtils.serverRotation.getYaw());
            mc.player.setYBodyRot(mc.player.getYHeadRot());
    }
}
