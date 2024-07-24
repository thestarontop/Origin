package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.AttackEvent;
import com.heypixel.heypixel.origin.main.event.events.PacketEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.main.utils.PacketUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoReport extends Module {
    public AutoReport(){super("AutoReport","bzd", Module.Category.MISC);}
    boolean reported;
    LinkedList<String> reportedlist = new LinkedList<>();
    @EventTarget
    public void onAttack(AttackEvent event){
        if (!reportedlist.contains(event.getEntity().getDisplayName().getString())){
            mc.getConnection().send(new ServerboundChatPacket("/report "+event.getEntity().getDisplayName().getString()));
            reported = true;
        }
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundOpenScreenPacket packet){
            if (reported){
                mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(),-14,0,0, ClickType.PICKUP,new ItemStack(Items.AIR),Int2ObjectMaps.emptyMap()));
                reported = false;
            }
        }
    }
}
