package net.java.main.modules.client.hud;

import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.client.hud.hud.Border;
import net.java.main.modules.client.hud.hud.Element;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.utils.MinecraftInstance;
import net.minecraft.ChatFormatting;

import java.util.List;

public class ArrayList extends Element  {
    public ArrayList() {
        super("ArrayList");
    }

    private float currY = 0f;

    @Override
    public Border draw(PoseStack poseStack) {
        currY = 2f;
        List<String> enable = new java.util.ArrayList<>();
        for (Module module : ModuleManager.modules) {
            if (module.isEnabled() && module.getCategory() != Module.Category.CLIENT) {
                enable.add(module.getName());
            }
        }
        enable.sort((s1, s2) -> Integer.compare(mc.font.width(s2), mc.font.width(s1)));
        for (String s : enable) {
            mc.font.drawShadow(poseStack,s,mc.getWindow().getGuiScaledWidth() - mc.font.width(s) - 2f,currY,-1);
            currY += mc.font.lineHeight;
            currY += 1f;
        }
        return new Border(mc.getWindow().getGuiScaledWidth() - mc.font.width(enable.get(0)) - 4,0,mc.getWindow().getGuiScaledWidth(),currY);
    }

    @Override
    public void update() {

    }
}
