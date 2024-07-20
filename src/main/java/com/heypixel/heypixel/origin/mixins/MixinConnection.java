package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import com.heypixel.heypixel.origin.main.Origin;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinConnection {
    @Inject(method = "sendPacket",at=@At("HEAD"), cancellable = true)
    private void sendPacket(Packet<?> arg, GenericFutureListener<? extends Future<? super Void>> genericFutureListener, CallbackInfo ci){
        PacketEvent event = new PacketEvent(arg);
        if(!PacketUtils.pass) {
            Origin.getInstance().getEventManager().call(event);
        }
        if(event.isCancelled){
            ci.cancel();
        }
    }
    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",at=@At("HEAD"), cancellable = true)
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet<?> arg, CallbackInfo ci){
        PacketEvent event = new PacketEvent(arg);
        if(!PacketUtils.pass) {
            Origin.getInstance().getEventManager().call(event);
        }
        if(event.isCancelled){
            ci.cancel();
        }
    }

}
