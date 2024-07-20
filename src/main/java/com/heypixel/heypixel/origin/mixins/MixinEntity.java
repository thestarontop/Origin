package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.event.events.StrafeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(Entity.class)
public abstract class MixinEntity {


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
            Origin.getInstance().getEventManager().call(event);
            return event.getVelocity();
        }
        return MixinEntity.getInputVector(movementInput,speed,yaw);
    }
}
