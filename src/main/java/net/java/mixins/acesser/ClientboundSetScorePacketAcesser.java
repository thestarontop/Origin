package net.java.mixins.acesser;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSetScorePacket.class)
public interface ClientboundSetScorePacketAcesser {
    @Mutable
    @Accessor("objectiveName")
    void setObjectiveName(String objectiveName);
}
