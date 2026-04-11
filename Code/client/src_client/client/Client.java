package com.mycompany.baitap15_remoteclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
<<<<<<< HEAD
import java.io.IOException;
=======
>>>>>>> main
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class BaiTap15_RemoteClient {

    private static final int PORT = 7000;
<<<<<<< HEAD
    private static final String OUTPUT_BEGIN = "OUTPUT_BEGIN";
    private static final String OUTPUT_END = "OUTPUT_END";
=======
>>>>>>> main

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== REMOTE COMMAND CLIENT =====");

        try {
            System.out.print("Nhap IP server: ");
            String serverIp = sc.nextLine().trim();

            try (
                Socket socket = new Socket(serverIp, PORT);
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
                )
            ) {
<<<<<<< HEAD
                String authRequest = reader.readLine();

                if (!"AUTH_REQUIRED".equals(authRequest)) {
                    System.out.println("Server khong phan hoi dung giao thuc.");
                    return;
                }

                System.out.print("Nhap password: ");
                String password = sc.nextLine();

                writer.write(password);
                writer.write("\n");
                writer.flush();

                String authResult = reader.readLine();

                if ("AUTH_FAIL".equals(authResult)) {
                    System.out.println("Sai password. Ket noi bi tu choi.");
                    return;
                }

                if (!"AUTH_OK".equals(authResult)) {
                    System.out.println("Loi giao thuc xac thuc.");
                    return;
                }

                System.out.println("Da ket noi va xac thuc thanh cong toi server: " + serverIp + ":" + PORT);
                System.out.println("Nhap lenh de chay tren may server.");
                System.out.println("Go 'pwd' de xem thu muc hien tai tren server.");
                System.out.println("Go 'cd <duong_dan>' de doi thu muc.");
                System.out.println("Go 'exit' de thoat.");
                System.out.println("--------------------------------");

=======
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

>>>>>>> main
                while (true) {
                    System.out.print("remote-shell> ");
                    String command = sc.nextLine();

                    writer.write(command);
                    writer.write("\n");
                    writer.flush();

                    String line;

<<<<<<< HEAD
                    while ((line = reader.readLine()) != null) {
                        if (OUTPUT_BEGIN.equals(line)) {
                            break;
}
                    }

                    if (line == null) {
                        System.out.println("Mat ket noi toi server.");
                        break;
                    }

                    while ((line = reader.readLine()) != null) {
                        if (OUTPUT_END.equals(line)) {
                            break;
                        }
                        System.out.println(line);
=======
                    // Tim tin hieu bat dau output
                    while ((line = reader.readLine()) != null) {
                        if ("OUTPUT_BEGIN".equals(line)) {
                            break;
                        }
>>>>>>> main
                    }

                    if (line == null) {
                        System.out.println("Mat ket noi toi server.");
                        break;
                    }

<<<<<<< HEAD
                    System.out.println("--------------------------------");

                    if ("exit".equalsIgnoreCase(command.trim())) {
=======
                    // Doc va in ket qua cho den khi gap tin hieu ket thuc
                    while ((line = reader.readLine()) != null) {
                        if ("OUTPUT_END".equals(line)) {
                            break;
                        }
                        System.out.println(line);
                    }

                    System.out.println("--------------------------------");

                    if ("exit".equalsIgnoreCase(command)) {
>>>>>>> main
                        break;
                    }
                }
            }

<<<<<<< HEAD
        } catch (IOException e) {
=======
        } catch (Exception e) {
>>>>>>> main
            System.out.println("Loi client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}