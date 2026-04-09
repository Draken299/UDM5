package com.mycompany.baitap15_remoteserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BaiTap15_RemoteServer {

    private static final int PORT = 7000;

    public static void main(String[] args) {
        System.out.println("===== REMOTE COMMAND SERVER =====");
        System.out.println("Server dang chay tai port " + PORT);
        System.out.println("Dang cho client ket noi...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                
                handleClient(socket); //Goi logic cua TV6
                
                System.out.println("Client da ngat ket noi.");
                System.out.println("Dang cho client khac...");
            }
        } catch (Exception e) {
           //Logic xu ly chuoi cua tv6
        }
    }
}