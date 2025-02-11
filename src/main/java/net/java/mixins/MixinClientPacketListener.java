package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.modules.misc.NameProtect;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.java.main.utils.StringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraftforge.event.ForgeEventFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private Connection connection;

    @Shadow private boolean started;

    @Shadow private ClientLevel level;

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void handleMovePlayer(ClientboundPlayerPositionPacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        Player player = this.minecraft.player;
        if (arg.requestDismountVehicle()) {
            player.removeVehicle();
        }
        float preYaw = player.getYRot();
        float prePitch = player.getXRot();
        Vec3 vec3 = player.getDeltaMovement();
        boolean flag = arg.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.X);
        boolean flag1 = arg.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Y);
        boolean flag2 = arg.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Z);
        double d0;
        double d1;
        if (flag) {
            d0 = vec3.x();
            d1 = player.getX() + arg.getX();
            player.xOld += arg.getX();
        } else {
            d0 = 0.0;
            d1 = arg.getX();
            player.xOld = d1;
        }

        double d2;
        double d3;
        if (flag1) {
            d2 = vec3.y();
            d3 = player.getY() + arg.getY();
            player.yOld += arg.getY();
        } else {
            d2 = 0.0;
            d3 = arg.getY();
            player.yOld = d3;
        }

        double d4;
        double d5;
        if (flag2) {
            d4 = vec3.z();
            d5 = player.getZ() + arg.getZ();
            player.zOld += arg.getZ();
        } else {
            d4 = 0.0;
            d5 = arg.getZ();
            player.zOld = d5;
        }

        player.setPosRaw(d1, d3, d5);
        player.xo = d1;
        player.yo = d3;
        player.zo = d5;
        player.setDeltaMovement(d0, d2, d4);
        float f = arg.getYRot();
        float f1 = arg.getXRot();
        if (arg.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT)) {
            f1 += player.getXRot();
        }

        if (arg.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT)) {
            f += player.getYRot();
        }

        player.absMoveTo(d1, d3, d5, f, f1);
        this.connection.send(new ServerboundAcceptTeleportationPacket(arg.getId()));
        this.connection.send(new ServerboundMovePlayerPacket.PosRot(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot(), false));
        if (!this.started) {
            this.started = true;
            this.minecraft.setScreen((Screen)null);
        }
        RotationUtils.setTargetRotation(new Rotation(player.getYRot(), player.getXRot()),0);
        player.setYRot(preYaw);
        player.setXRot(prePitch);

    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void handleChat(ClientboundChatPacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        Component message = ForgeEventFactory.onClientChat(arg.getType(), arg.getMessage(), arg.getSender());
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nameprotect").isEnabled() && message.getString().contains(NameProtect.name)){
            message = new TextComponent(StringUtils.replace(message.getString(),NameProtect.name, ColorUtils.makeColour("Hidden"))).setStyle(message.getStyle());
        }
        if (message != null) {
            this.minecraft.gui.handleChat(arg.getType(), message, arg.getSender());
        }
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void handleAddObjective(ClientboundSetObjectivePacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        Scoreboard scoreboard = this.level.getScoreboard();
        String s = arg.getObjectiveName();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
        }
        if (arg.getMethod() == 0) {
            scoreboard.addObjective(s, ObjectiveCriteria.DUMMY, arg.getDisplayName(), arg.getRenderType());
        } else if (scoreboard.hasObjective(s)) {
            Objective objective = scoreboard.getObjective(s);
            if (arg.getMethod() == 1) {
                scoreboard.removeObjective(objective);
            } else if (arg.getMethod() == 2) {
                objective.setRenderType(arg.getRenderType());
                objective.setDisplayName(arg.getDisplayName());
            }
        }

    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void handleSetScore(ClientboundSetScorePacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        Scoreboard scoreboard = this.level.getScoreboard();
        String s = arg.getObjectiveName();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
        }
        switch (arg.getMethod()) {
            case CHANGE:
                Objective objective = scoreboard.getOrCreateObjective(s);
                Score score = scoreboard.getOrCreatePlayerScore(arg.getOwner(), objective);
                score.setScore(arg.getScore());
                break;
            case REMOVE:
                scoreboard.resetPlayerScore(arg.getOwner(), scoreboard.getObjective(s));
        }

    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void handleSetDisplayObjective(ClientboundSetDisplayObjectivePacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        Scoreboard scoreboard = this.level.getScoreboard();
        String s = arg.getObjectiveName();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
        }
        Objective objective = s == null ? null : scoreboard.getOrCreateObjective(s);
        scoreboard.setDisplayObjective(arg.getSlot(), objective);
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void setTitleText(ClientboundSetTitleTextPacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        String s = arg.getText().getString();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
            this.minecraft.gui.setTitle(new TextComponent(s).setStyle(arg.getText().getStyle()));
            return;
        }
        this.minecraft.gui.setTitle(arg.getText());
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void setSubtitleText(ClientboundSetSubtitleTextPacket arg) {
        PacketUtils.ensureRunningOnSameThread(arg, (ClientPacketListener)(Object)this, this.minecraft);
        String s = arg.getText().getString();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
            this.minecraft.gui.setTitle(new TextComponent(s).setStyle(arg.getText().getStyle()));
            return;
        }
        this.minecraft.gui.setSubtitle(arg.getText());
    }
}
