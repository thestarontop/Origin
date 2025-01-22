package net.java.main.modules.misc;


import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.modules.Module;
import net.java.main.protocol.IProtocol;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.value.ListValue;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.w3c.dom.Text;

public class Protocol extends Module {
    public Protocol(){
        super("Protocol","bzd", Category.MISC);
        addValues(mode);

    }


    public static ListValue mode = new ListValue("type", new String[]{"Zone", "Myth"},"Myth");






    public static NeteaseLauncher getNEL() {
        return NeteaseLauncher.valueOf(mode.getValue());
    }

    @EventTarget
    private void onPacket(PacketEvent e) {
       var c = Protocols.HeyPixel.protocol.onPacket(e.getPacket());
        e.setCancelled(c);
        var c1 = Protocols.NULL.protocol.onPacket(e.getPacket());
        e.setCancelled(c1);
    }
    @EventTarget
    private void onTick(TickEvent e) {
        Protocols.HeyPixel.protocol.tick();

        Protocols.NULL.protocol.tick();
    }
    @Override
    public void onActivate() {
        Protocols.HeyPixel.protocol.reload();
        Protocols.NULL.protocol.reload();
    }
    public enum NeteaseLauncher {
        Zone,
        Myth
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
