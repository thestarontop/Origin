package net.java.mixins.acesser;

import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockHitResult.class)
public interface BlockHitResultAcesser {
    @Mutable
    @Accessor("inside")
    void setInside(boolean inside);
    @Mutable
    @Accessor("miss")
    void setMiss(boolean miss);
}
