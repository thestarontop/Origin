package com.mojang.main.modules.combat;

import com.mojang.main.event.events.AttackEvent;
import com.mojang.main.event.events.UpdateEvent;
import com.mojang.main.modules.Module;
import com.mojang.main.event.annotations.EventTarget;
import com.mojang.main.event.events.PacketEvent;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;


public class SuperKnockBack extends Module {
    public SuperKnockBack() {
        super("SuperKnockBack","bzd", Module.Category.COMBAT);
    }
    private boolean sprinting;
    private boolean needstop;
    @EventTarget
    public void onAttack(AttackEvent event) {
        if (mc.level == null || mc.player == null || event.getEntity() == null)return;
        if (!sprinting) {
            needstop = true;
            mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
        }
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
        mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player,ServerboundPlayerCommandPacket.Action.START_SPRINTING));
    }
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundPlayerCommandPacket packet){
            if (packet.getAction() == ServerboundPlayerCommandPacket.Action.START_SPRINTING){
                sprinting = true;
            }
            if (packet.getAction() == ServerboundPlayerCommandPacket.Action.STOP_SPRINTING){
                sprinting = false;
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (sprinting && needstop){
            if (mc.options.keyLeft.isDown() || mc.options.keyRight.isDown() || mc.options.keyDown.isDown()){
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                needstop = false;
            }else{
                if (mc.options.keyUp.isDown()) {
                    needstop = false;
                }
            }
        }
    }
}

