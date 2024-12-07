package net.java.mixins.acesser;

import net.minecraft.client.Timer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundInteractPacket.class)
public interface ServerboundInteractPacketAcesser {

    @Mutable
    @Accessor("entityId")
    int getId();
}
