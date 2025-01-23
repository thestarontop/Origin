package net.java.main.irc;

import java.io.*;
import java.net.*;
import java.util.*;

public class IRCServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();

    public IRCServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        System.out.println("Server started. Waiting for clients...");

        // 启动一个线程来接收管理员输入并发送消息
        new Thread(this::adminInputHandler).start();

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler client = new ClientHandler(socket);
                clients.add(client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void adminInputHandler() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String message = reader.readLine();
                if (message != null && !message.isEmpty()) {
                    sendMessageToAllClients("[Admin]: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToAllClients(String message) {
        for (ClientHandler client : clients) {
            client.sendServerMessage(message);
        }
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                // 使用UTF-8编码读取和写入
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                    System.out.println("Received message: " + message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
                System.out.println("Client disconnected: " + socket);
            }
        }

        private void broadcast(String message) {
            System.out.println("Broadcasting: " + message);
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.sendMessage(message);
                }
            }
        }

        private void sendMessage(String message) {
            out.println(message);
            out.flush();
        }

        public void sendServerMessage(String message) {
            out.println("[Server]: " + message);
            out.flush();
        }
    }

    public static void main(String[] args) {
        int port = 11451; // 选择一个合适的端口号
        IRCServer server = new IRCServer(port);
        server.startServer();
    }
}
