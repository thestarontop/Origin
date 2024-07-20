package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.RenderLivingEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer <T extends LivingEntity> {

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;)Z",at=@At("HEAD"), cancellable = true)
    protected void shouldShowName(T arg, CallbackInfoReturnable<Boolean> cir) {
        if (Origin.getInstance().getModuleManager().getModule("nametags").isEnabled()) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",at=@At("HEAD"))
    public void render(T arg, float g, float h, PoseStack arg2, MultiBufferSource arg3, int j, CallbackInfo ci){
    Origin.getInstance().getEventManager().call(new RenderLivingEvent(arg,arg2,arg3,h));
    }

}
