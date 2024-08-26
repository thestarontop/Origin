package com.mojang.main.modules.world;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.command.ChatManager;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.modules.Module;
import com.mojang.mixins.acesser.MinecraftAcesser;
import net.minecraft.ChatFormatting;

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
        madebystarontopandfml.getInstance().getEventManager().register(this);
        ChatManager.sendHotBarChat(ChatFormatting.GREEN + "Timer Was Disabled");
        ((MinecraftAcesser)mc).setTimer(new net.minecraft.client.Timer(20,0));
    }
}
