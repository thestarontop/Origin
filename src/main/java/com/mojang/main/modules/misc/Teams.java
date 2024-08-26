package com.mojang.main.modules.misc;


import com.mojang.main.madebystarontopandfml;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;


import java.util.LinkedList;

public class Teams extends Module {
    public Teams(){super("Teams","bzd", Module.Category.MISC);}
    public static LinkedList<Entity> teammates = new LinkedList<Entity>();



    @EventTarget
    public void onUpdate(UpdateEvent event){


        if (mc.level != null && mc.player != null) {
            for (Entity entity :mc.level.entitiesForRendering()){
                if (entity instanceof AbstractClientPlayer player) {
                    TextColor targetcolor = player.getDisplayName().getStyle().getColor();
                    TextColor selfcolor = mc.player.getDisplayName().getStyle().getColor();
                    if (targetcolor == selfcolor) {
                        if (!teammates.contains(entity)) {
                            teammates.add(entity);
                        }
                    } else {
                        if (teammates.contains(entity)) {
                            teammates.remove(entity);
                        }
                    }
                }
            }
        }
    }
    public static boolean isTeammate(Entity entity){
        return teammates.contains(entity);
    }




    public void onEnable() {
        teammates.clear();
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN +  "Teams Was Enabled");
    }

    public void onDisable() {
        teammates.clear();
        madebystarontopandfml.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED  + "Teams Was Disabled");
    }
}
