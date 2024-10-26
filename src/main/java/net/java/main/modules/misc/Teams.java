package net.java.main.modules.misc;


import net.java.main.madebystarontopandfml;
import net.java.main.command.ChatManager;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;


import java.util.LinkedList;

public class Teams extends Module {
    public Teams(){super("Teams","bzd", Module.Category.MISC);}
    public static LinkedList<Entity> teammates = new LinkedList<>();



    @EventTarget
    public void onUpdate(UpdateEvent event){


        if (mc.level != null && mc.player != null) {
            for (Entity entity :mc.level.entitiesForRendering()){
                if (entity instanceof AbstractClientPlayer player) {
                    String targetname = player.getDisplayName().getString();
                    String selfname = mc.player.getDisplayName().getString();
                    if (targetname.length() < 3 || selfname.length() < 3) {
                        continue;
                    }
                    if (targetname.substring(0, 3).equals(selfname.substring(0, 3))) {
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
        super.onEnable();
    }

    public void onDisable() {
        teammates.clear();
        super.onDisable();
    }
}
