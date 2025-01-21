package net.java.main.modules.render;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.TextEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.RenderUtils;
import net.java.mixins.acesser.ClientboundChatPacketAcesser;
import net.java.mixins.acesser.ClientboundSetTitleTextPacketAcesser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;


public class NameProtect extends Module {
    public NameProtect(){super("NameProtect","bzd",Category.MISC);}
    String name = null;
    @EventTarget
    public void onText(TextEvent event){
        event.getText().replaceAll(name,ColorUtils.makeColour("Hidden"));
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
       if (name == null && mc.player != null){
            name = mc.player.getDisplayName().getString();
       }
    }
}
