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
    //Co che xac thuc mat khau
    private static void handleClient(Socket socket) {
        String clientIp = socket.getInetAddress().getHostAddress();
        Path currentDirectory = getInitialDirectory();

        try (
            Socket clientSocket = socket;
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
            );
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8)
            )
        ) {
            //giao thuc xac thuc
            writer.write("AUTH_REQUIRED\n");
            writer.flush();

            String password = reader.readLine();
            if (password == null || !SERVER_PASSWORD.equals(password.trim())) {
                writer.write("AUTH_FAIL\n");
                writer.flush();
                System.out.println("Client " + clientIp + " xac thuc that bai.");
                return;
            }

            writer.write("AUTH_OK\n");
            writer.flush();

            System.out.println("Client " + clientIp + " xac thuc thanh cong.");
            System.out.println("Thu muc ban dau cua client " + clientIp + ": " + currentDirectory);

            String command;
            while ((command = reader.readLine()) != null) {
                command = command.trim();

                if (command.isEmpty()) {
                    sendResult(writer, "Lenh rong.\nCurrent directory: " + currentDirectory + "\nExit code: 1");
                    continue;
                }

                System.out.println("[" + clientIp + "] Lenh: " + command);

                if (command.equalsIgnoreCase("exit")) {
                    sendResult(writer, "Client da thoat.\nCurrent directory: " + currentDirectory + "\nExit code: 0");
                    break;
                }

                if (command.equalsIgnoreCase("pwd")) {
                    sendResult(writer, currentDirectory.toString() + "\nExit code: 0");
                    continue;
                }

                if (command.equalsIgnoreCase("cd")) {
                    sendResult(writer, currentDirectory.toString() + "\nExit code: 0");
                    continue;
                }

                if (command.toLowerCase().startsWith("cd ")) {
                    Path newDirectory = resolveCd(command, currentDirectory);

                    if (newDirectory == null) {
                        sendResult(writer, "Duong dan khong hop le.\nCurrent directory: " + currentDirectory + "\nExit code: 1");
                        continue;
                    }

                    if (!Files.exists(newDirectory)) {
                        sendResult(writer, "Thu muc khong ton tai: " + newDirectory + "\nCurrent directory: " + currentDirectory + "\nExit code: 1");
                        continue;
                    }

                    if (!Files.isDirectory(newDirectory)) {
                        sendResult(writer, "Day khong phai la thu muc: " + newDirectory + "\nCurrent directory: " + currentDirectory + "\nExit code: 1");
                        continue;
                    }

                    currentDirectory = newDirectory.normalize().toAbsolutePath();
                    sendResult(writer, "Da chuyen thu muc den: " + currentDirectory + "\nExit code: 0");
                    continue;
                }

                String result = executeCommand(command, currentDirectory);
                sendResult(writer, result);
            }

        } catch (SocketException e) {
            System.out.println("Client " + clientIp + " da ngat ket noi.");
        } catch (IOException e) {
            System.out.println("Loi xu ly client " + clientIp + ": " + e.getMessage());
        }
    }

    private static Path getInitialDirectory() {
        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }
}