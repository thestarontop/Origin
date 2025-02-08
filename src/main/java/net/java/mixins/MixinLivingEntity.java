package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.event.events.JumpEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity{
    public MixinLivingEntity(EntityType<?> arg, Level arg2) {
        super(null,null);
    }

    @Shadow protected abstract float getJumpPower();

    @Shadow public abstract double getJumpBoostPower();

    @Shadow protected abstract float getFrictionInfluencedSpeed(float f);


    @Shadow protected boolean jumping;

    @Shadow public abstract boolean onClimbable();

    @Inject(method = "push",at=@At("HEAD"), cancellable = true)
    public void push(Entity arg, CallbackInfo ci){
            if (madebystarontopandfml.getInstance().getModuleManager().getModule("nopush").isEnabled()) {
                ci.cancel();
            }
    }

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    protected void jumpFromGround() {
        float yaw = this.getYRot();
        if(this.getId() == Minecraft.getInstance().player.getId()){
            JumpEvent event = new JumpEvent(yaw);
            madebystarontopandfml.getInstance().getEventManager().call(event);
            yaw = event.getYaw();
        }
        double d0 = (double)this.getJumpPower() + this.getJumpBoostPower();
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, d0, vec3.z);

            if (this.isSprinting()) {
                float f = yaw * 0.017453292F;
                this.setDeltaMovement(this.getDeltaMovement().add(-Mth.sin(f) * 0.2f, 0.0, Mth.cos(f) * 0.2f));
            }

        this.hasImpulse = true;
        ForgeHooks.onLivingJump((LivingEntity) Minecraft.getInstance().level.getEntity(getId()));
    }
    @Inject(method = "hasEffect",at=@At("HEAD"),cancellable = true)
    private void hasEffect(MobEffect arg, CallbackInfoReturnable<Boolean> cir){
        Module AntiBlind = madebystarontopandfml.getInstance().getModuleManager().getModule("antiblind");
        if ((arg == MobEffects.CONFUSION || arg == MobEffects.BLINDNESS) && AntiBlind.isEnabled())
            cir.setReturnValue(false);
    }


}
