package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class FastAnchor extends Module {
    public FastAnchor(){super("FastAnchor","bzd",Category.COMBAT);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundUseItemOnPacket packet){
            if (mc.player.getItemInHand(packet.getHand()).getItem() == Items.RESPAWN_ANCHOR){
                int slot = -1;
                for (int i = 0; i < 9; i++) {
                    ItemStack itemstack = mc.player.inventoryMenu.getSlot(i + 36).getItem();
                    Item item =itemstack.getItem();
                    if (item == Items.GLOWSTONE) {
                        slot = i;
                    }
                }
                if (slot == -1) return;
                BlockPos blockpos = packet.getHitResult().getBlockPos().relative(packet.getHitResult().getDirection());
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(slot));
                PacketUtils.sendPacketNoEvent(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND,new BlockHitResult(new Vec3(blockpos.getX(),blockpos.getY(),blockpos.getZ()),getBlockFace(mc.player,blockpos),blockpos,false)));
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            }
        }
    }
    public static Direction getBlockFace(Player player, BlockPos pos) {
        // 获取玩家视线的射线追踪结果
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1.0F);
        double reach = 4.5; // 通常是 4.5 格
        Vec3 endVec = eyePos.add(lookVec.x * reach, lookVec.y * reach, lookVec.z * reach);

        // 进行射线追踪
        BlockHitResult hitResult = player.level.clip(new ClipContext(
                eyePos,
                endVec,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        // 如果击中的是目标方块，返回击中的面
        if (hitResult.getType() == HitResult.Type.BLOCK &&
                hitResult.getBlockPos().equals(pos)) {
            return hitResult.getDirection();
        }

        return null;
    }
}
