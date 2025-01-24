package net.java.main.irc;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class IRCClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public IRCClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            // 使用UTF-8编码读取和写入
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            System.out.print("Enter your username: ");
            Scanner scanner = new Scanner(System.in);
            username = scanner.nextLine();

            // 启动一个线程来接收服务器的消息
            new Thread(this::receiveMessages).start();

            // 在主线程中发送消息
            String message;
            while (true) {
                message = scanner.nextLine();
                if (message.equalsIgnoreCase("/quit")) {
                    break;
                }
                sendMessage(username + ": " + message);
                System.out.println("Message sent: " + username + ": " + message);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        out.println(message);
        out.flush(); // 确保消息被立即发送
    }

    public static void main(String[] args) {
        String host = "n.rainplay.cn"; // 或者服务器的IP地址
        int port = 30586; // 与服务器相同的端口
        IRCClient client = new IRCClient(host, port);
        client.start();
    }
}
