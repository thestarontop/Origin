package net.java.main.protocol.heypixel.check.c2s;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.msgpack.core.MessageBufferPacker;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

import java.io.IOException;
import java.util.List;



public class HitResultC2SPacket extends HeypixelCheckPacket {


    public static long delay;
    public InteractionHand hand;
    public BlockHitResult hitResult;


    public AbstractClientPlayer player;

    public HitResultC2SPacket(ByteBuf friendlyByteBuf) {
    }


    public HitResultC2SPacket(AbstractClientPlayer localPlayer, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        this.player = localPlayer;
        this.hand = interactionHand;
        this.hitResult = blockHitResult;
    }



}
