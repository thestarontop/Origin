package net.java.main.modules.render;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.TextEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.mixins.acesser.ClientboundChatPacketAcesser;
import net.java.mixins.acesser.ClientboundSetTitleTextPacketAcesser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;


public class NameProtect extends Module {
    public NameProtect(){super("NameProtect","bzd",Category.MISC);}
    @EventTarget
    public void onText(TextEvent event){
        event.getText().replace(mc.player.getDisplayName().getString(),"Hidden");
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getConnection().getPlayerInfo(mc.player.getUUID()).setTabListDisplayName(Component.nullToEmpty("Hidden"));

    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundChatPacket packet1){
            ClientboundChatPacketAcesser packet = (ClientboundChatPacketAcesser) event.getPacket();
            Component newmessage =new TextComponent(packet1.getMessage().getString().replaceAll(mc.player.getDisplayName().getString(),"Hidden"));
            packet.setmessage(newmessage);
        }
    }
}
