package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.event.events.TickEvent;
import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.WorldEvent;
import com.heypixel.heypixel.origin.main.modules.player.FastPlace;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft  {

    @Shadow private int rightClickDelay;

    @Inject(method = "runTick",at=@At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 0))
    private void runtick(boolean bl ,CallbackInfo ci){
        TickEvent event = new TickEvent();
        Origin.getInstance().getEventManager().call(event);
    }
    @Inject(method = "loadLevel",at=@At("HEAD"))
    public void loadLevel(String string, CallbackInfo ci) {
        WorldEvent event = new WorldEvent();
        Origin.getInstance().getEventManager().call(event);
    }
    @Inject(method = "startUseItem",at = @At(value = "FIELD",target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I"))
    private void startUseItem(CallbackInfo ci) {
        if (Origin.getInstance().getModuleManager().getModule("FastPlace").isEnabled())
            rightClickDelay = 0;
    }
}
