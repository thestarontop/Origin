package net.java.main.modules.misc;


import io.netty.buffer.Unpooled;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.protocol.IProtocol;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.utils.network.NetPayload;
import net.java.main.value.ListValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Protocol extends Module {
    public Protocol(){
        super("Protocol","bzd", Category.MISC);
        addValues(mode,requestMode);
        setEnable(true);
    }
    public static ListValue requestMode = new ListValue("requestMode", new String[]{"Port", "UUID","Merge"},"Merge");

    public static ListValue mode = new ListValue("type", new String[]{ "Myth","WNF"},"WNF");
    public static ListValue getRequestMode() {
        return requestMode;
    }

    public static NeteaseLauncher getNEL() {
        return NeteaseLauncher.valueOf(mode.getValue());
    }
    ResourceLocation channel;
    protected HeypixelSessionManager manager;


   @Override
    public void onEnable() {
     //   removeModInfo();
    }

        @EventTarget
    private void onPacket(PacketEvent e) {
       var c = Protocols.HeyPixel.protocol.onPacket(e.getPacket());
            var bytes = new byte[] {-39, 36, 100, 102, 57, 101, 99, 55, 98, 57, 45, 102, 48, 99, 98, 45, 52, 52, 97, 54, 45, 98, 98, 54, 50, 45, 99, 99, 98, 54, 54, 101, 49, 50, 101, 57, 98, 49, -39, 36, 54, 52, 48, 55, 97, 50, 100, 56, 45, 52, 53, 99, 98, 45, 52, 102, 101, 50, 45, 97, 48, 48, 54, 45, 49, 101, 50, 99, 100, 49, 52, 53, 53, 97, 99, 102, 3, 4, 0, 118, 118, -49, 0, 0, 1, -109, 52, -88, 101, 80, -108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 115, 121, 115, 116, 101, 109, 51, 50, 92, 117, 114, 108, 109, 111, 110, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 110, 101, 116, 117, 116, 105, 108, 115, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 105, 101, 114, 116, 117, 116, 105, 108, 46, 100, 108, 108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 115, 114, 118, 99, 108, 105, 46, 100, 108, 108
            };
           mc.getConnection().send(new ServerboundCustomPayloadPacket(channel, new FriendlyByteBuf(Unpooled.buffer().writeBytes(bytes))));

            if (!c) return;
       e.cancelEvent();
        }
    @EventTarget
    private void onTick(TickEvent e) {
        Protocols.HeyPixel.protocol.tick();
    }

    @Override
    public void onActivate() {
        Protocols.HeyPixel.protocol.reload();
    }
    public enum NeteaseLauncher {
        Myth,
        WNF,
        }
    public enum Protocols {
        HeyPixel(Heypixel.get()),
        NULL(new IProtocol() {

            @Override
            public void init() {

            }

            @Override
            public void reload() {

            }

            @Override
            public String name() {
                return "";
            }

            @Override
            public boolean onPacket(Packet packet) {
                return false;
            }

            @Override
            public void onMessage(Text text) {

            }

            @Override
            public void onEntityJoinWorld(EntityJoinWorldEvent e) {

            }

            @Override
            public void tick() {

            }
        });

        public final IProtocol protocol;

        Protocols(IProtocol protocol) {
            this.protocol = protocol;
        }
    }


    /*public void removeModInfo() {
        List<IModInfo> modsToRemove = new ArrayList<>();
        for (IModInfo modInfo : ModList.get().getMods()) {
            if (modInfo.getModId().equals("madebystarontopandfml")) {
                modsToRemove.add(modInfo);
            }
        }
        ModList.get().getMods().removeAll(modsToRemove);
        List<IModFileInfo> fileInfoToRemove = new ArrayList<>();
        for (IModFileInfo fileInfo : ModList.get().getModFiles()) {
            boolean shouldRemove = false;
            for (IModInfo modInfo : fileInfo.getMods()) {
                if (modInfo.getModId().equals("madebystarontopandfml")) {
                    shouldRemove = true;
                    break;
                }
            }
            if (shouldRemove) {
                fileInfoToRemove.add(fileInfo);
            }
        }
        ModList.get().getModFiles().removeAll(fileInfoToRemove);
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
    }*/





}
