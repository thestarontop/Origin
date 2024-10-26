package net.java.mixins;

import net.java.main.madebystarontopandfml;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.render.ESP;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {
    @Inject(method = "renderEntity",at=@At("HEAD"))
    private void renderEntity1(Entity arg, double d, double e, double g, float h, PoseStack arg2, MultiBufferSource arg3, CallbackInfo ci) {
            if (madebystarontopandfml.getInstance().getModuleManager().getModule("chams").isEnabled()) {
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1f, -1000000F);
            }
    }

    @Inject(method = "renderEntity", at = @At("RETURN"))
    private void renderEntity2(Entity arg, double d, double e, double g, float h, PoseStack arg2, MultiBufferSource arg3, CallbackInfo ci){
            if (madebystarontopandfml.getInstance().getModuleManager().getModule("chams").isEnabled()) {
                GL11.glPolygonOffset(1f, 1000000F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            }
    }
    @Inject(method = "shouldShowEntityOutlines", at = @At("HEAD"), cancellable = true)
    private void shouldShowEntityOutlines(CallbackInfoReturnable<Boolean> cir) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("ESP").isEnabled() && ESP.mode.getValue().equals("Glow")) {
            cir.setReturnValue(true);
        }
    }

}
