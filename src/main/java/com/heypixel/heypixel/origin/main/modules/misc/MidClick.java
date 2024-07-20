package com.heypixel.heypixel.origin.main.modules.misc;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.Render2DEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.ScreenEvent;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;


public class MidClick extends Module {
    public MidClick(){super("MidClick","bzd",Category.MISC);}
    private boolean wasDown = false;
    public static LinkedList<Player> friend = new LinkedList<Player>();
    @EventTarget
    public void onRender2D(Render2DEvent event){
        if (mc.screen != null) return;

        if (!wasDown && mc.mouseHandler.isMiddlePressed()){
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult)mc.hitResult).getEntity();
                if (entity instanceof Player player) {
                    if (!friend.contains(player)) {
                        friend.add(player);
                    } else {
                        friend.remove(player);
                    }
                }else{
                    //mc.player.sendMessage(Component.nullToEmpty("You should select humans to add friends"),mc.player.getUUID());
                    ChatManager.sendHotBarChat(ChatFormatting.RED + "You should select humans to add friends");
                }
            }
        }
        wasDown = mc.mouseHandler.isMiddlePressed();
    }
    public static boolean isFriend(Player entity){
        return friend.contains(entity);
    }
}
