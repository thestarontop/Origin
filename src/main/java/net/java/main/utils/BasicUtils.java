package net.java.main.utils;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.ClientStartEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.ArrayList;
import java.util.List;

public class BasicUtils extends MinecraftInstance{

    public BasicUtils() {
        madebystarontopandfml.getInstance().getEventManager().register(this);
    }
    @EventTarget
    public void onClientStart(ClientStartEvent event){
        List<IModInfo> modlist = new ArrayList<>();

        for (IModInfo modInfo : ModList.get().getMods()) {
            if (modInfo.getModId().equals("madebystarontopandfml")) {
                modlist.add(modInfo);
            }
        }

        ModList.get().getMods().removeAll(modlist);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        mc.getWindow().setTitle(madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION+"-布吉岛");
        if (mc.player.getItemInHand(InteractionHand.MAIN_HAND) != ItemStack.EMPTY){
            mc.player.getItemInHand(InteractionHand.MAIN_HAND).setHoverName(new TextComponent(ColorUtils.makeColour("Origin-Owner : starontop")));
        }
    }
}
