package com.mycompany.baitap15_remoteserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//khoi tao cau truc va cac hang so
public class BaiTap15_RemoteServer {
    //hang so cau hinh he thong
    private static final int PORT = 7000;
    private static final String SERVER_PASSWORD = "1103";
    private static final String OUTPUT_BEGIN = "OUTPUT_BEGIN";
    private static final String OUTPUT_END = "OUTPUT_END";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("===== REMOTE COMMAND SERVER =====");
            System.out.println("Server dang chay tai port " + PORT);
            System.out.println("Dang cho client ket noi...");
            
            //thiet lap xu ly da luong
            while (true) {
                Socket socket = serverSocket.accept();
                String clientIp = socket.getInetAddress().getHostAddress();
                System.out.println("Client dang ket noi: " + clientIp);
                
                //moi client 1 thread rieng
                Thread clientThread = new Thread(() -> handleClient(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Loi server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}