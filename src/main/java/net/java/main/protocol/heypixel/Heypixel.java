package net.java.main.protocol.heypixel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.java.main.event.annotations.EventTarget;
import net.java.main.protocol.IProtocol;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.nel.GameSessionProvider;
import net.java.main.utils.network.NetPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import static net.java.main.protocol.heypixel.check.c2s.ReflectDataC2SPacket.GSON;


public class Heypixel implements IProtocol {
    public static final String MOD_ID = "heypixel";
    public static final ResourceLocation S2C_CHANNEL = new ResourceLocation("heypixel", "s2cevent");
    public static Heypixel sInstance;
    public static long randomId = -1L;
    public static BufferHelper bufferHelper = new BufferHelper();
    public UUID clientId = UUID.randomUUID();
    public long runTime;

    public HeypixelHwids hwids = new HeypixelHwids();

    public void save() {
        try {
            String json = GSON.toJson(hwids);
            if (!CFG.exists()) CFG.createNewFile();
            Files.writeString(CFG.toPath(), json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final File CFG = new File(FOLDER, "heypixel.json");

    public HeypixelSessionManager manager;

    public GameSessionProvider provider() {
        return GameSessionProvider.get();
    }

    public static Heypixel get() {
        if (sInstance == null) {
            sInstance = new Heypixel();
            sInstance.init();
        }
        return sInstance;
    }

    static {
        HeypixelCheckPacket.init();
    }

    @Override
    public void init() {
        try {
            if (!FOLDER.exists()) FOLDER.mkdirs();

            this.hwids.hwids.clear();
            if (CFG.exists()) {
                HeypixelHwids hwids1 = GSON.fromJson(Files.readString(CFG.toPath(), StandardCharsets.UTF_8), HeypixelHwids.class);
                this.hwids.hwids.addAll(hwids1.hwids);
            } else save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sInstance = this;
        runTime = System.currentTimeMillis() - 15000;
        manager = new HeypixelSessionManager();
    }

    public String getPlayerUUID() {
        return Minecraft.getInstance().player.getStringUUID();
    }

    @Override
    public void reload() {
        clientId = UUID.randomUUID();
        runTime = System.currentTimeMillis() - 15000;
        manager = new HeypixelSessionManager();

        NetPayload.register(S2C_CHANNEL, buf -> {
        });
    }

    @Override
    public String name() {
        return "Heypixel";
    }

    @Override
    public boolean onPacket(Packet packet) {
        if (packet instanceof ServerboundCustomPayloadPacket p) {
            if (p.getData() instanceof NetPayload.PayloadPacket payload) {
                // FORGE CHANNEL INDEX
                var buf = payload.buf;
                var index = buf.readUnsignedByte();
                switch (index) {
                    case 233 -> { // S2C DEFAULT
                        JsonObject asJsonObject = JsonParser.parseString(buf.toString(StandardCharsets.UTF_8)).getAsJsonObject();
                        if (asJsonObject.has("plugin") && asJsonObject.has("event") && asJsonObject.has("data")) {
                            var s2c = new S2CEvent(asJsonObject.get("plugin").getAsString(), asJsonObject.get("event").getAsString(), asJsonObject.getAsJsonObject("data"));
                            manager.onServerEvent(s2c);
                        }

                    }
                    case 250 -> { // HEYPIXEL SESSION
                        try {
                            var heypixelCheckPacket = manager.decodePacket(buf);
                            manager.handleNetworkEvent(heypixelCheckPacket);
                        } catch (Throwable t) {
                            //t.printStackTrace();
                        }
                    }
                }


                return true;
            }
            /*if (packet instanceof ServerboundCustomPayloadPacket e) {
                boolean b = !(e.getData() instanceof NetPayload.PayloadPacket);
                if (e.getData().readIntIdList().toString().equals("minecraft:register")) {
                    return true;
                }
            }*/

        }
        return false;
    }


        @Override
        public void onMessage (Text text){
            HeypixelSessionManager.handleChatMessage(text);
        }

        @Override
        public void onEntityJoinWorld (EntityJoinWorldEvent e){
            //  manager.onEntityJoinWorld(e.getWorld(), e.getEntity());
        }


        @Override
        public void tick () {
            manager.onClientTick();
        }

}

