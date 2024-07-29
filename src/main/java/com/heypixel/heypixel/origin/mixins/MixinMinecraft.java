package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.event.events.TickEvent;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.WorldEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow private int rightClickDelay;

    @Shadow @Final private Timer timer;



    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void runtick(boolean bl, CallbackInfo ci) {
        TickEvent event = new TickEvent();
        Origin.getInstance().getEventManager().call(event);
    }

    @Inject(method = "loadLevel", at = @At("HEAD"))
    public void loadLevel(String string, CallbackInfo ci) {
        WorldEvent event = new WorldEvent();
        Origin.getInstance().getEventManager().call(event);
    }

    @Inject(method = "startUseItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", shift = At.Shift.AFTER))
    private void useItem(CallbackInfo ci) {
        if (Origin.getInstance().getModuleManager().getModule("FastPlace").isEnabled())
            this.rightClickDelay = 0;
    }

}
