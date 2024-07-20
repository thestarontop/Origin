package com.heypixel.heypixel.origin.mixins;

import com.heypixel.heypixel.origin.main.Origin;
import com.heypixel.heypixel.origin.main.modules.player.AutoTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
    @Shadow private float offHandHeight;

    @Shadow private float mainHandHeight;

    @Shadow private ItemStack mainHandItem;

    @Shadow private ItemStack offHandItem;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private float oOffHandHeight;

    @Shadow private float oMainHandHeight;

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void tick(){
            this.oMainHandHeight = this.mainHandHeight;
            this.oOffHandHeight = this.offHandHeight;
            LocalPlayer localplayer = this.minecraft.player;
            ItemStack itemstack = localplayer.getMainHandItem();
            if (Origin.getInstance().getModuleManager().getModule("autotool").isEnabled()){
                itemstack = localplayer.inventoryMenu.getSlot(AutoTool.currentslot+36).getItem();
            }
            ItemStack itemstack1 = localplayer.getOffhandItem();
            if (ItemStack.matches(this.mainHandItem, itemstack)) {
                this.mainHandItem = itemstack;
            }
            if (ItemStack.matches(this.offHandItem, itemstack1)) {
                this.offHandItem = itemstack1;
            }
            if (localplayer.isHandsBusy()) {
                this.mainHandHeight = Mth.clamp(this.mainHandHeight - 0.4f, 0.0f, 1.0f);
                this.offHandHeight = Mth.clamp(this.offHandHeight - 0.4f, 0.0f, 1.0f);
            } else {
                float f = localplayer.getAttackStrengthScale(1.0f);
                boolean requipM = ForgeHooksClient.shouldCauseReequipAnimation(this.mainHandItem, itemstack, localplayer.getInventory().selected);
                boolean requipO = ForgeHooksClient.shouldCauseReequipAnimation(this.offHandItem, itemstack1, -1);
                if (!requipM && this.mainHandItem != itemstack) {
                    this.mainHandItem = itemstack;
                }
                if (!requipO && this.offHandItem != itemstack1) {
                    this.offHandItem = itemstack1;
                }
                this.mainHandHeight += Mth.clamp((!requipM ? f * f * f : 0.0f) - this.mainHandHeight, -0.4f, 0.4f);
                this.offHandHeight += Mth.clamp((float)(!requipO ? 1 : 0) - this.offHandHeight, -0.4f, 0.4f);
            }
            if (this.mainHandHeight < 0.1f) {
                this.mainHandItem = itemstack;
            }
            if (this.offHandHeight < 0.1f) {
                this.offHandItem = itemstack1;
            }
        }
    }

