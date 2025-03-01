package net.java.main.modules.misc;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.TextEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.RenderUtils;
import net.java.main.utils.StringUtils;
import net.java.mixins.acesser.ClientboundChatPacketAcesser;
import net.java.mixins.acesser.ClientboundSetScorePacketAcesser;
import net.java.mixins.acesser.ClientboundSetSubtitleTextPacketAcesser;
import net.java.mixins.acesser.ClientboundSetTitleTextPacketAcesser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.*;


public class NameProtect extends Module {
    public NameProtect(){super("NameProtect","bzd",Category.MISC);}
    public static String name = null;
    @EventTarget
    public void onText(TextEvent event){
        if (name == null) return;
        event.setText(StringUtils.replace(event.getText(),name,ColorUtils.makeColour("Hidden")+"§f"));
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundChatPacket packet){
            ClientboundChatPacketAcesser packet1 = (ClientboundChatPacketAcesser) packet;
            packet1.setmessage(new TextComponent(StringUtils.replace(packet.getMessage().getString(),name,ColorUtils.makeColour("Hidden")+"§f")));
        }
        if (event.getPacket() instanceof ClientboundSetTitleTextPacket packet){
            ClientboundSetTitleTextPacketAcesser packet1 = (ClientboundSetTitleTextPacketAcesser) packet;
            packet1.setmessage(new TextComponent(StringUtils.replace(packet.getText().getString(),name,ColorUtils.makeColour("Hidden")+"§f")));
        }
        if (event.getPacket() instanceof ClientboundSetSubtitleTextPacket packet){
            ClientboundSetSubtitleTextPacketAcesser packet1 = (ClientboundSetSubtitleTextPacketAcesser) packet;
            packet1.setmessage(new TextComponent(StringUtils.replace(packet.getText().getString(),name,ColorUtils.makeColour("Hidden")+"§f")));
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
       if (name == null && mc.player != null){
           name = StringUtils.replace(mc.player.getDisplayName().getString()," ","");
       }

    }
}
