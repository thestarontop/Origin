package com.heypixel.heypixel.origin.mixins;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundMovePlayerPacket.class)
public interface MixinServerboundMovePlayerPacket {
    @Mutable
    @Accessor("yRot")
    void setYaw(float yaw);

    @Mutable
    @Accessor("xRot")
    void setPitch(float pitch);

    @Mutable
    @Accessor("hasRot")
    void setHasRot(boolean hasRot);
}
