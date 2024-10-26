package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer{
    @Inject(method = "isStayingOnGroundSurface",at=@At("RETURN"), cancellable = true)
    protected void isStayingOnGroundSurface(CallbackInfoReturnable<Boolean> cir) {
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("safewalk").isEnabled()){
            cir.setReturnValue(true);
        }
    }
}
