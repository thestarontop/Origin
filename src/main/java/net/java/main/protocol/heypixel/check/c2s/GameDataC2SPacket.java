package net.java.main.protocol.heypixel.check.c2s;



import io.netty.buffer.ByteBuf;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.protocol.heypixel.msgpack.core.MessageBufferPacker;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;

import java.io.IOException;
import java.util.List;
import java.util.UUID;



public class GameDataC2SPacket extends HeypixelCheckPacket {


    public Entity entity;


    public AbstractClientPlayer player;


    public HitResult hitResult;

    public GameDataC2SPacket(AbstractClientPlayer player, Entity entity) {
        this.player = player;
        this.entity = entity;
        this.hitResult = mc.hitResult;
    }


    public GameDataC2SPacket(ByteBuf friendlyByteBuf) {
    }

    public static void check(HeypixelSessionManager manager, AbstractClientPlayer player, Entity entity) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.getCameraEntity() == null || minecraft.level == null || mc.hitResult == null) {
            return;
        }
        new GameDataC2SPacket(player, entity).m(manager).sendCheckPacketVanilla();
    }


    /*@Override
    public void writeData(MessageBufferPacker packer) {
        try {
            var uuid = this.entity.getUUID().equals(Heypixel.get().getPlayerUUID()) ? Heypixel.get().getPlayerUUID() : entity.getUUID().toString();
            packer.packValue(new Variable().setStringValue(uuid));
            packer.packInt(this.hitResult.getType().ordinal());
            packer.packDouble(this.hitResult.getPos().x);
            packer.packDouble(this.hitResult.getPos().y);
            packer.packDouble(this.hitResult.getPos().z);
            packer.packValue(new Variable().setIntegerValue(this.player.getPose().ordinal()));
            packer.packValue(new Variable().setArrayValue(
                List.of(
                    new Variable().setFloatValue(this.player.getPos().x),
                    new Variable().setFloatValue(this.player.getPos().y),
                    new Variable().setFloatValue(this.player.getPos().z)
                )));
            packer.packFloat(this.player.getRotationClient().x);
            packer.packFloat(this.player.getRotationClient().y);

            packer.packInt(this.entity.getPose().ordinal());
            packer.packDouble(this.entity.getPos().x);
            packer.packDouble(this.entity.getPos().y);
            packer.packDouble(this.entity.getPos().z);
            packer.packValue(new Variable().setFloatValue(this.entity.getRotationClient().x));
            packer.packValue(new Variable().setFloatValue(this.entity.getRotationClient().y));
        } catch (IOException e) {
        }
    }

    @Override
    public void processBuffer(ByteBuf buf, BufferHelper bufferHelper) {
        var uuid = this.entity.getStringUUID().equals(Heypixel.get().getPlayerUUID()) ? Heypixel.get().getPlayerUUID() : entity.getStringUUID();
        bufferHelper.writeUUID(buf, UUID.fromString(uuid));
        HeypixelVarUtils.writeVarInt(buf, this.hitResult.getType().ordinal());
        bufferHelper.writeVec3(buf, this.hitResult.getPos());

        bufferHelper.writeEnum(buf, this.player.getPose());
        bufferHelper.writeVec3(buf, this.player.getPos());
        bufferHelper.writeVec2(buf, this.player.getRotationClient());

        bufferHelper.writeEnum(buf, this.entity.getPose());
        bufferHelper.writeVec3(buf, this.entity.getPos());
        bufferHelper.writeVec2(buf, this.entity.getRotationClient());
    }*/
}
