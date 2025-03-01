package net.java.main.modules.render;

import net.java.main.event.events.Render3DEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.java.main.modules.misc.MidClick;
import net.java.main.modules.misc.MurderDetector;
import net.java.main.utils.RenderUtils;
import net.java.main.value.ListValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ESP extends Module {
    public ESP() {
        super("ESP", "draw player", Category.RENDER);
        this.addValues(mode);
    }
    public static ListValue mode = new ListValue("Mode", new String[]{"Box", "Glow"}, "Glow");
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        if (mode.getValue().equals("Box")) {
            for (Entity entity : mc.level.entitiesForRendering()) {
                if (entity == mc.player) continue;
                if ((entity == MurderDetector.murder1 || entity == MurderDetector.murder2) && madebystarontopandfml.getInstance().getModuleManager().getModule("murderdetector").isEnabled()){
                    RenderUtils.renderBoundingBox(event.getPoseStack(), entity.getBoundingBox(), 0.1F, 1F, 1F);
                    continue;
                }
                if (KillAura.isEnemy(entity)) {
                    if (entity instanceof Player player){
                        if (MidClick.isFriend(player)) {
                            RenderUtils.renderBoundingBox(event.getPoseStack(), entity.getBoundingBox(), 0.1F, 1F, 0.1F);
                            continue;
                        }
                }
                        RenderUtils.renderBoundingBox(event.getPoseStack(), entity.getBoundingBox(), 1F, 1F, 1F);
                    }
                }
            }
        }
    }
