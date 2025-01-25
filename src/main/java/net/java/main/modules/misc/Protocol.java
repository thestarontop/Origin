package net.java.main.modules.misc;


import io.netty.buffer.Unpooled;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.modules.Module;
import net.java.main.protocol.IProtocol;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.utils.network.NetPayload;
import net.java.main.value.ListValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.w3c.dom.Text;

public class Protocol extends Module {
    public Protocol(){
        super("Protocol","bzd", Category.MISC);
        addValues(mode,requestMode);

    }
    public static ListValue requestMode = new ListValue("requestMode", new String[]{"Port", "UUID","Merge"},"Merge");

    public static ListValue mode = new ListValue("type", new String[]{ "Myth","WNF"},"Myth");
    public static ListValue getRequestMode() {
        return requestMode;
    }

    public static NeteaseLauncher getNEL() {

        return NeteaseLauncher.valueOf(mode.getValue());
    }
    ResourceLocation channel;
    protected HeypixelSessionManager manager;

    @EventTarget
    private void onPacket(PacketEvent e) {
       var c = Protocols.HeyPixel.protocol.onPacket(e.getPacket());
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
}
