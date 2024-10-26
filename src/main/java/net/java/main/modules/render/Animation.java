package net.java.main.modules.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.ItemInhandRenderEvent;
import net.java.main.modules.Module;
import net.java.main.modules.combat.KillAura;
import net.java.main.modules.movement.NoSlow;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;

public class Animation extends Module {
    public Animation(){
        super("Animation","bzd",Category.RENDER);
        this.addValues(blockRotX,blockRotY,blockRotZ,animationSpeed,anythingblock,realAutoBlock);
    }
    public FloatValue blockRotX = new FloatValue("BlockRotX", 220f, 0f, 360f);
    public FloatValue blockRotY = new FloatValue("BlockRotY", 355f, 0f,  360f);
    public FloatValue blockRotZ = new FloatValue("BlockRotZ", 80f, 0f, 360f);
    public FloatValue animationSpeed = new FloatValue("AnimationSpeed",1f,0f,4f);
    public BooleanValue anythingblock = new BooleanValue("AnythingBlock",false);
    public BooleanValue realAutoBlock = new BooleanValue("RealAutoBlock",false);
    @EventTarget
    public void onRenderItem(ItemInhandRenderEvent event){
        if(!(event.getEntity() instanceof LocalPlayer)) return;
        if (NoSlow.isUsable(event.getItemstack()) && !(realAutoBlock.getValue() && NoSlow.isUsable(mc.player.getItemInHand(InteractionHand.OFF_HAND)))) return;
        if (!mc.options.getCameraType().isFirstPerson()) return;
        ItemStack item = event.getItemstack();
                if (KillAura.target != null) {
                    if (item.getItem() instanceof SwordItem || anythingblock.getValue()) {
                        mc.player.swingTime = 0;
                        mc.player.swinging = false;
                        event.getPosestack().translate(0.0, 0.4000000059604645, -0.30000001192092896);
                        rotate(blockRotX.getValue(), 1.0F, 0.0F, 0.0F,event.getPosestack());
                        rotate((float)((double)((float)this.blockRotY.getValue()) + Math.sin((double)((float)System.nanoTime() / 800.0F) * this.animationSpeed.getValue() * 0.01745329238474369) * 30.0), 0.0F, 1.0F, 0.0F,event.getPosestack());
                        rotate(blockRotZ.getValue(), 0.0F, 0.0F, 1.0F,event.getPosestack());
                    }
                }
        if (NoSlow.isUsable(item) && mc.player.isUsingItem() && mc.player.getUsedItemHand() == InteractionHand.OFF_HAND && realAutoBlock.getValue()){
            event.cancelEvent();
        }
            
    }
    public void rotate(float angle, float x, float y, float z, PoseStack poseStack) {
        if (x == 1.0F) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(angle));
        }

        if (y == 1.0F) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(angle));
        }

        if (z == 1.0F) {
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(angle));
        }

    }
}
