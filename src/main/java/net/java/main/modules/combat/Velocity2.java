package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.value.BooleanValue;
import net.java.mixins.acesser.ClientboundSetEntityMotionPacketAcesser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class Velocity2 extends Module {
    public Velocity2(){super("Velocity2","bzd",Category.COMBAT);
    this.addValues(x,y,z);}
    public BooleanValue x = new BooleanValue("x",true);
    public BooleanValue y = new BooleanValue("y",true);
    public BooleanValue z = new BooleanValue("z",true);
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundSetEntityMotionPacket packet && packet.getId() == mc.player.getId()){
            event.cancelEvent();
        }
    }
}
