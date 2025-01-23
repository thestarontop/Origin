package net.java.mixins.acesser;

import net.minecraft.client.Timer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetEntityMotionPacket.class)
public interface ClientboundSetEntityMotionPacketAcesser {
    @Mutable
    @Accessor("xa")
    void setXa(int Xa);
    @Mutable
    @Accessor("ya")
    void setYa(int Ya);
    @Mutable
    @Accessor("za")
    void setZa(int Za);
}
