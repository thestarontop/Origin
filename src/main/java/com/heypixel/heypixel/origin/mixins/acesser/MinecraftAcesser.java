package com.heypixel.heypixel.origin.mixins.acesser;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAcesser {
    @Mutable
    @Accessor("timer")
    void setTimer(Timer timer);
}
