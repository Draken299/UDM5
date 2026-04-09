package com.mycompany.baitap15_remoteclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BaiTap15_RemoteClient {

    private static final int PORT = 7000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== REMOTE COMMAND CLIENT =====");

        try {
            System.out.print("Nhap IP server: ");
            String serverIp = sc.nextLine().trim();
        }
        
            try (
                Socket socket = new Socket(serverIp, PORT);
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
                )
            ) {
                String status = reader.readLine();

                if (!"CONNECTED".equals(status)) {
                    System.out.println("Khong the ket noi toi server.");
                    return;
                }

                System.out.println("Da ket noi toi server: " + serverIp + ":" + PORT);
                System.out.println("Nhap lenh de chay tren may server.");
                System.out.println("Go 'exit' de thoat client.");
                System.out.println("Go 'shutdown_server' de tat server.");
                System.out.println("--------------------------------");

 
        
    }
}