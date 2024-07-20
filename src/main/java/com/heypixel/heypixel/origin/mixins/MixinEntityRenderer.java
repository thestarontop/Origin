package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.RenderNamePlateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.modules.ModuleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer <T extends Entity>{
    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Shadow public abstract Font getFont();

    @Inject(method = "render",at=@At("HEAD"))
    public void render(T arg, float f, float g, PoseStack arg2, MultiBufferSource arg3, int i, CallbackInfo ci) {
        RenderNamePlateEvent event = new RenderNamePlateEvent(arg,arg2,arg3);
        Origin.getInstance().getEventManager().call(event);
    }
}
