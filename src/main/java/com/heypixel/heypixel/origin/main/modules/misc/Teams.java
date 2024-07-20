package com.heypixel.heypixel.origin.main.modules.misc;


import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;


import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

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
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN +  "Teams Was Enabled");
    }

    public void onDisable() {
        teammates.clear();
        Origin.getInstance().getEventManager().unregister(this);
        ChatManager.sendHotBarChat(ChatFormatting.RED  + "Teams Was Disabled");
    }
}
