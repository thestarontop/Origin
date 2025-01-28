package net.java.main.modules.misc;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.java.main.utils.RandomUtils;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.Random;


public class AutoL extends Module {
    public AutoL(){super("AutoL","bzd",Category.MISC);}
    List<String> list = List.of("Get Good Get N@ven Client","Get Good Get XiChenQi Client","Get Good Get SouthSide Client","Get Good Get Styles Client","Get Good Get Cheart Client","Get Good Get Nitro Client","Get Good Get Zen Client","Get Good Get Shove Client","Get Good Get Faith Client","Get Good Get Shove Client","Get Good Get Augustus Client","Get Good Get FDP Client");
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (KillAura.target == null) return;
        if (KillAura.target instanceof Player target){
            if (target.isDeadOrDying()){

                mc.getConnection().send(new ServerboundChatPacket("@ "+getRandomItem(list)+" "+target.getDisplayName().getString()));
            }
        }
    }
    public static String getRandomItem(List<String> list) {
        Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }
}
