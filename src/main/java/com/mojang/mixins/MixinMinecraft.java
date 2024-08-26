package com.mojang.mixins;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.TickEvent;
import com.mojang.main.event.events.WorldEvent;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow private int rightClickDelay;




    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;profiler:Lnet/minecraft/util/profiling/ProfilerFiller;",ordinal = 5,shift = At.Shift.BEFORE))
    private void runtick(CallbackInfo ci) {
        TickEvent event = new TickEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }

    @Inject(method = "loadLevel", at = @At("HEAD"))
    public void loadLevel(String string, CallbackInfo ci) {
        WorldEvent event = new WorldEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }

    @Inject(method = "startUseItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", shift = At.Shift.AFTER))
    private void useItem(CallbackInfo ci) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("FastPlace").isEnabled())
            this.rightClickDelay = 0;
    }

}
