package net.java.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.events.ItemInhandRenderEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemInHandRenderer.class)
public class MixinItemInHandRenderer {
    @Shadow private float offHandHeight;

    @Shadow private float mainHandHeight;

    @Shadow private ItemStack mainHandItem;

    @Shadow private ItemStack offHandItem;

    @Shadow @Final private Minecraft minecraft;

    @Shadow private float oOffHandHeight;

    @Shadow private float oMainHandHeight;
    @Shadow @Final private ItemRenderer itemRenderer;

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void renderItem(LivingEntity arg, ItemStack arg2, ItemTransforms.TransformType arg3, boolean bl, PoseStack arg4, MultiBufferSource arg5, int i) {
        if (!arg2.isEmpty()) {
            ItemInhandRenderEvent event = new ItemInhandRenderEvent(arg2,arg,arg4);
            madebystarontopandfml.getInstance().getEventManager().call(event);
            if (event.isCancelled) return;
            this.itemRenderer.renderStatic(event.getEntity(), event.getItemstack(), arg3, bl, event.getPosestack(), arg5, arg.level, i, OverlayTexture.NO_OVERLAY, arg.getId() + arg3.ordinal());
        }

    }
    }

