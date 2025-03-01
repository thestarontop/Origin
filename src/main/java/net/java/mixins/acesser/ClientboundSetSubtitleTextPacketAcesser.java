package net.java.mixins.acesser;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetSubtitleTextPacket.class)
public interface ClientboundSetSubtitleTextPacketAcesser {
    @Mutable
    @Accessor("text")
    void setmessage(Component message);
}
