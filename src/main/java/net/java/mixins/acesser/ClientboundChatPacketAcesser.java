package net.java.mixins.acesser;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundChatPacket.class)
public interface ClientboundChatPacketAcesser {
    @Mutable
    @Accessor("message")
    void setmessage(Component message);
}
