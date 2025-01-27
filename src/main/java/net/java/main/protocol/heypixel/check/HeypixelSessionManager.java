package net.java.main.protocol.heypixel.check;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.java.main.protocol.heypixel.*;
import net.java.main.protocol.heypixel.check.c2s.ClassesC2SPacket;
import net.java.main.protocol.heypixel.check.s2c.PlayerListDataS2CPacket;
import net.java.main.protocol.heypixel.check.s2c.SyncKeysS2CPacket;
import net.java.main.protocol.heypixel.msgpack.core.MessagePack;
import net.java.main.protocol.heypixel.msgpack.value.Value;
import net.java.main.protocol.heypixel.msgpack.value.ValueFactory;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.java.main.protocol.heypixel.utils.StringUtils;
import net.java.main.utils.RandomUtils;
import net.java.main.utils.network.NetPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.w3c.dom.Entity;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.logging.Level;


public class HeypixelSessionManager {
    public static Minecraft mc;

    public static String channel = "heypixel:check";
    public static int updateInterval = 5;
    public static String placeholderString1 = "EMPTY#1";
    public static String placeholderString2 = "EMPTY#2";
    public static String placeholderString3 = "EMPTY#3";
    public static String defaultBlankValue1 = "BLANK";
    public static String defaultBlankValue2 = "BLANK";
    public static String defaultBlankValue3 = "BLANK";
    public List playerList;
    public int playerCount;
    public byte[] key1;
    public byte[] key2;
    public byte[] key3;
    public HwidContainer hardware;
    public byte[] clientToken;
    public byte[] serverToken;
    public int[] unknownArray;
    public byte[] unknownBytes;
    public byte[] encryptionKey;
    public byte[] encryptMode;
    public int modClassCount;
    public int loadClassCount;
    public int jmapLineCount;
    public int dllCount;
    public HashMap<String, String> tokenMap;
    public MessageDecoder messageDecoder = new MessageDecoder(null);
    public byte[] serverId;
    public byte[] encryptMode1;
    public Set<String> uniqueClassNames = new HashSet<>();
    public Set<String> newDLLs = new HashSet<>();
    public Set<String> allDLLS = new HashSet<>();

    public BufferHelper bufferHelper = new BufferHelper();
    public ResourceLocation lastResourceLocation = null;

    public long lastActionTime = -1;
    public long lastUpdateClasses = -1;
    public long nextSendClasses = -1;
    public long lastCheckTimestamp = 0;
    public boolean classesSent = false;

    public HeypixelSessionManager() {
        tokenMap = new HashMap<>();

        initClasses();
        this.hardware = new HwidContainer();

        mc = Minecraft.getInstance();
    }

