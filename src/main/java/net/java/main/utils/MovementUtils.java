package net.java.main.utils;


import net.java.main.modules.movement.Sprint;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;

public class MovementUtils extends MinecraftInstance{

    public static final double WALK_SPEED = 0.221;
    public static final double MOD_SPRINTING = 1.3F;
    public static final double MOD_WEB = 0.105 / WALK_SPEED;
    public static final double MOD_SWIM = 0.115F / WALK_SPEED;
    public static final double MOD_SNEAK = 0.3F;
    public static final double[] MOD_DEPTH_STRIDER = {
            1.0F,
            0.1645F / MOD_SWIM / WALK_SPEED,
            0.1995F / MOD_SWIM / WALK_SPEED,
            1.0F / MOD_SWIM,
    };

    public static void strafe(float speed) {
        if (!isMoving()) return;
        double yaw = RotationUtils.movingYaw();
        mc.player.setDeltaMovement(-Math.sin(yaw) * speed,mc.player.getDeltaMovement().y,Math.cos(yaw) * speed);
    }
    public static boolean isMoving(){
        return mc.player.input.up || mc.player.input.down || mc.player.input.left || mc.player.input.right;
    }

    public static boolean jumpDown() {
        return mc.options.keyJump.isDown();
    }


    public static double getAllowedHorizontalDistance() {
        double horizontalDistance;
        boolean useBaseModifiers = false;

        if (mc.player == null) {
            return 0.0; // 如果玩家为空，返回 0
        }

        // 判断玩家是否在蜘蛛网中
        if (isInWeb(mc.player)) {
            horizontalDistance = MOD_WEB * WALK_SPEED;

            // 判断玩家是否在水中
        } else if (mc.player.isInWater()) {
            horizontalDistance = MOD_SWIM * WALK_SPEED;

/*            int depthStriderLevel = depthStriderLevel(mc.player);
            if (depthStriderLevel > 0) {
                horizontalDistance *= MOD_DEPTH_STRIDER[depthStriderLevel];
                useBaseModifiers = true;
            }*/

            // 判断玩家是否潜行
        } else if (mc.player.isCrouching()) {
            horizontalDistance = MOD_SNEAK * WALK_SPEED;

            // 默认情况下
        } else {
            horizontalDistance = WALK_SPEED;
            useBaseModifiers = true;
        }

        // 应用基础修正
        if (useBaseModifiers) {
            if (Sprint.canSprint()) {
                horizontalDistance *= MOD_SPRINTING;
            }

            // 药水效果：速度提升
            if (mc.player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                MobEffectInstance speedEffect = mc.player.getEffect(MobEffects.MOVEMENT_SPEED);
                if (speedEffect != null && speedEffect.getDuration() > 0) {
                    horizontalDistance *= 1 + (0.2 * (speedEffect.getAmplifier() + 1));
                }
            }

            // 药水效果：缓慢
            if (mc.player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN)) {
                horizontalDistance = 0.29; // 缓慢效果直接限制移动速度
            }
        }

        return horizontalDistance;
    }
    private static boolean isInWeb(Player player) {
        // 检查玩家当前站立的方块是否是蜘蛛网
        return mc.player.level.getBlockState(player.blockPosition()).getBlock() == Blocks.COBWEB;
    }
    public static int getJumpEffect() {
        // 检查玩家是否拥有跳跃提升效果
        if (mc.player != null && mc.player.hasEffect(MobEffects.JUMP)) {
            // 获取跳跃提升效果的实例
            MobEffectInstance jumpEffect = mc.player.getEffect(MobEffects.JUMP);
            if (jumpEffect != null) {
                // 返回效果等级 + 1（因为等级是从 0 开始计数的）
                return jumpEffect.getAmplifier() + 1;
            }
        }
        // 如果玩家没有跳跃提升效果，则返回 0
        return 0;
    }

}
