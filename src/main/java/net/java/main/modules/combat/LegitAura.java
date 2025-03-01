package net.java.main.modules.combat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.command.ChatManager;
import net.java.main.command.commands.Modules;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.misc.AntiBot;
import net.java.main.modules.misc.MidClick;
import net.java.main.modules.misc.Teams;
import net.java.main.utils.*;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class LegitAura extends Module {
    public LegitAura() {
        super("LegitAura", "LegitAura", Module.Category.COMBAT);
        addValues(player,mob,animal,cps,range,silentrotation);

    }
    public FloatValue cps = new FloatValue("CPS", 7f, 0.0f, 20f);
    public FloatValue range = new FloatValue("Range", 3.2f, 0.0f, 6.0f);
    public BooleanValue silentrotation = new BooleanValue("SilentRotation",true);
    public static BooleanValue player = new BooleanValue("AttackPlayer",true);
    public static BooleanValue mob = new BooleanValue("AttackMob",true);
    public static BooleanValue animal = new BooleanValue("AttackAnimal",true);
    private MSTimer timer = new MSTimer();
    public static Entity target;


    @EventTarget
    public void onUpdate2(UpdateEvent event) {
            if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                var entity = ((EntityHitResult) mc.hitResult).getEntity();
                if (entity instanceof AbstractClientPlayer player && !Teams.isTeammate(player)) {
                    target = player;
                    if (mc.player.distanceTo(target) <=range.getValue() && shouldAttack()) {

                        madebystarontopandfml.getInstance().getEventManager().call(new AttackEvent(entity));
                        mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(entity, mc.player.isShiftKeyDown()));
                        mc.player.swing(InteractionHand.MAIN_HAND);
                        timer.reset();
                    }
                }
            }
        }
    private boolean shouldAttack() {
        return timer.hasTimePassed((long) (1000.0D / cps.getValue()));
    }




    @Override
    public void onDisable(){
        super.onDisable();
        target = null;

    }
    @Override
    public void onEnable(){
        super.onEnable();
    }




}