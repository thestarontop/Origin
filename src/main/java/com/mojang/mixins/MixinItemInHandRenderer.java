package com.mojang.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
    @Shadow private float offHandHeight;

    @Shadow private float mainHandHeight;

    @Shadow private ItemStack mainHandItem;

    @Shadow private ItemStack offHandItem;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private float oOffHandHeight;

    @Shadow private float oMainHandHeight;
    }

