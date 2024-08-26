package com.mojang.main.modules.misc;

import com.mojang.main.event.events.AttackEvent;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import com.mojang.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.LinkedList;

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
