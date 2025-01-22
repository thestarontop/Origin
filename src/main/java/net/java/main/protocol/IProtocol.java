package net.java.main.protocol;



import net.java.main.madebystarontopandfml;
import net.minecraft.network.protocol.Packet;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import org.w3c.dom.Text;

import java.io.File;


public interface IProtocol {
    File FOLDER = new File(madebystarontopandfml.NAME, "protocol");

    void init();

    void reload();

    String name();

    boolean onPacket(Packet packet);

    void onMessage(Text text);

    void onEntityJoinWorld(EntityJoinWorldEvent e);
    void tick();
}
