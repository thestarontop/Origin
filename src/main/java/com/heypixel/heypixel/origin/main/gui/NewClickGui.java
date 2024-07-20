package com.heypixel.heypixel.origin.main.gui;


import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class NewClickGui extends Screen {

    float x = 50f;
    float y = 50f;
    public NewClickGui() {
        super(new TextComponent("menu.game"));
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int p_96563_, int p_96564_, float p_96565_) {
        float xOffset = this.x;
        int index = 1;
        for (Module.Category category : ModuleManager.categoryArrayList) {

            renderButton(poseStack,category,xOffset,this.y,index);
            index += 1;
            xOffset += 80f;
        }
    }
    @Override
    public boolean isMouseOver(double x, double y) {
        return super.isMouseOver(x, y);
    }
    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        float xOffset = this.x;
        for (Module.Category category : ModuleManager.categoryArrayList) {
            float currY = this.y + 20f;
            if (ClickUtils.isInLine((float) x,xOffset,xOffset + 70)) {
                for (Module module : ModuleManager.modules) {
                    if (module.getCategory() == category) {
                        if(mouseButton == 0) {
                            if (ClickUtils.isInRect((float) x, (float) y, xOffset + 3f, currY, xOffset + 67f, currY + 15f)) {
                                module.toggle();
                                break;
                            }

                        currY += 15f;
                        }
                    }
                }
            }
            xOffset += 80f;
        }
        return super.mouseClicked(x, y, mouseButton);
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    public void renderButton(PoseStack poseStack, Module.Category category, float x, float y, int index) {
        float currY = y + 20f;
        Screen.fill(poseStack, (int) x, (int) y, (int) (x + 70), (int) (y + 20), Color.white.getRGB());
        Screen.drawCenteredString(poseStack, Minecraft.getInstance().font, category.name(), (int) (x + 35f), (int) (y + 5),new Color(0x2ED5FF).getRGB());
        for (Module module : ModuleManager.modules) {
            if (module.getCategory() == category) {
                renderModule(poseStack,module,x + 3f,currY);
                currY += 15;
            }
        }
    }
    public void renderModule(PoseStack poseStack, Module module, float x, float y) {
        if (module.isEnabled()) {
            Screen.fill(poseStack, (int) x, (int) y, (int) (x + 64), (int) (y + 15), new Color(150,150,150).getRGB());
            Screen.drawCenteredString(poseStack, Minecraft.getInstance().font,module.getName(), (int) (x + 32), (int) (y + 3),new Color(230,230,230).getRGB());
        } else {
            Screen.fill(poseStack, (int) x, (int) y, (int) (x + 64), (int) (y + 15), new Color(50,50,50).getRGB());
            Screen.drawCenteredString(poseStack, Minecraft.getInstance().font,module.getName(), (int) (x + 32), (int) (y + 3),Color.white.getRGB());
        }
    }
}
