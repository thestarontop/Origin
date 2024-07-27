package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render3DEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.misc.MidClick;
import com.heypixel.heypixel.origin.main.utils.RenderUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.awt.*;

public class ESP extends Module {
    public ESP() {
        super("ESP", "draw player", Category.RENDER);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity.getId() != mc.player.getId() && entity instanceof Player) {
                RenderUtils.renderBoundingBox(event.getPoseStack(), entity, 1F, 1F, 1F);
            } else if (entity instanceof Player && MidClick.isFriend((Player) entity)) {
                RenderUtils.renderBoundingBox(event.getPoseStack(), entity, 0.1f, 1f, 0.1f);
            }
        }
    }
}
