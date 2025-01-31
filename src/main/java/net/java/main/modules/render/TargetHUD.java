package net.java.main.modules.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class TargetHUD extends Module {
    public TargetHUD(){super("TargetHUD","bzd",Category.CLIENT);}
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/icons.png");

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event){
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        if(KillAura.target instanceof LivingEntity target) {
            renderTargetHUD(event.getMatrixStack(), target);
        }

    }
    // 绘制目标信息（名字和血量）
    private void renderTargetHUD(PoseStack matrixStack, LivingEntity target) {
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 + 30;
        int y = screenHeight / 2 - 20;

        // 设置渲染状态
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // 绘制背景
        GuiComponent.fill(matrixStack, x, y, x + 120, y + 40, 0x90000000);

        // 设置文本渲染着色器
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        // 绘制目标名称
        mc.font.draw(matrixStack, target.getDisplayName(),
                x + 2, y + 2, 0xFFFFFF);

        // 绘制血量条
        float healthPercentage = target.getHealth() / target.getMaxHealth();
        int healthBarWidth = 116;
        int healthWidth = (int)(healthPercentage * healthBarWidth);

        // 血量条背景
        GuiComponent.fill(matrixStack, x + 2, y + 14, x + healthBarWidth + 2, y + 24, 0x80000000);
        // 血量条
        GuiComponent.fill(matrixStack, x + 2, y + 14, x + healthWidth + 2, y + 24, 0xFFFF0000);

        // 显示具体血量数值
        String healthText = String.format("%.1f/%.1f", target.getHealth(), target.getMaxHealth());
        mc.font.draw(matrixStack, healthText,
                x + 4, y + 15, 0xFFFFFF);

        // 绘制护甲值
        int armor = target.getArmorValue();
        if (armor > 0) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);

            for (int i = 0; i < armor / 2; i++) {
                GuiComponent.blit(matrixStack, x + 2 + (i * 8), y + 28, 0, 34, 9, 9, 9, 256, 256);
            }
            if (armor % 2 != 0) {
                GuiComponent.blit(matrixStack, x + 2 + ((armor / 2) * 8), y + 28, 0, 25, 9, 9, 9, 256, 256);
            }
        }

        // 恢复渲染状态
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
