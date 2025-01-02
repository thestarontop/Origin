package net.java.main.modules.combat;

import net.java.main.command.ChatManager;
import net.java.main.command.commands.Modules;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.misc.AntiBot;
import net.java.main.modules.misc.MidClick;
import net.java.main.modules.misc.Teams;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.apache.http.util.EntityUtils;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class LegitAura extends Module {
    public LegitAura() {
        super("LegitAura", "LegitAura", Module.Category.COMBAT);

    }


    @EventTarget
    public void Render2DEvent(UpdateEvent event) {
        if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) mc.hitResult).getEntity();
            if (entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) {
                if (mc.player.distanceTo(player) <= 3.000) {
                    mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, false));
                    mc.player.swing(InteractionHand.MAIN_HAND);
                }
            }
        }
    }



}