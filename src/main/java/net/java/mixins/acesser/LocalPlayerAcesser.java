package net.java.mixins.acesser;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface LocalPlayerAcesser {
    @Mutable
    @Accessor("positionReminder")
    int getPositionReminder();

    @Mutable
    @Accessor("positionReminder")
    void setPositionReminder(int positionReminder);
}
