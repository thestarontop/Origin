package net.java.mixins;

import net.java.main.utils.Rotation;
import net.java.main.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private Connection connection;

    @Shadow private boolean started;

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
}
