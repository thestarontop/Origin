package net.java.mixins;

import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.exploit.Disabler;
import net.java.main.modules.movement.NoSlow;
import net.java.main.modules.movement.Sprint;
import net.java.main.utils.RotationUtils;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AmbientSoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.java.main.utils.MinecraftInstance.mc;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
    @Shadow private boolean wasSprinting;

    @Shadow @Final public ClientPacketListener connection;

    @Shadow private boolean wasShiftKeyDown;

    @Shadow private double xLast;

    @Shadow protected abstract boolean isControlledCamera();

    @Shadow private double yLast1;

    @Shadow private double zLast;

    @Shadow private float yRotLast;

    @Shadow private float xRotLast;

    @Shadow private int positionReminder;

    @Shadow private boolean lastOnGround;

    @Shadow @Final protected Minecraft minecraft;

    @Shadow private boolean autoJumpEnabled;

    @Shadow public int sprintTime;

    @Shadow protected int sprintTriggerTime;

    @Shadow protected abstract void handleNetherPortalClient();

    @Shadow public Input input;

    @Shadow protected abstract boolean hasEnoughImpulseToStartSprinting();

    @Shadow public abstract boolean isMovingSlowly();

    @Shadow private boolean crouching;

    @Shadow private int autoJumpTime;

    @Shadow protected abstract boolean suffocatesAt(BlockPos arg);

    @Shadow private boolean wasFallFlying;

    @Shadow private int waterVisionTime;

    @Shadow public abstract boolean isRidingJumpable();

    @Shadow private int jumpRidingTicks;

    @Shadow private float jumpRidingScale;

    @Shadow public abstract float getJumpRidingScale();

    @Shadow protected abstract void sendRidingJump();

    @Shadow @Final private List<AmbientSoundHandler> ambientSoundHandlers;

    @Shadow public abstract boolean isUsingItem();

    @Shadow public abstract void setSprinting(boolean bl);

    public MixinLocalPlayer(ClientLevel arg, GameProfile gameProfile) {
        super(arg, gameProfile);
    }


    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    private void sendPosition() {
        MotionEvent event = new MotionEvent(this.getX(),this.getY(),this.getZ(),this.getYRot(),this.getXRot(),true,false,this.onGround);
        madebystarontopandfml.getInstance().getEventManager().call(event);
        boolean flag = this.isSprinting();
        if (flag != this.wasSprinting) {
            ServerboundPlayerCommandPacket.Action action = flag ? ServerboundPlayerCommandPacket.Action.START_SPRINTING : ServerboundPlayerCommandPacket.Action.STOP_SPRINTING;
            this.connection.send(new ServerboundPlayerCommandPacket(this, action));
            this.wasSprinting = flag;
        }

        boolean flag3 = this.isShiftKeyDown();
        if (flag3 != this.wasShiftKeyDown) {
            ServerboundPlayerCommandPacket.Action action1 = flag3 ? ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;
            this.connection.send(new ServerboundPlayerCommandPacket(this, action1));
            this.wasShiftKeyDown = flag3;
        }

        if (this.isControlledCamera()) {
            float yaw = this.getYRot();
            float pitch = this.getXRot();
            float yRotLast = RotationUtils.serverRotation.getYaw();
            float xRotLast = RotationUtils.serverRotation.getPitch();
            if (RotationUtils.targetRotation != null) {
                yaw   = RotationUtils.targetRotation.getYaw();
                pitch = RotationUtils.targetRotation.getPitch();
            }
            double dX = event.getX() - this.xLast;
            double dY = event.getY() - this.yLast1;
            double dZ = event.getZ() - this.zLast;
            double dYaw = (double)(yaw - yRotLast);
            double dPitch = (double)(pitch - xRotLast);
            ++this.positionReminder;
            boolean flag1 = dX * dX + dY * dY + dZ * dZ > 9.0E-4 || this.positionReminder >= 20;
            boolean flag2 = dYaw != 0.0 || dPitch != 0.0;



            if (this.isPassenger()) {
                Vec3 vec3 = this.getDeltaMovement();
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(vec3.x, -999.0, vec3.z, event.getYaw(), event.getPitch(), event.getGround()));

            } else if (flag1 && flag2) {
                this.connection.send(new ServerboundMovePlayerPacket.PosRot(event.getX(), event.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.getGround()));

            } else if (flag1) {
                this.connection.send(new ServerboundMovePlayerPacket.Pos(event.getX(), event.getY(), event.getZ(), event.getGround()));

            } else if (flag2) {
                this.connection.send(new ServerboundMovePlayerPacket.Rot(event.getYaw(), event.getPitch(), event.getGround()));

            } else if (this.lastOnGround != this.onGround) {
                this.connection.send(new ServerboundMovePlayerPacket.StatusOnly(event.getGround()));
            }

            if (flag1) {
                this.xLast = event.getX();
                this.yLast1 = event.getY();
                this.zLast = event.getZ();
                this.positionReminder = 0;
            }

            if (flag2) {
                this.yRotLast = event.getYaw();
                this.xRotLast = event.getPitch();
            }

            this.lastOnGround = event.getGround();
            this.autoJumpEnabled = this.minecraft.options.autoJump;
        }
        MotionEvent event1 = new MotionEvent(this.getX(),this.getY(),this.getZ(),this.getYRot(),this.getXRot(),false,true,this.onGround);
        madebystarontopandfml.getInstance().getEventManager().call(event1);
        madebystarontopandfml.getInstance().getEventManager().call(new RotationUpdateEvent());
        }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void aiStep() {
        UpdateEvent event = new UpdateEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
        ++this.sprintTime;
        if (this.sprintTriggerTime > 0) {
            --this.sprintTriggerTime;
        }

        this.handleNetherPortalClient();
        boolean flag = this.input.jumping;
        boolean flag1 = this.input.shiftKeyDown;
        boolean flag2 = this.hasEnoughImpulseToStartSprinting();
        this.crouching = !this.getAbilities().flying && !this.isSwimming() && this.canEnterPose(Pose.CROUCHING) && (this.isShiftKeyDown() || !this.isSleeping() && !this.canEnterPose(Pose.STANDING));
        this.input.tick(this.isMovingSlowly());
        ForgeHooksClient.onMovementInputUpdate(this, this.input);
        this.minecraft.getTutorial().onInput(this.input);
        if (this.isUsingItem() && !this.isPassenger()) {
            SlowDownEvent slowdownevent = new SlowDownEvent(0.2F, 0.2F);
            madebystarontopandfml.getInstance().getEventManager().call(slowdownevent);
            Input var10000 = this.input;
            var10000.leftImpulse *= slowdownevent.getMovementStrafe();
            var10000 = this.input;
            var10000.forwardImpulse *= slowdownevent.getMovementForward();
            if(!madebystarontopandfml.getInstance().getModuleManager().getModule("noslow").isEnabled()) {
                this.sprintTriggerTime = 0;
            }
        }

        boolean flag3 = false;
        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            flag3 = true;
            this.input.jumping = true;
        }

        if (!this.noPhysics) {
            this.moveTowardsClosestSpace(this.getX() - (double)this.getBbWidth() * 0.35, this.getZ() + (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() - (double)this.getBbWidth() * 0.35, this.getZ() - (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + (double)this.getBbWidth() * 0.35, this.getZ() - (double)this.getBbWidth() * 0.35);
            this.moveTowardsClosestSpace(this.getX() + (double)this.getBbWidth() * 0.35, this.getZ() + (double)this.getBbWidth() * 0.35);
        }

        if (flag1) {
            this.sprintTriggerTime = 0;
        }

        boolean flag4 = (float)this.getFoodData().getFoodLevel() > 6.0F || this.getAbilities().mayfly;
            if ((this.onGround || this.isUnderWater()) && !flag1 && !flag2 && this.hasEnoughImpulseToStartSprinting() && !this.isSprinting() && flag4 && (!this.isUsingItem() || NoSlow.shouldnoslow) && !this.hasEffect(MobEffects.BLINDNESS)) {
                if (this.sprintTriggerTime <= 0 && !this.minecraft.options.keySprint.isDown()) {
                    this.sprintTriggerTime = 7;
                } else {
                    if (Sprint.canSprint() || madebystarontopandfml.getInstance().getModuleManager().getModule("safewalk").isEnabled() ) {
                        this.setSprinting(true);
                    }
                }
            }


        if (!this.isSprinting() && (!this.isInWater() || this.isUnderWater()) && this.hasEnoughImpulseToStartSprinting() && flag4 && (!this.isUsingItem() || madebystarontopandfml.getInstance().getModuleManager().getModule("noslow").isEnabled()) && !this.hasEffect(MobEffects.BLINDNESS) && this.minecraft.options.keySprint.isDown()) {
            if (Sprint.canSprint() || madebystarontopandfml.getInstance().getModuleManager().getModule("safewalk").isEnabled()) {
                this.setSprinting(true);
            }
        }

        boolean flag7;
            if (this.isSprinting() && (!madebystarontopandfml.getInstance().getModuleManager().getModule("safewalk").isEnabled())) {
                flag7 = !this.input.hasForwardImpulse() || !flag4;
                boolean flag6 = flag7 || this.horizontalCollision && !this.minorHorizontalCollision || this.isInWater() && !this.isUnderWater();
                if (this.isSwimming()) {
                    if (!this.onGround && !this.input.shiftKeyDown && flag7 || !this.isInWater()) {
                        this.setSprinting(false);
                    }
                } else if (flag6) {
                    this.setSprinting(false);
                }
            }


        flag7 = false;
        if (this.getAbilities().mayfly) {
            if (this.minecraft.gameMode.isAlwaysFlying()) {
                if (!this.getAbilities().flying) {
                    this.getAbilities().flying = true;
                    flag7 = true;
                    this.onUpdateAbilities();
                }
            } else if (!flag && this.input.jumping && !flag3) {
                if (this.jumpTriggerTime == 0) {
                    this.jumpTriggerTime = 7;
                } else if (!this.isSwimming()) {
                    this.getAbilities().flying = !this.getAbilities().flying;
                    flag7 = true;
                    this.onUpdateAbilities();
                    this.jumpTriggerTime = 0;
                }
            }
        }

        if (this.input.jumping && !flag7 && !flag && !this.getAbilities().flying && !this.isPassenger() && !this.onClimbable()) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlot.CHEST);
            if (itemstack.canElytraFly(this) && this.tryToStartFallFlying()) {
                this.connection.send(new ServerboundPlayerCommandPacket(this, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
            }
        }

        this.wasFallFlying = this.isFallFlying();
        if (this.isInWater() && this.input.shiftKeyDown && this.isAffectedByFluids()) {
            this.goDownInWater();
        }

        int j;
        if (this.isEyeInFluid(FluidTags.WATER)) {
            j = this.isSpectator() ? 10 : 1;
            this.waterVisionTime = Mth.clamp(this.waterVisionTime + j, 0, 600);
        } else if (this.waterVisionTime > 0) {
            this.isEyeInFluid(FluidTags.WATER);
            this.waterVisionTime = Mth.clamp(this.waterVisionTime - 10, 0, 600);
        }

        if (this.getAbilities().flying && this.isControlledCamera()) {
            j = 0;
            if (this.input.shiftKeyDown) {
                --j;
            }

            if (this.input.jumping) {
                ++j;
            }

            if (j != 0) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, (double)((float)j * this.getAbilities().getFlyingSpeed() * 3.0F), 0.0));
            }
        }

        if (this.isRidingJumpable()) {
            PlayerRideableJumping playerrideablejumping = (PlayerRideableJumping)this.getVehicle();
            if (this.jumpRidingTicks < 0) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks == 0) {
                    this.jumpRidingScale = 0.0F;
                }
            }

            if (flag && !this.input.jumping) {
                this.jumpRidingTicks = -10;
                playerrideablejumping.onPlayerJump(Mth.floor(this.getJumpRidingScale() * 100.0F));
                this.sendRidingJump();
            } else if (!flag && this.input.jumping) {
                this.jumpRidingTicks = 0;
                this.jumpRidingScale = 0.0F;
            } else if (flag) {
                ++this.jumpRidingTicks;
                if (this.jumpRidingTicks < 10) {
                    this.jumpRidingScale = (float)this.jumpRidingTicks * 0.1F;
                } else {
                    this.jumpRidingScale = 0.8F + 2.0F / (float)(this.jumpRidingTicks - 9) * 0.1F;
                }
            }
        } else {
            this.jumpRidingScale = 0.0F;
        }

        super.aiStep();
        if (this.onGround && this.getAbilities().flying && !this.minecraft.gameMode.isAlwaysFlying()) {
            this.getAbilities().flying = false;
            this.onUpdateAbilities();
        }

    }


    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    private void moveTowardsClosestSpace(double d, double e) {
        MoveEvent event = new MoveEvent(this.getX(),this.getY(),this.getZ());
        madebystarontopandfml.getInstance().getEventManager().call(event);
        BlockPos blockpos = new BlockPos(d, this.getY(), e);
        if(event.isCancelled) return;
        if (this.suffocatesAt(blockpos)) {
            double d0 = d - (double)blockpos.getX();
            double d1 = e - (double)blockpos.getZ();
            Direction direction = null;
            double d2 = Double.MAX_VALUE;
            Direction[] adirection = new Direction[]{Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH};
            Direction[] var14 = adirection;
            int var15 = adirection.length;

            for(int var16 = 0; var16 < var15; ++var16) {
                Direction direction1 = var14[var16];
                double d3 = direction1.getAxis().choose(d0, 0.0, d1);
                double d4 = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 - d3 : d3;
                if (d4 < d2 && !this.suffocatesAt(blockpos.relative(direction1))) {
                    d2 = d4;
                    direction = direction1;
                }
            }

            if (direction != null) {
                Vec3 vec3 = this.getDeltaMovement();
                if (direction.getAxis() == Direction.Axis.X) {
                    this.setDeltaMovement(0.1 * (double)direction.getStepX(), vec3.y, vec3.z);
                } else {
                    this.setDeltaMovement(vec3.x, vec3.y, 0.1 * (double)direction.getStepZ());
                }
            }
        }

    }
    @Inject(method = "suffocatesAt",at = @At("HEAD"), cancellable = true)
    private void onsuffocatesAt(BlockPos arg, CallbackInfoReturnable<Boolean> cir) {
        BlockPushOutEvent event = new BlockPushOutEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
        if (event.isCancelled) {
            cir.setReturnValue(true);
        }
    }


}
