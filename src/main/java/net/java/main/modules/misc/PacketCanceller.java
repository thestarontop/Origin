package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.java.main.value.BooleanValue;

public class PacketCanceller extends Module {
    public PacketCanceller(){super("PacketCanceller","bzd",Category.MISC);
    this.addValues(c,s);}
    public static BooleanValue c = new BooleanValue("CPacket",true);
    public static BooleanValue s = new BooleanValue("SPacket",true);
    @EventTarget
    public void onPacket(PacketEvent event){
        if (PacketUtils.isCPacket(event.getPacket()) && c.getValue()){
            event.cancelEvent();
        }
        if (PacketUtils.isSPacket(event.getPacket()) && s.getValue()){
            event.cancelEvent();
        }
    }
}
