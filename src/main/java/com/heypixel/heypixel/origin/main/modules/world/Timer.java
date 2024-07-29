package com.heypixel.heypixel.origin.main.modules.world;

import com.heypixel.heypixel.origin.main.Commonds.ChatManager;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.annotations.EventTarget;
import com.heypixel.heypixel.origin.main.event.events.UpdateEvent;
import com.heypixel.heypixel.origin.main.modules.Module;
import com.heypixel.heypixel.origin.mixins.acesser.MinecraftAcesser;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

public class Timer extends Module {
    public Timer(){super("Timer","bzd",Category.WORLD);}
    @EventTarget
    public void onUpdate(UpdateEvent event){
        // value * 3 = input value;
        // just like timer 3.0 need input 3.0*60
        ((MinecraftAcesser)mc).setTimer(new net.minecraft.client.Timer(60,0));
    }
    @Override
    public void onDisable(){
        Origin.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Timer Was Disabled");
        ((MinecraftAcesser)mc).setTimer(new net.minecraft.client.Timer(20,0));
    }
}
