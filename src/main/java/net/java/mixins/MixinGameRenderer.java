package net.java.mixins;

import com.google.common.collect.Maps;
import com.mojang.math.Vector3f;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.Render3DEvent;
import net.java.main.event.events.Render2DEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.render.NoHurtCam;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Shadow public abstract Camera getMainCamera();


    @Inject(method = "renderLevel",at=@At(value="FIELD",target= "Lnet/minecraft/client/renderer/GameRenderer;renderHand:Z"))
    public void renderLevel(float g, long l, PoseStack arg, CallbackInfo ci) {
        madebystarontopandfml.getInstance().getEventManager().call(new Render3DEvent(arg, g));
    }
    @Inject(method = "render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/Gui;render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    public void onRender(float g, long l, boolean bl, CallbackInfo ci){
        madebystarontopandfml.getInstance().getEventManager().call(new Render2DEvent(g));
    }
    @Overwrite
    private void bobHurt(PoseStack arg, float g) {
        if (madebystarontopandfml.moduleManager.getModule("NoHurtCam").isEnabled())
            return;
        if (Minecraft.getInstance().getCameraEntity() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)Minecraft.getInstance().getCameraEntity();
            float f = (float)livingentity.hurtTime - g;
            float f2;
            if (livingentity.isDeadOrDying()) {
                f2 = Math.min((float)livingentity.deathTime + g, 20.0F);
                arg.mulPose(Vector3f.ZP.rotationDegrees(40.0F - 8000.0F / (f2 + 200.0F)));
            }

            if (f < 0.0F) {
                return;
            }

            f /= (float)livingentity.hurtDuration;
            f = Mth.sin(f * f * f * f * 3.1415927F);
            f2 = livingentity.hurtDir;
            arg.mulPose(Vector3f.YP.rotationDegrees(-f2));
            arg.mulPose(Vector3f.ZP.rotationDegrees(-f * 14.0F));
            arg.mulPose(Vector3f.YP.rotationDegrees(f2));
        }

    }

}
