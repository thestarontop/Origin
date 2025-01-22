package net.java.main.modules.render;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.TextEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.RenderUtils;
import net.java.mixins.acesser.ClientboundChatPacketAcesser;
import net.java.mixins.acesser.ClientboundSetScorePacketAcesser;
import net.java.mixins.acesser.ClientboundSetTitleTextPacketAcesser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;


public class NameProtect extends Module {
    public NameProtect(){super("NameProtect","bzd",Category.MISC);}
    String name = null;
    @EventTarget
    public void onText(TextEvent event){
        event.setText(event.getText().replaceAll(name,ColorUtils.makeColour("Hidden")));
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
       if (name == null && mc.player != null){
            name = mc.player.getDisplayName().getString();
           ChatManager.sendChat("玩家名字："+ name);
       }
       if (name != null){
           mc.getConnection().getPlayerInfo(mc.player.getUUID()).setTabListDisplayName(new TextComponent(mc.getConnection().getPlayerInfo(mc.player.getUUID()).getTabListDisplayName().getString().replaceAll(name,ColorUtils.makeColour("Hidden"))));

       }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (name == null) return;
        if (event.getPacket() instanceof ClientboundChatPacket packet){
            ClientboundChatPacketAcesser packet1 = (ClientboundChatPacketAcesser) packet;
            packet1.setmessage(new TextComponent(packet.getMessage().getString().replaceAll(name,ColorUtils.makeColour("Hidden"))));
        }
        if (event.getPacket() instanceof ClientboundSetScorePacket packet){
            ClientboundSetScorePacketAcesser packet1 = (ClientboundSetScorePacketAcesser) packet;
            packet1.setObjectiveName(packet.getObjectiveName().replaceAll(name,ColorUtils.makeColour("Hidden")));
        }
    }
}
