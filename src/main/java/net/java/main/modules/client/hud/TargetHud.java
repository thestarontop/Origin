package net.java.main.modules.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.AttackEvent;
import net.java.main.event.events.MotionEvent;
import net.java.main.modules.client.hud.hud.Border;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.utils.AnimationUtils;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.RenderUtils;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.math.BigDecimal;

public class TargetHud extends Element {
    public TargetHud() {
        super("TargetHud");
    }

    Entity target = null;
    float lastHealth = 0;
    float renderLastHealth = 0;
    float animHealth = 0;
    AnimationUtils animationUtils = new AnimationUtils();
    int a = 0;

    @EventTarget
    public void onAttack(AttackEvent event) {
        target = event.getEntity();
        a = 0;
    }

    @EventTarget
    public void onUpdate(MotionEvent event) {
        if (!event.getPost()) return;
        a++;
        if (a >= 60) {
            a = 0;
            target = null;
        }
    }

    @Override
    public Border draw(PoseStack poseStack) {
        if (target == null && mc.screen instanceof ChatScreen) target = mc.player;

        if (target != null) {

            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            RenderUtils.drawRect(poseStack, (float) width / 2 - 5, (float) height / 2 - 105, (float) width / 2 - 5 + 110, (float) height / 2 - 105 + 36,new Color(0,0,0,100).getRGB());

            mc.font.drawShadow(poseStack,target.getName().getString(), (float) width / 2, (float) height / 2 - 100,-1);
            if (target instanceof LivingEntity) {

                float health = ((LivingEntity) target).getHealth();
                float maxHealth = ((LivingEntity) target).getMaxHealth();

                BigDecimal decimalValue = new BigDecimal(health);
                BigDecimal roundedValue = decimalValue.setScale(1, BigDecimal.ROUND_HALF_UP);
                health = roundedValue.floatValue();


                if (lastHealth != health) {
                    renderLastHealth = lastHealth;
                    lastHealth = health;
                }

                float healthWidth = (100 * (health / maxHealth));
                animHealth = (float) animationUtils.animate(healthWidth, animHealth, 0.3);


                float lastHealthWidth = (100 * (renderLastHealth / maxHealth));

                if (lastHealthWidth > 100) {
                    lastHealthWidth = 100;
                }
                if (animHealth > 100) {
                    animHealth = 100;
                }

                mc.font.drawShadow(poseStack, String.valueOf(health),(float) width / 2, height / 2 - 90, ColorUtils.getHealthColor(health,maxHealth)) ;

                RenderUtils.drawRect(poseStack,(float) width / 2,(float) height / 2 - 80, (float) width / 2 + lastHealthWidth, (float) height / 2 - 75,-1);
                RenderUtils.drawRect(poseStack,(float) width / 2,(float) height / 2 - 80, (float) width / 2 + animHealth, (float) height / 2 - 75,ColorUtils.getHealthColor(health,maxHealth));
            }
        }
        return new Border((float) mc.getWindow().getGuiScaledWidth() / 2 - 5, (float) mc.getWindow().getGuiScaledHeight() / 2 - 105, (float) mc.getWindow().getGuiScaledWidth() / 2 - 5 + 110, (float) mc.getWindow().getGuiScaledHeight() / 2 - 105 + 36);
    }

    @Override
    public void update() {

    }
}
