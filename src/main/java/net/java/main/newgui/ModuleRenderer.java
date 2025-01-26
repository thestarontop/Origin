package net.java.main.newgui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.Module;
import net.java.main.newgui.components.BooleanValueComponent;
import net.java.main.newgui.components.ChoiceValueComponent;
import net.java.main.utils.ColorUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.java.main.value.Value;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.java.main.utils.MinecraftInstance.mc;

public class ModuleRenderer {
    public Module module;
    public Frame parent;
    public int offset;
    public List<Component> components;
    public boolean extended;
    private float openProgress;
    private long lastToggleTime;

    public ModuleRenderer(Module module, Frame parent, int offset) {
        this.module = module;
        this.parent = parent;
        this.offset = offset;
        this.extended = false;
        this.openProgress = 0.0F;
        this.lastToggleTime = 0L;
        this.components = new ArrayList<>();
        int valueOffset = 20;

        for (Value<?> value : module.getValues()) {
            if (value instanceof BooleanValue booleanValue) {
                this.components.add(new BooleanValueComponent(booleanValue, this, valueOffset));
            } else if (value instanceof ListValue choiceValue) {
                this.components.add(new ChoiceValueComponent(choiceValue, this, valueOffset));
            }

            valueOffset += 20;
        }
    }

    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        this.updateAnimation();
        ClickGUIScreen.drawRoundedRect(
                stack, x, y, width, height, 0, ColorUtils.color(0, 0, 0, this.isHovered(mouseX, mouseY, x, y, width, height) ? 200 : 160)
        );
        int textOffset = 10 - 9 / 2;
        String moduleName = this.module.getName();
        float scaleFactor = 1.0F;
        int moduleNameWidth = mc.font.width(moduleName);
        if (moduleNameWidth > width - 30) {
            scaleFactor = (float) (width - 30) / (float) moduleNameWidth;
        }

        stack.pushPose();
        stack.translate(x + textOffset, y + textOffset, 0.0);
        stack.scale(scaleFactor, scaleFactor, 1.0F);
        mc.font.drawShadow(stack, moduleName, 0.0F, 0.0F, this.module.isEnabled() ? Color.GREEN.getRGB() : -1);
        stack.popPose();
        if (!this.components.isEmpty()) {
            mc.font.drawShadow(stack, this.extended ? "-" : "+", (float) (x + width - 14), (float) (y + textOffset), -1);
        }

        if (this.openProgress > 0.0F) {
            int componentY = y + 20;

            for (Component component : this.components) {
                if (componentY + 20 > y + height) {
                    break;
                }

                component.render(stack, mouseX, mouseY, delta, x, componentY, width, 20);
                componentY += 20;
            }
        }
    }

    private void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        float targetProgress = this.extended ? 1.0F : 0.0F;
        if (this.openProgress != targetProgress) {
            float deltaTime = (float) (currentTime - this.lastToggleTime) / 1000.0F;
            this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
        }
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY, this.parent.x, this.parent.y+ this.offset, this.parent.width, 20)) {
            if (mouseButton == 0) {
                this.module.setEnable(!this.module.isEnabled());
            } else if (mouseButton == 1 && !this.components.isEmpty()) {
                this.extended = !this.extended;
                this.lastToggleTime = System.currentTimeMillis();
                this.parent.updateButtons();
            }
        }

        if (this.extended) {
            for (Component component : this.components) {
                component.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.extended) {
            for (Component component : this.components) {
                component.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX > (double) x && mouseX < (double) (x + width) && mouseY > (double) y && mouseY < (double) (y + height);
    }

    public int getHeight() {
        return 20 + (this.extended ? this.components.size() * 20 : 0);
    }
}
