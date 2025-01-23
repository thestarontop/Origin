package net.java.main.modules.misc;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.java.main.modules.combat.KillAura.isInIterable;
import static net.minecraft.world.level.block.Blocks.DARK_OAK_SAPLING;

public class AutoPartyGame extends Module {
    public AutoPartyGame(){super("AutoPartyGame","bzd",Category.MISC);}
    Block currentitem = null;
    private int windowid = 0;
    private boolean hasWindow = false;
    private int ticks = 0;
    private boolean isfull = false;

    private List<Item> blackItemList = List.of(Items.DIAMOND_SHOVEL,Items.STONE_SHOVEL,Items.IRON_SHOVEL,Items.GOLDEN_SHOVEL,Items.NETHERITE_SHOVEL,Items.COBWEB,Items.EGG,Items.BOOK,Items.CHEST,Items.FISHING_ROD,Items.LAVA_BUCKET,Items.CROSSBOW,Items.EXPERIENCE_BOTTLE,Items.WATER_BUCKET,Items.SADDLE,Items.FLINT,Items.FLINT_AND_STEEL,Items.COMPASS,Items.SNOWBALL);

    private List<BlockPos> clickedblock = List.of();
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
        put("深色橡树树苗", DARK_OAK_SAPLING);
    }};
    int delay = 1;
    private LinkedList<Entity> CanReachEntities = new LinkedList<>();
    private LinkedList<Entity> AttackedEntities = new LinkedList<>();
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundOpenScreenPacket packet){
            hasWindow = true;
            windowid = packet.getContainerId();
        }
        if (event.getPacket() instanceof ClientboundContainerSetContentPacket packet) {
            isfull = true;
            for (int i = 9; i <= 36; i++) {
                if (mc.player.inventoryMenu.getSlot(i).getItem().getItem() == Items.AIR) {
                    isfull = false;
                    break;
                }
            }
            if (packet.getContainerId() != windowid || isfull) return;

            //powered by mojang
            AbstractContainerMenu abstractcontainermenu = mc.player.containerMenu;
            NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
            int i = nonnulllist.size();
            List<ItemStack> list = Lists.newArrayListWithCapacity(i);
            Iterator var10 = nonnulllist.iterator();

            while (var10.hasNext()) {
                Slot slot = (Slot) var10.next();
                list.add(slot.getItem().copy());
            }

            Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = list.get(j);
                ItemStack itemstack1 = nonnulllist.get(j).getItem();
                if (!ItemStack.matches(itemstack, itemstack1)) {
                    int2objectmap.put(j, itemstack1.copy());
                }
            }

            if (packet.getItems().size() == 63) {
                for (int index = 0; index <= 26; index++) {
                    if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                    if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                        mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                    } else {
                        mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                    }

                }

                if (packet.getItems().size() == 90) {
                    for (int index = 0; index <= 53; index++) {
                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;


                        if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                        }
                    }
                }
                if (packet.getItems().size() == 39) {
                    for (int index = 0; index <= 2; index++) {
                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                        if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                        }
                    }
                }
                if (packet.getItems().size() == 41) {
                    for (int index = 0; index <= 2; index++) {


                        if (packet.getItems().get(index).getItem() == Items.AIR) continue;
                        if (blackItemList.contains(packet.getItems().get(index).getItem())) {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.THROW, packet.getItems().get(index), int2objectmap));
                        } else {
                            mc.getConnection().send(new ServerboundContainerClickPacket(packet.getContainerId(), index, index, 1, ClickType.QUICK_MOVE, packet.getItems().get(index), int2objectmap));
                        }
                    }
                }
            }
        }
        if (event.getPacket() instanceof ClientboundSetSubtitleTextPacket packet){
            List<Block> Blocks = findMatchingBlocks(packet.getText().getString());
            for (Block item : Blocks) {
                currentitem = item;
                if (item == DARK_OAK_SAPLING)
                    break;
            }
        }

    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        if (hasWindow){
            ticks ++;
            if (ticks == 5){
                mc.player.closeContainer();
                hasWindow = false;
                ticks = 0;
            }
        }
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
            if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof HoeItem && value != Blocks.AIR){
                if (clickedblock.contains(key)) continue;
                mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()), Direction.UP, key, false)));
                clickedblock.add(key);
                break;
            }
            if (value == Blocks.WATER){
                if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BUCKET) {
                    Rotation rotation = RotationUtils.getBlockPlacementRotation(key);
                    if (rotation != null) {
                        RotationUtils.setTargetRotation(rotation, 20);
                        mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                    }
                }
                break;
            }
            if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.WATER_BUCKET && value == Blocks.FURNACE){
                mc.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(key.getX(), key.getY(), key.getZ()), Direction.DOWN, key, false)));
            }
            if (value == Blocks.STONE_BUTTON || value == Blocks.CAKE){
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

        // 遍历 FLORA_MAP 中的所有中文名称
        for (Map.Entry<String, Block> entry : FLORA_MAP.entrySet()) {
            String chineseName = entry.getKey();
            // 如果输入的字符串包含某个中文名称
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
    @Override
    public void onDisable(){
        super.onDisable();
        clickedblock.clear();
    }
}