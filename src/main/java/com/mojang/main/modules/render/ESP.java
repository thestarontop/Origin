package com.mojang.main.modules.render;

import com.mojang.main.event.events.Render3DEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.main.modules.misc.MidClick;
import com.mojang.main.utils.RenderUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class ESP extends Module {
    public ESP() {
        super("ESP", "draw player", Category.RENDER);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity.getId() != mc.player.getId() && entity instanceof Player) {
                if (MidClick.isFriend((Player) entity)) {
                    RenderUtils.renderBoundingBox(event.getPoseStack(), entity, 0.1F, 1F, 0.1F);
                }else{
                    RenderUtils.renderBoundingBox(event.getPoseStack(), entity, 1F, 1F, 1F);
                }
            }
        }
    }
}
