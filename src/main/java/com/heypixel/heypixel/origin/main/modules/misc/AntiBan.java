package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

public class AntiBan extends Module {
    public AntiBan(){super("AntiBan","bzd",Category.MISC);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundChatPacket packet){
            if (packet.getMessage().getString().equals("§d§l布吉岛 §7| §c一名违规玩家§4§l" + mc.player.getDisplayName().getString() + "§c被§5§l虚空娘§c吞掉了！§r")){
                mc.getConnection().send(new ServerboundChatPacket("/hub"));
            }
        }
    }
    //§d§l布吉岛 §7| §c一名违规玩家§4§l星_FhxtsvF§c被§5§l虚空娘§c吞掉了！§r
}
