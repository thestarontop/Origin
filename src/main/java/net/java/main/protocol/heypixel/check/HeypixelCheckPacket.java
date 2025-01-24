package net.java.main.protocol.heypixel.check;


import com.mojang.authlib.minecraft.client.MinecraftClient;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.c2s.*;
import net.java.main.protocol.heypixel.check.s2c.HeypixelKeysS2CPacket;
import net.java.main.protocol.heypixel.check.s2c.*;
import net.java.main.protocol.heypixel.msgpack.core.MessageBufferPacker;
import net.java.main.protocol.heypixel.msgpack.core.MessagePack;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.java.main.utils.network.NetPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;



public class HeypixelCheckPacket {
    public static final String channel = "heypixel:check";
    public static final Minecraft mc = Minecraft.getInstance();

    public static BufferHelper helper = Heypixel.bufferHelper;

    public static Map<Integer, Function<ByteBuf, HeypixelCheckPacket>> s2cMap = new HashMap<>();
    public static Map<Class<? extends HeypixelCheckPacket>, Integer> c2sMap = new HashMap<>();

    protected HeypixelSessionManager manager;

    public static void init() {
        registerC2S(0, EmptyC2SPacket.class);
        registerC2S(1, GameDataC2SPacket.class);
        registerC2S(2, HitResultC2SPacket.class);
        registerC2S(3, ReflectDataC2SPacket.class);
        registerC2S(4, EncryptDataC2SPacket.class);
        registerC2S(5, BlockStateC2SPacket.class);
        registerC2S(6, ClassesC2SPacket.class);

        registerS2C(100, UnknownS2CPacket::new);
        registerS2C(101, HeypixelKeysS2CPacket::new);
        registerS2C(102, TokenDataS2CPacket::new);
        registerS2C(103, SyncKeysS2CPacket::new);
        registerS2C(104, ReflectCheckS2CPacket::new);
        registerS2C(105, PlayerListDataS2CPacket::new);
        registerS2C(106, BlockPosS2CPacket::new);
        registerS2C(107, NeteaseCheckS2CPacket::new);
    }

    private static void registerC2S(int id, Class<? extends HeypixelCheckPacket> cls) {
        c2sMap.put(cls, id);
    }

    private static void registerS2C(int id, Function<ByteBuf, HeypixelCheckPacket> function) {
        s2cMap.put(id, function);
    }

    public static BufferHelper getHelper() {
        return helper;
    }

    public static String getChannel() {
        return channel;
    }

    public static Function<ByteBuf, HeypixelCheckPacket> newS2CInstance(int i) {
        return s2cMap.get(i);
    }

    public void handleClass(Class<? extends HeypixelCheckPacket> cls) {
    }

    public void processBuffer(ByteBuf buf, BufferHelper helper) {
    }
   // AbstractClientPlayer
    public void handleClientSide(AbstractClientPlayer player) {
        throw new UnsupportedOperationException("This packet ( " + getPacketId() + ") does not implement a client side handler.");
    }

    public ByteBuf createPacketBuffer() {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        HeypixelVarUtils.writeUnsignedInt(buf, getPacketId());
        return buf;
    }

    public void sendCheckPacket() {
        try(var buffer = MessagePack.newDefaultBufferPacker()) {
            buffer.packString(Heypixel.get().clientId.toString());
            buffer.packString(Heypixel.get().getPlayerUUID());
            writeData(buffer);

            var buf = new FriendlyByteBuf(Unpooled.buffer());

            HeypixelVarUtils.writeUnsignedInt(buf, getPacketId());
            helper.writeByteArray(buf, buffer.toByteArray());

            ResourceLocation channel = new ResourceLocation(getChannel());
            NetPayload.send(channel, buf);
//            System.out.println("heypixel: send check packet " + getClass().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCheckPacketVanilla() {
        ResourceLocation channel;
        if (manager.getPlayerList() == null || manager.getPlayerList().isEmpty()) {
            channel = new ResourceLocation(getChannel());
        } else {
            channel = new ResourceLocation(Heypixel.MOD_ID + ":" + manager.convertBytesToString((byte[]) manager.getPlayerList().get(manager.getPlayerCount())));
        }

        var buf = createPacketBuffer();
        processBuffer(buf, getHelper());

        NetPayload.send(channel, buf);
//        System.out.println("heypixel: send check packet vanilla");
    }

    public int getPacketId() {
        return c2sMap.getOrDefault(getClass(), -1);
    }

    public void writeData(MessageBufferPacker packer) throws IOException {
    }

    public <T extends HeypixelCheckPacket> T m(HeypixelSessionManager manager) {
        this.manager = manager;
        return (T) this;
    }
}
