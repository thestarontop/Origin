package net.java.main.modules.movement;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.value.ListValue;
import net.java.mixins.acesser.ServerboundMovePlayerPacketAcesser;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Fly extends Module {
    public Fly(){
        super("Fly","Creatively",Category.MOVEMENT);
        addValues(mode);
    }
    public ListValue mode=new ListValue("Mode",new String[]{"OldGrim","Creative"},"Grim");
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (mode.getValue() == "Creative") {
            mc.player.getAbilities().mayfly = true;
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (mode.getValue() == "Creative") {
            if (event.getPacket() instanceof ServerboundMovePlayerPacket) {
                ServerboundMovePlayerPacketAcesser packet = (ServerboundMovePlayerPacketAcesser) event.getPacket();
                packet.setOnGround(false);
            }
        }
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        if (mode.getValue() == "OldGrim") {
            if (event.getPre()) {
                mc.player.setPos(mc.player.getX() + 1000, mc.player.getY(), mc.player.getZ());
            } else {
                mc.player.setPos(mc.player.getX() - 1000, mc.player.getY(), mc.player.getZ());
            }
        }
    }
}
