package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.Render3DEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
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
        var newPoseStack = new PoseStack();
        Matrix4f matrix4f2 = arg.last().pose();
        newPoseStack.last().pose().load(matrix4f2);
        Origin.getInstance().getEventManager().call(new Render3DEvent(matrix4f2, this.getMainCamera(), g));
    }
    @Inject(method = "render",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/gui/Gui;render(Lcom/mojang/blaze3d/vertex/PoseStack;F)V"))
    public void onRender(float g, long l, boolean bl, CallbackInfo ci){
        Origin.getInstance().getEventManager().call(new Render2DEvent());
    }

}
