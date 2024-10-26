package net.java.main.modules.client.hud;


import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.client.hud.hud.Border;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.utils.RenderUtils;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;

public class Test1 extends Element {
    public Test1() {
        super("Test1");
    }

    @Override
    public Border draw(PoseStack poseStack) {

        RenderUtils.drawRoundedRect(poseStack.last().pose(), 0,0,100,100,5f,new Color(255,255,255,255).getRGB());
        return new Border(0,0,100,100);
    }

    @Override
    public void update() {

    }
}
