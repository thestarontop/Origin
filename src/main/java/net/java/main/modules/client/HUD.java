package net.java.main.modules.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.gui.screens.ChatScreen;
import org.lwjgl.glfw.GLFW;
import net.java.main.modules.Module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HUD extends Module {

    public static ArrayList<Element> elements = new ArrayList<>();
    public static int realMouseX = 0, realMouseY = 0;
    static int xOffset = -1;
    static int yOffset = -1;
    SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    public HUD() {
        super("HUD","HUD", Category.CLIENT, GLFW.GLFW_KEY_UNKNOWN);
        setEnable(true);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION, 4, 1, RenderUtils.getRainbowOpaque(0.9f));
        mc.font.drawShadow(new PoseStack(), "("+HOUR_FORMAT.format(new Date(System.currentTimeMillis()))+")",92 , 1.3f, -1);
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
        /*for (Element element : elements) {
            if (element.dragging) {
                if (xOffset != -1 && yOffset != -1) {
                    element.renderX = realMouseX - xOffset;
                    element.renderY = realMouseY - yOffset;
                }
            }

            PoseStack poseStack = new PoseStack();
            poseStack.pushPose();

            poseStack.scale(element.getScale(), element.getScale(), element.getScale());
            poseStack.translate(element.renderX, element.renderY, 0);

            double mouseX1 = (realMouseX / element.getScale()) - element.renderX;
            double mouseY1 = (realMouseY / element.getScale()) - element.renderY;

            element.border = element.draw(poseStack);

            if (mouseX1 > element.border.getX() && mouseX1 < element.border.getX2() && mouseY1 > element.border.getY() && mouseY1 < element.border.getY2()) {
                if (mc.screen instanceof ChatScreen) {
                    element.border.draw(poseStack);
                }
            }

            poseStack.popPose();
        }*/
    }
    public static void handleMouseClick(double mouseX, double mouseY, int mouseButton) {
        if (!(mc.screen instanceof ChatScreen)) return;

        for (Element element : elements) {
            double mouseX1 = (mouseX / element.getScale()) - element.renderX;
            double mouseY1 = (mouseY / element.getScale()) - element.renderY;

            if (mouseX1 > element.border.getX() && mouseX1 < element.border.getX2() && mouseY1 > element.border.getY() && mouseY1 < element.border.getY2()) {
                element.dragging = true;
                xOffset = (int) (mouseX - element.renderX);
                yOffset = (int) (mouseY - element.renderY);
            }
        }
    }
    public static void handleMouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (!(mc.screen instanceof ChatScreen)) return;

        for (Element element : elements) {
            element.dragging = false;
        }
        xOffset = -1;
        yOffset = -1;
    }
    public static void initHud() {
        for (Module module : ModuleManager.modules) {
            if (module.getCategory() == Category.CLIENT) {
                if(module instanceof ClickGui || module instanceof HUD) break;
                if (module.isEnabled()) {
                    elements.add((Element) module);
                }
            }
        }
    }
}
