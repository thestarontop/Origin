package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.event.events.RenderLivingEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer <T extends LivingEntity> {

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z",at=@At("HEAD"), cancellable = true)
    protected void shouldShowName(T arg, CallbackInfoReturnable<Boolean> cir) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nametags").isEnabled()) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at=@At("HEAD"))
    public void render(T arg, float g, float h, PoseStack arg2, MultiBufferSource arg3, int j, CallbackInfo ci){
    madebystarontopandfml.getInstance().getEventManager().call(new RenderLivingEvent(arg,arg2,arg3,h));
    }

}
