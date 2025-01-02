package net.java.main.modules.misc;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.AttackEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.utils.BlockUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.java.main.modules.combat.KillAura.isInIterable;

public class AutoPartyGame extends Module {
    public AutoPartyGame(){super("AutoPartyGame","bzd",Category.MISC);}
    Block currentitem = null;

    Map<String, Block> FLORA_MAP = new HashMap<String, Block>() {{
        // 小型花
        put("蒲公英", Blocks.DANDELION);
        put("罂粟", Blocks.POPPY);
        put("蓝色兰花", Blocks.BLUE_ORCHID);
        put("绒球葱", Blocks.ALLIUM);
        put("蓝矢车菊", Blocks.CORNFLOWER);
        put("雏菊", Blocks.OXEYE_DAISY);
        put("铃兰", Blocks.LILY_OF_THE_VALLEY);
        put("凋零玫瑰", Blocks.WITHER_ROSE);
        put("红色郁金香", Blocks.RED_TULIP);
        put("橙色郁金香", Blocks.ORANGE_TULIP);
        put("白色郁金香", Blocks.WHITE_TULIP);
        put("粉色郁金香", Blocks.PINK_TULIP);
        put("菊花", Blocks.AZURE_BLUET);   // 新增

        // 大型花
        put("向日葵", Blocks.SUNFLOWER);
        put("丁香", Blocks.LILAC);
        put("玫瑰丛", Blocks.ROSE_BUSH);
        put("牡丹", Blocks.PEONY);

        // 树苗
        put("橡树树苗", Blocks.OAK_SAPLING);
        put("云杉树苗", Blocks.SPRUCE_SAPLING);
        put("白桦树苗", Blocks.BIRCH_SAPLING);
        put("丛林树苗", Blocks.JUNGLE_SAPLING);
        put("金合欢树苗", Blocks.ACACIA_SAPLING);
        put("深色橡树树苗", Blocks.DARK_OAK_SAPLING);
    }};
    int delay = 1;
    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundSetSubtitleTextPacket packet){
            List<Block> Blocks = findMatchingBlocks(packet.getText().getString());
            for (Block item : Blocks) {
                currentitem = item;
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        var BlockMap = BlockUtils.searchBlocks(4);
        for (Map.Entry<BlockPos, Block> entry : BlockMap.entrySet()) {
            BlockPos key = entry.getKey();
            Block value = entry.getValue();
            if (currentitem != null) {
                    if (value == currentitem) {
                        mc.getConnection().send(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK, key, Direction.DOWN));
                        currentitem = null;
                        return;
                    }
            }
            if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.FLINT_AND_STEEL) {
                if (value == Blocks.TNT) {
                    mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()), Direction.DOWN, key, false)));
                    return;
                }
            }
            if (value == Blocks.STONE_BUTTON){
                mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()), Direction.DOWN, key, false)));
            }
        }
        Iterable<Entity> entitylist = mc.level.entitiesForRendering();
        for (Entity entity1 : entitylist){
            if(entity1 instanceof EndCrystal){
                boolean canReach = mc.player.distanceTo(entity1) <= 5;
                if (canReach){
                    attackEntity(entity1);
                }
            }

            boolean canReach = mc.player.distanceTo(entity1) <= 5;

            if (canReach && isEnemy(entity1) && entity1.getId() != mc.player.getId()) {
                if (!CanReachEntities.contains(entity1)) {
                    CanReachEntities.add(entity1);
                }
            } else {
                if (CanReachEntities.contains(entity1)) {
                    CanReachEntities.remove(entity1);
                }
            }
            if (entity1.isRemoved()) {
                if (CanReachEntities.contains(entity1)) {
                    CanReachEntities.remove(entity1);
                }
            }


        }
        CanReachEntities.removeIf(entity -> !isInIterable(entity, entitylist));


        if (!CanReachEntities.isEmpty()){
            CanReachEntities.sort((e1, e2) -> {
                double dist1 = mc.player.distanceTo(e1);
                double dist2 = mc.player.distanceTo(e2);
                return Double.compare(dist1, dist2);
            });
        }
        for (Entity entity: CanReachEntities) {
                if (AttackedEntities.contains(entity))
                    continue;

            if (entity instanceof LivingEntity) {
                   attackEntity(entity);
                   AttackedEntities.add(entity);
                   break;
            }
        }



        if(AttackedEntities.size() >= CanReachEntities.size()){
            AttackedEntities.clear();
        }
    }
    public List<Block> findMatchingBlocks(String input) {
        List<Block> matchingBlocks = new ArrayList<>();
        boolean hasDeepOak = false;

        // 第一次遍历检查是否包含深色橡树树苗
        if (input.contains("深色橡树树苗")) {
            matchingBlocks.add(FLORA_MAP.get("深色橡树树苗"));
            hasDeepOak = true;
        }

        // 遍历其他植物
        for (Map.Entry<String, Block> entry : FLORA_MAP.entrySet()) {
            String chineseName = entry.getKey();
            // 如果是深色橡树树苗或者在有深色橡树树苗的情况下是橡树树苗，则跳过
            if (chineseName.equals("深色橡树树苗") ||
                    (hasDeepOak && chineseName.equals("橡树树苗"))) {
                continue;
            }
            if (input.contains(chineseName)) {
                matchingBlocks.add(entry.getValue());
            }
        }

        return matchingBlocks;
    }
    public static boolean isPassiveMob(Entity entity) {
        // Animal 类包含了大多数被动生物（如牛、羊、猪等）
        if (entity instanceof Animal) {
            return true;
        }

        // 某些特殊的被动生物需要单独判断
        if (entity instanceof Squid) {
            return true;
        }


        if (entity instanceof Bat) {
            return true;
        }

        if (entity instanceof Dolphin) {
            return true;
        }

        return false;
    }
    public static boolean isEnemy(Entity entity) {
        boolean isliving = false;
        if (entity instanceof LivingEntity){
            if (!((LivingEntity) entity).isDeadOrDying()){
                isliving = true;
            }
        }
        return isliving && isPassiveMob(entity);
    }
    private static void attackEntity(Entity entity) {
        mc.getConnection().send(ServerboundInteractPacket.createInteractionPacket(entity,mc.player.isShiftKeyDown(),InteractionHand.MAIN_HAND));
    }
}
