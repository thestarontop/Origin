package com.mojang.mixins;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.RenderNamePlateEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
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
}
