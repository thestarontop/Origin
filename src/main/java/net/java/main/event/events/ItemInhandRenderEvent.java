package net.java.main.event.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.impl.Event;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemInhandRenderEvent implements Event {
    private ItemStack itemstack;
    private LivingEntity entity;
    private PoseStack posestack;
    public boolean isCancelled = false;
    public ItemInhandRenderEvent(ItemStack itemstack,LivingEntity entity,PoseStack posestack){
        this.itemstack = itemstack;
        this.entity = entity;
        this.posestack = posestack;
    }

    public PoseStack getPosestack() {
        return posestack;
    }

    public void setPosestack(PoseStack posestack) {
        this.posestack = posestack;
    }

    public ItemStack getItemstack() {
        return itemstack;
    }

    public void setItemstack(ItemStack itemstack) {
        this.itemstack = itemstack;
    }

    public LivingEntity getEntity() {
        return entity;
    }
    public void cancelEvent() {
        isCancelled = true;
    }
}
