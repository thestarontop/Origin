package net.java.mixins;

import net.java.main.madebystarontopandfml;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class MixinScreenEffectRenderer {
    @Inject(method = "renderFire",at=@At("HEAD"), cancellable = true)
    private static void renderFire(Minecraft arg, PoseStack arg2, CallbackInfo ci){
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("LowFire").isEnabled())
            ci.cancel();
    }
    @Inject(method = "renderWater",at=@At("HEAD"), cancellable = true)
    private static void renderWater(Minecraft arg, PoseStack arg2, CallbackInfo ci){
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("LowFire").isEnabled())
            ci.cancel();
    }
}
