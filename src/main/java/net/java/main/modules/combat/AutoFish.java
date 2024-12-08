package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class AutoFish extends Module {
    public AutoFish() {
        super("AutoFish","bitch", Category.COMBAT);
    }

    private MSTimer timer = new MSTimer();
//操你妈
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        Iterable<Entity> entitiylist = mc.level.entitiesForRendering();
        for (Entity entity : entitiylist) {
            if (mc.player.distanceTo(entity) < 4) {
                if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.FISHING_ROD && timer.hasTimePassed(150)) {
                    mc.getConnection().send(new ServerboundUseItemPacket(InteractionHand.MAIN_HAND));
                }
            }
        }
        timer.reset();
    }











}
