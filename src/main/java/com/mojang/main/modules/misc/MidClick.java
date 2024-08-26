package com.mojang.main.modules.misc;

import com.mojang.main.command.ChatManager;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.Render2DEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.LinkedList;


public class MidClick extends Module {
    public MidClick(){super("MidClick","bzd",Category.MISC);}
    private boolean wasDown = false;
    public static LinkedList<String> friend = new LinkedList<String>();
    @EventTarget
    public void onRender2D(Render2DEvent event){
        if (mc.screen != null) return;

        if (!wasDown && mc.mouseHandler.isMiddlePressed()){
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult)mc.hitResult).getEntity();
                if (entity instanceof Player player) {
                    if (!friend.contains(player.getDisplayName().getString())) {
                        friend.add(player.getDisplayName().getString());
                    } else {
                        friend.remove(player.getDisplayName().getString());
                    }
                }else{
                    ChatManager.sendHotBarChat(ChatFormatting.RED + "You should select humans to add friends");
                }
            }
        }
        wasDown = mc.mouseHandler.isMiddlePressed();
    }
    public static boolean isFriend(Player entity){
        return friend.contains(entity.getDisplayName().getString());
    }
}
