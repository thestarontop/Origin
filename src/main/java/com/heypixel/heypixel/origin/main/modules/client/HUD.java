package com.heypixel.heypixel.origin.main.modules.client;

import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.heypixel.heypixel.origin.main.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HUD extends Module {
    public HUD(){super("HUD","bzd",Category.CLIENT);}
    SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), "Origin", 4, 1, RenderUtils.getRainbowOpaque(0.9f));
        mc.font.drawShadow(new PoseStack(), "("+HOUR_FORMAT.format(new Date(System.currentTimeMillis()))+")",32 , 1.3f, -1);
        List<String> enabled = new ArrayList<>();
        for (Module module : ModuleManager.modules) {
            if (module.isEnabled()) {
                enabled.add(module.getName());
            }
        }
        enabled.sort((s1, s2) -> Integer.compare(mc.font.width(s2), mc.font.width(s1)));
        float y = 0;
        for (String str : enabled) {
            mc.font.drawShadow(new PoseStack(), str, 4, 14 + y, RenderUtils.getRainbowOpaque(0.5f));
            y += 10;
        }
    }
}
