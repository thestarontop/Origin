package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.exploit.Disabler;
import net.java.main.utils.PacketUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPongPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.java.main.utils.MinecraftInstance.mc;

@Mixin(Connection.class)
public class MixinConnection {
    @Inject(method = "sendPacket",at=@At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> arg, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, CallbackInfo ci){
        PacketEvent event = new PacketEvent(arg);
        if(!PacketUtils.pass) {
            madebystarontopandfml.getInstance().getEventManager().call(event);
        }
        if(event.isCancelled){
            ci.cancel();
        }
    }
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",at=@At("HEAD"), cancellable = true)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> arg, CallbackInfo ci){
        PacketEvent event = new PacketEvent(arg);
        if(!PacketUtils.pass) {
            madebystarontopandfml.getInstance().getEventManager().call(event);
        }
        if(event.isCancelled){
            ci.cancel();
        }
    }
    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void genericsFtw(Packet<T> arg, PacketListener arg2, CallbackInfo info) {
        if (Disabler.getGrimPost() && Disabler.grimPostDelay(arg)) {
            mc.execute(() -> {Disabler.storedPackets.add((Packet<PacketListener>) arg2);
            });
        }
    }
}
