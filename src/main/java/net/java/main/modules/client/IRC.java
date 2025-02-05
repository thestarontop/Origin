package net.java.main.modules.client;

import net.java.main.command.ChatManager;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class IRC extends Module {
    public IRC(){super("IRC","bzd",Category.CLIENT);}
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean running = true;
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundChatPacket packet){
            String message = packet.getMessage();
            if (message.startsWith("!")){
                event.cancelEvent();
                sendMessage("["+mc.player.getDisplayName().getString()+"]" +" : "+message.replaceFirst("!",""));
            }
        }
    }
    @EventTarget
    public void onUpdate(UpdateEvent event){
        new Thread(this::receiveMessages).start();
        if (socket != null){
            if (!socket.isClosed()){
                return;
            }
        }
        try {
            socket = new Socket("n.rainplay.cn", 56690);
            // 使用UTF-8编码读取和写入
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void receiveMessages() {
        if (!running) return;
        String message;
        try {
            while ((message = in.readLine()) != null) {
                ChatManager.sendChat(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(String message) {
        out.println(message);
        out.flush(); // 确保消息被立即发送
    }
    @Override
    public void onDisable(){
        super.onDisable();
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    }
