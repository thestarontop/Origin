package net.java.main.protocol;



import com.mojang.authlib.GameProfile;
import net.java.main.event.events.EntityJoinWorldEvent;
import net.java.main.event.events.WorldChangeEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.network.protocol.Packet;
import org.w3c.dom.Text;

import java.io.File;

import static net.java.main.utils.MinecraftInstance.mc;


public interface IProtocol {
    File FOLDER = new File(madebystarontopandfml.NAME, "protocol");

    void init();

    void reload();

    String name();

    boolean onPacket(Packet packet);

    void onMessage(Text text);
    void onWorldChanged(WorldChangeEvent e);

    void onEntityJoinWorld(EntityJoinWorldEvent e);
    void tick();

    default GameProfile getPlayerProfile() {
        try {
            return mc.player.getGameProfile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
