package com.mojang.mixins;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.Render3DEvent;
import com.mojang.main.event.events.Render2DEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
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
        madebystarontopandfml.getInstance().getEventManager().call(new Render2DEvent());
    }

}
