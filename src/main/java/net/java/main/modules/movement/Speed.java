package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MovementUtils;
import net.java.main.value.ListValue;
import net.minecraft.world.phys.Vec3;

public class Speed extends Module{
    public Speed(){super("Speed","123", Module.Category.MOVEMENT);
        addValues(mode);
    }
    private boolean toDisable = false;
    private int offGroundTicks = 0;
    public static ListValue mode = new ListValue("type", new String[]{"Hypixel"},"Hypixel");

    @EventTarget
    public void onMotion(MotionEvent event) {
        if (mc.player == null) return;

        if (event.getPost()){
            if (mc.player.isOnGround()) {
                offGroundTicks = 0;
            } else {
                offGroundTicks++;
            }
        }

        if (!MovementUtils.isMoving()) return;

        if (offGroundTicks == 0) {
            if (toDisable) {
                return;
            } else if (!MovementUtils.jumpDown()) {
                MovementUtils.strafe((float) (MovementUtils.getAllowedHorizontalDistance() - Math.random() / 100f));
                mc.player.jumpFromGround();
            }
        } else if (MovementUtils.getJumpEffect() != 0) {
            return;
        }

        Vec3 motion = mc.player.getDeltaMovement();

        switch (offGroundTicks) {
            case 1:
                mc.player.setDeltaMovement(motion.x,0.39,motion.z);
                break;
            case 3:
                mc.player.setDeltaMovement(motion.x,motion.y-0.13,motion.z);
                break;
            case 4:
                mc.player.setDeltaMovement(motion.x,motion.y-0.2,motion.z);
                break;
        }
    }
    @Override
    public void onEnable() {
        offGroundTicks = 999;
        toDisable = false;
    }
    public void onDisable() {
        toDisable = true;
    }
}
