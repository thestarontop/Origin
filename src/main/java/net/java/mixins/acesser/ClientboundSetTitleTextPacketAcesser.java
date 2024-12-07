package net.java.mixins.acesser;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetTitleTextPacket.class)
public interface ClientboundSetTitleTextPacketAcesser {
    @Mutable
    @Accessor("text")
    void setmessage(Component message);
}
