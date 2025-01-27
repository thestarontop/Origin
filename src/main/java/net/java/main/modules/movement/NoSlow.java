package net.java.main.modules.movement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.events.*;
import net.java.main.event.annotations.EventTarget;
import net.java.main.modules.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.utils.MSTimer;
import net.java.main.utils.PacketUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.Foods;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class NoSlow extends Module {
    public NoSlow(){
        super("NoSlow","bzd",Category.MOVEMENT);
        addValues(withHitting,mode,startTime);
    }
    public static BooleanValue withHitting = new BooleanValue("With Hitting",true);
    public static ListValue mode = new ListValue("Mode", new String[]{"RestBug","Grim","Gapple"},"Gapple");
    public FloatValue startTime = new FloatValue("startTime", 100f, 0.0f, 200f);
    public static boolean shouldnoslow = true;

    @EventTarget
    public void onPacket(PacketEvent event) {

        if(mode.getValue().equals("Grim")) {
            if (event.getPacket() instanceof ServerboundInteractPacket && (!shouldnoslow && mc.player.isUsingItem())&&!withHitting.getValue()){
                event.cancelEvent();
            }
            if (event.getPacket() instanceof ServerboundSwingPacket && (!shouldnoslow && mc.player.isUsingItem())&&!withHitting.getValue()){
                event.cancelEvent();
            }
            if(event.getPacket() instanceof ServerboundUseItemPacket packet){

                ItemStack item = mc.player.getItemInHand(packet.getHand());
                if (!isUsable(item)){
                    return;
                }
                shouldnoslow = false;

                //powered by mojang
                AbstractContainerMenu abstractcontainermenu = mc.player.inventoryMenu;
                NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
                int i = nonnulllist.size();
                List<ItemStack> list = Lists.newArrayListWithCapacity(i);
                Iterator var10 = nonnulllist.iterator();

                while(var10.hasNext()) {
                    Slot slot = (Slot)var10.next();
                    list.add(slot.getItem().copy());
                }

                Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

                for(int j = 0; j < i; ++j) {
                    ItemStack itemstack = list.get(j);
                    ItemStack itemstack1 = nonnulllist.get(j).getItem();
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        int2objectmap.put(j, itemstack1.copy());
                    }
                }
                boolean sprinting=mc.player.isSprinting();
                if(tick>=maxTick&&isEatable(item)) {
                    tick = 0;
                    maxTick = item.getUseDuration();
                }
                if(sprinting)
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                PacketUtils.sendPacketNoEvent(new ServerboundContainerClickPacket(0,-32767,mc.player.getInventory().selected+36,0,ClickType.PICKUP, mc.player.getInventory().getSelected(),int2objectmap));
                PacketUtils.sendPacketNoEvent(new ServerboundContainerClosePacket(0));

                //第二解决方案：副手
                mc.player.setSprinting(false);
            }
            if(event.getPacket() instanceof ClientboundContainerSetSlotPacket packet){

                if (packet.getSlot() == mc.player.getInventory().selected +36 && packet.getContainerId() == 0){
                    shouldnoslow = true;
                }
            }
            if(event.getPacket() instanceof ServerboundPlayerActionPacket packet&&packet.getAction()== ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
                if(shouldnoslow){
                    event.cancelEvent();
                    shouldnoslow=false;
                }
            }
        }
        else if(mode.getValue().equals("Gapple")){
            if(event.getPacket() instanceof ServerboundUseItemPacket packet) {
                ItemStack item = mc.player.getItemInHand(packet.getHand());
                if (!isUsable(item)) {
                    return;
                }
                timer.reset();

            }

            if(event.getPacket() instanceof ServerboundPlayerActionPacket packet&&packet.getAction()== ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM){
                if(shouldnoslow&&eating) {
                    eating=false;
                    event.cancelEvent();
                }
                shouldnoslow = false;

            }
            if (event.getPacket() instanceof ServerboundInteractPacket && (!shouldnoslow && mc.player.isUsingItem())){
                event.cancelEvent();
            }
            if (event.getPacket() instanceof ServerboundSwingPacket && (!shouldnoslow && mc.player.isUsingItem())){
                event.cancelEvent();
            }
            //
        }


    }
    private boolean eating;
    private int tick,maxTick=0;
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if(tick<maxTick) {
            int width = mc.getWindow().getGuiScaledWidth(); // 获取窗口的宽度
            int height = mc.getWindow().getGuiScaledHeight(); // 获取窗口的高度

            int progressBarWidth = width / 4; // 进度条宽度为屏幕宽度的一半
            int progressBarHeight = 5; // 进度条的高度
            int progressBarX = width / 2 - progressBarWidth / 2; // 计算进度条的X坐标，使其居中
            int progressBarY = height / 2 - progressBarHeight / 2; // 计算进度条的Y坐标，使其居中

            // 绘制进度条的背景
            Screen.fill(new PoseStack(), progressBarX, progressBarY, progressBarX + progressBarWidth, progressBarY + progressBarHeight, new Color(0, 0, 0, 128).getRGB());

            // 计算进度条的当前进度
            float progress = Math.min(1.0F, (float) tick / maxTick); // 确保进度不超过1

            // 绘制进度条的填充部分
            int filledWidth = (int) (progressBarWidth * progress); // 根据进度计算填充的宽度
            Screen.fill(new PoseStack(), progressBarX, progressBarY, progressBarX + filledWidth, progressBarY + progressBarHeight, new Color(255, 255, 255, 255).getRGB());
            //  FontManager.tenacity20.drawCenteredString(RenderManager.currentPoseStack,"Eating",width/2f,height/2f+20,Color.white);
            //    mc.font.drawShadow(new PoseStack(),"Eating",width/2f,height/2f+20,Color.white);
        }
    }
    public static boolean isUsable(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            boolean isFood = item.getFoodProperties() != null;
            boolean isShield = item == Items.SHIELD;
            boolean isBow = item instanceof BowItem;
            boolean isCrossBow = item instanceof CrossbowItem;
            boolean isPotion = item == Items.POTION;

            return isFood || isShield || isBow || isCrossBow || isPotion;
        } else {
            return false;
        }
    }
    private boolean isEatable(ItemStack itemStack) {
        if (itemStack != null && !itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            boolean isFood = item.getFoodProperties() != null;

            boolean isPotion = item == Items.POTION;

            return isFood || isPotion;
        } else {
            return false;
        }
    }
    MSTimer timer = new MSTimer();
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        tick=Math.min(tick+1,maxTick);
        //System.out.println("tick:"+tick+" maxTick:"+maxTick);
        if (mode.getValue().equals("Gapple")&&timer.passedMs(startTime.getValue())&&!shouldnoslow) {
            ItemStack item = mc.player.getUseItem();

            if (!isUsable(item)) {
                return;
            }

            eating=isEatable(item);
            if(tick>=maxTick&&eating) {
                tick = 0;
                maxTick = item.getUseDuration();
            }
            else {
                tick=0;
                maxTick=0;
            }
            //PacketUtils.sendPacketNoEvent(new ServerboundTeleportToEntityPacket());
           /* if (slot != mc.player.getInventory().selected) {
                PacketUtils.sendPacketNoEvent(new ServerboundSetCarriedItemPacket(slot));
                PacketUtils.sendPacketNoEvent(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.DROP_ITEM, new BlockPos(0, 0, 0), Direction.DOWN));
                PacketUtils.sendPacketNoEvent(new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
            } else
                PacketUtils.sendPacketNoEvent(new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.DROP_ITEM, new BlockPos(0, 0, 0), Direction.DOWN));*/

            AbstractContainerMenu abstractcontainermenu = mc.player.inventoryMenu;
            NonNullList<Slot> nonnulllist = abstractcontainermenu.slots;
            int i = nonnulllist.size();
            List<ItemStack> list = Lists.newArrayListWithCapacity(i);
            Iterator var10 = nonnulllist.iterator();

            while(var10.hasNext()) {
                Slot slot = (Slot)var10.next();
                list.add(slot.getItem().copy());
            }

            Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = list.get(j);
                ItemStack itemstack1 = nonnulllist.get(j).getItem();
                if (!ItemStack.matches(itemstack, itemstack1)) {
                    int2objectmap.put(j, itemstack1.copy());
                }
            }

            boolean sprinting=mc.player.isSprinting();

            if(sprinting)
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));

            PacketUtils.sendPacketNoEvent(new ServerboundContainerClickPacket(0,-32767,mc.player.getInventory().selected%8+1+36,0,ClickType.PICKUP, mc.player.getInventory().getItem(mc.player.getInventory().selected%8),int2objectmap));
            PacketUtils.sendPacketNoEvent(new ServerboundContainerClosePacket(0));
            timer.reset();
            shouldnoslow = true;


        }
        if (mc.player.isUsingItem() && !shouldnoslow) mc.player.setSprinting(false);
        if (shouldnoslow && mc.player.getUseItemRemainingTicks() <= 0) shouldnoslow = false;
        if(mode.getValue().equals("RestBug")){
            if(mc.player.isUsingItem()) {

                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY));
                mc.player.stopUsingItem();
                mc.player.closeContainer();
            }
        }
    }

    @EventTarget
    public void onMoment(MoveEvent event) {
        if(mode.getValue().equals("Grim")) {
            if (mc.player.isUsingItem() && !shouldnoslow) mc.player.setSprinting(false);
        }

    }

    @EventTarget
    public void onSlow(SlowDownEvent event) {
        if (shouldnoslow) {
            event.setMovementForward(1.0f);
            event.setMovementStrafe(1.0f);
        }

    }

}
