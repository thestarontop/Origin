package net.java.mixins;

import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.StrafeEvent;
import net.java.main.modules.render.ESP;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.java.main.utils.MinecraftInstance.mc;


@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract void setDeltaMovement(Vec3 p_20257_);

    @Shadow public abstract Vec3 getDeltaMovement();

    @Shadow public abstract float getYRot();

    @Shadow
    private static Vec3 getInputVector(Vec3 arg, float g, float h) {
        return null;
    }

    @Shadow public abstract int getId();




    @Redirect(method = "moveRelative",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;getInputVector(Lnet/minecraft/world/phys/Vec3;FF)Lnet/minecraft/world/phys/Vec3;"))
    public Vec3 hookVelocity(Vec3 movementInput, float speed, float yaw) {
        if(this.getId() == Minecraft.getInstance().player.getId()) {
            StrafeEvent event = new StrafeEvent(movementInput, speed, this.getYRot(), getInputVector(movementInput, speed, yaw));
            madebystarontopandfml.getInstance().getEventManager().call(event);
            return event.getVelocity();
        }
        return MixinEntity.getInputVector(movementInput,speed,yaw);
    }
    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("ESP").isEnabled() && ESP.mode.getValue().equals("Glow")) {
            cir.setReturnValue(true);
        }
    }
}
