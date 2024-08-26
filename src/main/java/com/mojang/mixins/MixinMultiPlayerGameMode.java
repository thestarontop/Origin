package com.mojang.mixins;

import com.mojang.main.madebystarontopandfml;
import com.mojang.main.event.events.AttackEvent;
import com.mojang.main.event.events.ClickBlockEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode {
    @Shadow @Final private Minecraft minecraft;

    @Shadow private GameType localPlayerMode;

    @Shadow protected abstract void sendBlockAction(ServerboundPlayerActionPacket.Action arg, BlockPos arg2, Direction arg3);

    @Shadow public abstract boolean destroyBlock(BlockPos arg);

    @Shadow private int destroyDelay;

    @Shadow private boolean isDestroying;

    @Shadow protected abstract boolean sameDestroyTarget(BlockPos arg);

    @Shadow private BlockPos destroyBlockPos;

    @Shadow private float destroyProgress;

    @Shadow private ItemStack destroyingItem;

    @Shadow private float destroyTicks;

    @Shadow protected abstract void ensureHasSentCarriedItem();

    @Inject(method = "attack",at=@At(value = "INVOKE",target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;ensureHasSentCarriedItem()V"))
    public void attack(Player arg, Entity arg2, CallbackInfo ci) {
        madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(arg2));
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public boolean startDestroyBlock(BlockPos arg, Direction arg2) {
        if (this.minecraft.player.blockActionRestricted(this.minecraft.level, arg, this.localPlayerMode)) {
            return false;
        } else if (!this.minecraft.level.getWorldBorder().isWithinBounds(arg)) {
            return false;
        } else {
            if (this.localPlayerMode.isCreative()) {
                BlockState blockstate = this.minecraft.level.getBlockState(arg);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, arg, blockstate, 1.0F);
                this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, arg, arg2);
                if (!ForgeHooks.onLeftClickBlock(this.minecraft.player, arg, arg2).isCanceled()) {
                    this.destroyBlock(arg);
                }

                this.destroyDelay = 5;
            } else if (!this.isDestroying || !this.sameDestroyTarget(arg)) {
                if (this.isDestroying) {
                    this.sendBlockAction(ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK, this.destroyBlockPos, arg2);
                }

                PlayerInteractEvent.LeftClickBlock event = ForgeHooks.onLeftClickBlock(this.minecraft.player, arg, arg2);
                madebystarontopandfml.getInstance().getEventManager().call(new ClickBlockEvent(arg));
                BlockState blockstate1 = this.minecraft.level.getBlockState(arg);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, arg, blockstate1, 0.0F);
                this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, arg, arg2);
                boolean flag = !blockstate1.isAir();
                if (flag && this.destroyProgress == 0.0F && event.getUseBlock() != Event.Result.DENY) {
                    blockstate1.attack(this.minecraft.level, arg, this.minecraft.player);
                }

                if (event.getUseItem() == Event.Result.DENY) {
                    return true;
                }

                if (flag && blockstate1.getDestroyProgress(this.minecraft.player, this.minecraft.player.level, arg) >= 1.0F) {
                    this.destroyBlock(arg);
                } else {
                    this.isDestroying = true;
                    this.destroyBlockPos = arg;
                    this.destroyingItem = this.minecraft.player.getMainHandItem();
                    this.destroyProgress = 0.0F;
                    this.destroyTicks = 0.0F;
                    this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, (int)(this.destroyProgress * 10.0F) - 1);
                }
            }

            return true;
        }
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public boolean continueDestroyBlock(BlockPos arg, Direction arg2) {
        this.ensureHasSentCarriedItem();
        if (this.destroyDelay > 0) {
            --this.destroyDelay;
            return true;
        } else {
            BlockState blockstate;
            if (this.localPlayerMode.isCreative() && this.minecraft.level.getWorldBorder().isWithinBounds(arg)) {
                this.destroyDelay = 5;
                blockstate = this.minecraft.level.getBlockState(arg);
                this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, arg, blockstate, 1.0F);
                this.sendBlockAction(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, arg, arg2);
                if (!ForgeHooks.onLeftClickBlock(this.minecraft.player, arg, arg2).isCanceled()) {
                    this.destroyBlock(arg);
                }

                return true;
            } else if (this.sameDestroyTarget(arg)) {
                blockstate = this.minecraft.level.getBlockState(arg);
                if (blockstate.isAir()) {
                    this.isDestroying = false;
                    return false;
                } else {
                    this.destroyProgress += blockstate.getDestroyProgress(this.minecraft.player, this.minecraft.player.level, arg);
                    if (this.destroyTicks % 4.0F == 0.0F) {
                        SoundType soundtype = blockstate.getSoundType(this.minecraft.level, arg, this.minecraft.player);
                        this.minecraft.getSoundManager().play(new SimpleSoundInstance(soundtype.getHitSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 8.0F, soundtype.getPitch() * 0.5F, arg));
                    }

                    ++this.destroyTicks;
                    this.minecraft.getTutorial().onDestroyBlock(this.minecraft.level, arg, blockstate, Mth.clamp(this.destroyProgress, 0.0F, 1.0F));
                    madebystarontopandfml.getInstance().getEventManager().call(new ClickBlockEvent(arg));
                    if (ForgeHooks.onLeftClickBlock(this.minecraft.player, arg, arg2).getUseItem() == Event.Result.DENY) {
                        return true;
                    } else {
                        if (this.destroyProgress >= 1.0F) {
                            this.isDestroying = false;
                            this.sendBlockAction(ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK, arg, arg2);
                            this.destroyBlock(arg);
                            this.destroyProgress = 0.0F;
                            this.destroyTicks = 0.0F;
                            this.destroyDelay = 5;
                        }

                        this.minecraft.level.destroyBlockProgress(this.minecraft.player.getId(), this.destroyBlockPos, (int)(this.destroyProgress * 10.0F) - 1);
                        return true;
                    }
                }
            } else {
                return this.startDestroyBlock(arg, arg2);
            }
        }
    }
}
