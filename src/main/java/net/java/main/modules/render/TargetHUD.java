package net.java.main.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class TargetHUD extends Module {
    public TargetHUD(){super("TargetHUD","bzd",Category.CLIENT);}
    private static final ResourceLocation ICON = new ResourceLocation("minecraft", "textures/item/diamond.png");
    private static long startTime = System.currentTimeMillis();
    private static final int DISPLAY_TIME = 5000;  // 显示时间（毫秒）
    @EventTarget
    public void onRender(Render2DEvent event){
        render();
        if(KillAura.target instanceof LivingEntity target) {
            //render();
        }

    }
    // 绘制目标信息（名字和血量）
    public static void render() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > DISPLAY_TIME) return;

        // 计算透明度，模拟淡入效果
        float opacity = Math.min(1.0f, elapsed / 1000f);  // 淡入效果，1秒内渐变

        PoseStack poseStack = new PoseStack();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 设置弹窗大小
        int toastWidth = 160;
        int toastHeight = 32;
        int x = screenWidth / 2 - toastWidth / 2;
        int y = screenHeight / 4;

        // 设置渲染颜色，使用透明度
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);  // 设置透明度
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("minecraft", "textures/gui/toasts.png"));
        mc.gui.blit(poseStack, x, y, 0, 0, toastWidth, toastHeight);

        // 绘制图标
        RenderSystem.setShaderTexture(0, ICON);
        mc.gui.blit(poseStack, x + 6, y + 8, 0, 0, 16, 16);

        // 绘制标题和描述
        Font font = mc.font;
        font.draw(poseStack, new TextComponent("钻石之路"), x + 30, y + 5, 0xFFFFFF);
        font.draw(poseStack, new TextComponent("你获得了一颗钻石"), x + 30, y + 15, 0xAAAAAA);
    }
}
