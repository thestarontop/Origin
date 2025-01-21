package net.java.mixins;

import com.mojang.math.Matrix4f;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.RenderNamePlateEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer <T extends Entity>{
    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Shadow public abstract Font getFont();

    @Inject(method = "render",at=@At("HEAD"))
    public void render(T arg, float f, float g, PoseStack arg2, MultiBufferSource arg3, int i, CallbackInfo ci) {
        RenderNamePlateEvent event = new RenderNamePlateEvent(arg,arg2,arg3);
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    protected void renderNameTag(T arg, Component arg2, PoseStack arg3, MultiBufferSource arg4, int k) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(arg);
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nameprotect").isEnabled()) {
            arg2 = new TextComponent(ColorUtils.makeColour("Hidden"));
        }
        if (ForgeHooksClient.isNameplateInRenderDistance(arg, d0)) {
            boolean flag = !arg.isDiscrete();
            float f = arg.getBbHeight() + 0.5F;
            int i = "deadmau5".equals(arg2.getString()) ? -10 : 0;
            arg3.pushPose();
            arg3.translate(0.0, (double)f, 0.0);
            arg3.mulPose(this.entityRenderDispatcher.cameraOrientation());
            arg3.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = arg3.last().pose();
            float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int j = (int)(f1 * 255.0F) << 24;
            Font font = this.getFont();
            float f2 = (float)(-font.width(arg2) / 2);
            font.drawInBatch(arg2, f2, (float)i, 553648127, false, matrix4f, arg4, flag, j, k);
            if (flag) {
                font.drawInBatch(arg2, f2, (float)i, -1, false, matrix4f, arg4, false, 0, k);
            }

            arg3.popPose();
        }

    }
}
