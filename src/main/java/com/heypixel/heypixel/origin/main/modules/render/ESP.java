package com.heypixel.heypixel.origin.main.modules.render;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.event.events.Render3DEvent;
import com.heypixel.heypixel.origin.main.event.events.RenderLivingEvent;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.entity.PartEntity;

import java.awt.*;


public class ESP extends Module {
    public ESP(){super("ESP","render entities",Category.RENDER);}


    @EventTarget
    public void onRenderLiving(RenderLivingEvent event){
        RenderUtils.renderPlayerHitbox(event.getPosestack(),event.getEntity(),event.getPartialticks(),event.getMultibuffersource());
    }




}
