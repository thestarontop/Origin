package net.java.main.modules.render;

import net.java.main.event.events.Render3DEvent;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import net.java.main.modules.misc.MidClick;
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
                if (entity.getId() != mc.player.getId() && entity instanceof Player) {
                    if (MidClick.isFriend((Player) entity)) {
                        RenderUtils.renderBoundingBox(event.getPoseStack(), entity.getBoundingBox(), 0.1F, 1F, 0.1F);
                    } else {
                        RenderUtils.renderBoundingBox(event.getPoseStack(), entity.getBoundingBox(), 1F, 1F, 1F);
                    }
                }
            }
        }
    }
}
