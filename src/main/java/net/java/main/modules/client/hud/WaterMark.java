package net.java.main.modules.client.hud;


import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.client.hud.hud.Border;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.RenderUtils;

import java.awt.*;

public class WaterMark extends Element {
    public WaterMark() {
        super("WaterMark");
    }

    @Override
    public Border draw(PoseStack poseStack) {
        poseStack.pushPose();

        RenderUtils.drawRect(poseStack,2f,2f,mc.font.width("Origin") + 6f,mc.font.lineHeight + 6f, new Color(0,0,0,100).getRGB());
        RenderUtils.drawRect(poseStack,2f,2f,mc.font.width("Origin") + 6f,3f, new Color(255,255,255,255).getRGB());

        mc.font.drawShadow(poseStack,madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION,4,5, ColorUtils.skyRainbow(1,1f,1f).getRGB());
        //TextLayoutEngine.getInstance().getTextRenderer().drawText("Liquid Bounce",4,5, ColorUtils.skyRainbow(1,1f,1f).getRGB(),true,);

        poseStack.popPose();

        return new Border(0,0,mc.font.width(madebystarontopandfml.NAME) + 6f,mc.font.lineHeight + 6f);
    }

    @Override
    public void update() {

    }
}