    public static String hashString(String str) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String hexString = Integer.toHexString((b | (-256)) + 256);
                if (hexString.length() == 1) {
                    sb.append('0');
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return str;
        }
    }

    public void checkUserInfo() {
        try {
            Heypixel.get().provider().refresh();

            if (mc.hasSingleplayerServer()) return;
            int port = 25565;
            if (mc.getConnection() != null && mc.getCurrentServer() != null) {
                var addr = mc.getCurrentServer().ip;
                if (addr.contains(":")) {
                    port = Integer.parseInt(addr.split(":")[1]);
                }
            }


            long userId = Long.parseLong(Heypixel.get().provider().getUserId(port, Heypixel.get().getPlayerProfile()));
            var hash = StringUtils.hashString(Heypixel.get().provider().getToken(port, Heypixel.get().getPlayerProfile()));
            hardware.setUserInfo(userId, hash);

            var hwids = Heypixel.get().hwids;
            if (hwids.has(String.valueOf(userId))) {
                hardware.loadFromObj(hwids.get(String.valueOf(userId)));
            } else {
                hwids.add(hardware.toObj());
                Heypixel.get().save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateServerInfo(JsonObject jsonObject) {
        Heypixel.randomId = System.currentTimeMillis() - jsonObject.get("ServerTime").getAsLong();
        JsonArray asJsonArray = jsonObject.get("YkdsemRBPT0").getAsJsonArray();
        List<String> arrayList = new ArrayList<>();
        for (JsonElement jsonElement : asJsonArray) {
            arrayList.add(jsonElement.getAsString());
        }

        placeholderString1 = hashString(jsonObject.get(arrayList.get(0)).getAsString());
        placeholderString2 = hashString(jsonObject.get(arrayList.get(1)).getAsString());
        placeholderString3 = hashString(jsonObject.get(arrayList.get(2)).getAsString());
        defaultBlankValue1 = hashString(jsonObject.get(arrayList.get(3)).getAsString());
        defaultBlankValue2 = String.valueOf(hardware.networkHardware);
        defaultBlankValue3 = String.valueOf(hardware.diskSerials);
    }

    public void processSyncKeys(SyncKeysS2CPacket syncKeysPacket) {
        String str = tokenMap.get(messageDecoder.decode(serverToken));
        String str2 = tokenMap.get(messageDecoder.decode(clientToken));
        String str3 = tokenMap.get(messageDecoder.decode(serverId));
        String decodedKeyA = messageDecoder.decode(syncKeysPacket.keyA);
        String decodedKeyB = messageDecoder.decode(syncKeysPacket.keyB);
        String decodedKeyC = messageDecoder.decode(syncKeysPacket.keyC);
        if (decodedKeyA.equals(str)) {
            key1 = syncKeysPacket.keyListA;
        } else if (decodedKeyA.equals(str2)) {
            key2 = syncKeysPacket.keyListA;
        } else if (decodedKeyA.equals(str3)) {
            key3 = syncKeysPacket.keyListA;
        }
        if (decodedKeyB.equals(str)) {
            key1 = syncKeysPacket.keyListB;
        } else if (decodedKeyB.equals(str2)) {
            key2 = syncKeysPacket.keyListB;
        } else if (decodedKeyB.equals(str3)) {
            key3 = syncKeysPacket.keyListB;
        }
        if (decodedKeyC.equals(str)) {
            key1 = syncKeysPacket.keyListC;
        } else if (decodedKeyC.equals(str2)) {
            key2 = syncKeysPacket.keyListC;
        } else if (decodedKeyC.equals(str3)) {
            key3 = syncKeysPacket.keyListC;
        }
    }

    public void handleNetworkEvent(HeypixelCheckPacket heypixelPacket) {
        try {
//            System.out.println("apply: " + heypixelPacket.getClass().getName());
            heypixelPacket.manager = this;
            heypixelPacket.handleClientSide(Minecraft.getInstance().player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendGameInfo() {
        var id = new ResourceLocation("heypixel:game_info");

        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeByte(1);
        buf.writeLong(System.currentTimeMillis());
        buf.writeLong(Heypixel.randomId);

        NetPayload.send(id, buf);

        lastCheckTimestamp = System.currentTimeMillis();
    }


    public void initClasses() {
        loadClassCount = 73508 + RandomUtils.nextInt(-500, 800);
        modClassCount = 39299;
        jmapLineCount = 13390 + RandomUtils.nextInt(-170, 200);
        dllCount = 114;
    }

    public void checkClasses() {
        final var second = 1000;

        // 随机17 - 38秒 加 170 - 300个class
        if (System.currentTimeMillis() - lastUpdateClasses > RandomUtils.nextInt(second * 17, second * 38)) {
            loadClassCount += RandomUtils.nextInt(170, 300);
            jmapLineCount += RandomUtils.nextInt(-10, 37);

            if (RandomUtils.nextBoolean()) {

                var list = new ArrayList<String>();
                for (int i = 0; i < RandomUtils.nextInt(1, 4); i++) {
                    var grab = RandomUtils.nextList(HardwareList.DLLS);
                    if (!newDLLs.contains(grab) && !list.contains(grab)) {
                        list.add(grab);
                    }
                }
                newDLLs.addAll(list);
                dllCount += list.size();
            }

            lastUpdateClasses = System.currentTimeMillis();
        }
    }

    public void setSessionData(PlayerListDataS2CPacket playerListCheckPacket) {
        playerList = playerListCheckPacket.playerList;
        playerCount = playerListCheckPacket.playerCount;
    }

    public void sendClassMap() {
        Map<String, String> hashMap = ClassesRandom.randomCheck1();

        var packet = new ClassesC2SPacket(
                1,
                hashMap.size(),
                hashMap,
                0,
                modClassCount,
                modClassCount
        );
        packet.sendCheckPacket();
    }

    public HeypixelCheckPacket decodePacket(FriendlyByteBuf buf) {
        int pid = buf.readInt();
        var packet = HeypixelCheckPacket.newS2CInstance(pid);
        if (packet != null) {
//            System.out.println("decodedd: " + pid);
            return packet.apply(buf);
        }
        throw new UnsupportedOperationException("This packet ( " + pid + ") does not support in this version.");
    }


    public void onEventTrigger() {
        sendClasses();
    }


    public void sendHeartbeat() {
        try (var buffer = MessagePack.newDefaultBufferPacker()) {
            buffer.packInt(0);
            buffer.packString(Heypixel.get().getPlayerUUID());
            buffer.packValue(new Variable().setIntegerValue(Heypixel.get().runTime));
            buffer.packValue(new Variable().setIntegerValue(System.currentTimeMillis()));

            var buf = new FriendlyByteBuf(Unpooled.buffer());
            buf.writeBytes(buffer.toByteArray());
            NetPayload.send(new ResourceLocation(HeypixelCheckPacket.getChannel()), buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkClassMap() {
        if (classesSent) {
            return;
        }
        classesSent = true;
        sendClassMap();
    }

    public void sendClasses() {
        uniqueClassNames.add("com.sun.jmx.remote.util");
        uniqueClassNames.add("com.sun.org.apache.xerces");
        uniqueClassNames.add("sun.text.resources.cldr.ext");
        uniqueClassNames.add("com.sun.xml.internal.stream");
        uniqueClassNames.add("org.openjdk.nashorn.internal.scripts");

        if (RandomUtils.nextBoolean())
            uniqueClassNames.add("jdk.internal.net.http.SocketTube$InternalWriteSubscriber$WriteSubscription");
        if (RandomUtils.nextBoolean())
            uniqueClassNames.add("net.minecraftforge.common.loot.CanToolPerformAction$Serializer");

        uniqueClassNames.add("sun.util.resources.cldr.provider");

        var classCheck = new ClassesC2SPacket(
                -1,
                uniqueClassNames.size(),
                uniqueClassNames,
                jmapLineCount,
                loadClassCount,
                loadClassCount
        );
        classCheck.sendCheckPacket();
        uniqueClassNames.clear();

        var dllCheck = new ClassesC2SPacket(
                3,
                newDLLs.size(),
                newDLLs,
                0,
                dllCount,
                dllCount
        );
        dllCheck.sendCheckPacket();
        allDLLS.clear();
    }

    public String getEncryptMode() {
        return messageDecoder.decode(encryptMode);
    }

    public String getEncryptMode1() {
        return messageDecoder.decode(encryptMode1);
    }

    public String getEncryptKey() {
        return messageDecoder.decode(encryptionKey);
    }

    public void sendClientInfo() {
        try {
            checkUserInfo();

            if (hardware.user == null) {
                NetPayload.pre(() -> {
            //        ChatUtils.error("无法获取到网易用户信息。");
                });
                return;
            }


            try (var buffer = MessagePack.newDefaultBufferPacker()) {
                buffer.packString(Heypixel.get().clientId.toString());
                buffer.packString(Heypixel.get().getPlayerUUID());
                buffer.packValue(new Variable().setIntegerValue(Heypixel.get().runTime));
                buffer.packBoolean(false);

                var mods = new ArrayList<String>();
                mods.add("minecaft");
                mods.add("entityculling");
                mods.add("armourers_workshop");
                mods.add("netease_official");
                mods.add("immediatelyfast");
                mods.add("culllessleaves");
                mods.add("heypixel");
                mods.add("nochatlagforge");
                mods.add("memoryleakfix");
                mods.add("reeses_sodium_options");
                mods.add("forge");
                mods.add("rubidium");
                mods.add("embeddiumplus");
                mods.add("iceberg");
                mods.add("geckolib3");

                buffer.packValue(new Variable().setStringValue(mods.toString()));

                var local = RandomUtils.random(1, "CDEF");
                buffer.packValue(new Variable().setStringValue(local + ":\\MCLDownload\\Game\\.minecraft"));
                buffer.packValue(new Variable().setStringValue(local + ":\\MCLDownload\\ext\\jre-v64-220420\\jdk17"));

                buffer.packValue(hardware.cpu);
                buffer.packValue(hardware.baseboardSerial);
                buffer.packValue(hardware.diskSerials);
                buffer.packValue(hardware.networkHardware);
                buffer.packValue(hardware.user);
                buffer.packValue(hardware.baseboard);
                buffer.packValue(hardware.disks);
                buffer.packValue(hardware.networkInterfaces);
                buffer.packValue(hardware.system);
                buffer.packValue(hardware.neteaseUsersHash);
                var id = new ResourceLocation(HeypixelCheckPacket.getChannel());
                var buf = new FriendlyByteBuf(Unpooled.buffer());

                HeypixelVarUtils.writeUnsignedInt(buf, 1);
                bufferHelper.writeByteArray(buf, buffer.toByteArray());

                NetPayload.send(id, buf);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void asyncSendClientData() {
        sendClientInfo();
    }


    public void periodicClientData() {
        if (System.currentTimeMillis() - lastCheckTimestamp > updateInterval * 1000L) {
            sendHeartbeat();
            lastCheckTimestamp = System.currentTimeMillis();
        }
    }

    public void onClientTick() {
        checkClassMap();
        periodicClientData();
        checkClasses();
    }


    public void onServerEvent(S2CEvent s2CEvent) {
        String event = s2CEvent.getEvent();
        JsonObject data = s2CEvent.getData();
        if (event.equals("ServerRPC")) {
            placeholderString1 = data.get("s1").getAsString();
            placeholderString2 = data.get("s2").getAsString();
            placeholderString3 = data.get("s3").getAsString();
        }
        if ("SyncServerInfo".equals(s2CEvent.getEvent())) {
            updateServerInfo(data);
        }
    }

    public void onEntityJoinWorld(ClientLevel world, LivingEntity entity) {
        try {
            if (!entity.getUUID().equals(mc.player.getUUID())) return;

            ResourceLocation location = world.dimension().location();
            sendClientInfo();
            if (System.currentTimeMillis() > nextSendClasses) {
                nextSendClasses = System.currentTimeMillis() + 1000;
                classesSent = false;
            }
            if (System.currentTimeMillis() > this.lastActionTime + 180000) {
                onEventTrigger();
                this.lastActionTime = System.currentTimeMillis();
            } else {
                if (this.lastResourceLocation == null || location.getPath().equals(this.lastResourceLocation.getPath())) {
                    return;
                }
                this.lastResourceLocation = location;
                if (System.currentTimeMillis() > this.lastActionTime + 60000) {
                    onEventTrigger();
                    this.lastActionTime = System.currentTimeMillis();
                }
            }
        } catch (Exception ex) {
        }
    }

}
