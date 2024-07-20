package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.event.events.AttackEvent;
import com.heypixel.heypixel.origin.main.Origin;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MixinMultiPlayerGameMode {
    @Inject(method = "attack",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"))
    public void attack(Player arg, Entity arg2, CallbackInfo ci) {
        AttackEvent event = new AttackEvent(arg2);
        Origin.getInstance().getEventManager().call(event);
    }
}
