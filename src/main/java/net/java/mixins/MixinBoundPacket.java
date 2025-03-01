package net.java.mixins;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.java.main.event.events.PacketEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.exploit.Disabler;
import net.java.main.utils.PacketUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.java.main.utils.MinecraftInstance.mc;

@Mixin(ServerboundPongPacket.class)
public class MixinBoundPacket  {
    @Shadow int id;


    @Inject(method = "write",at=@At("HEAD"), cancellable = true)
    private void write(FriendlyByteBuf arg, CallbackInfo ci){
        if (Disabler.getGrimPost() && id < 0) {
            Disabler.pingPackets.add(id);
        }
    }






}
