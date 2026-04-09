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
            System.out.println("Lỗi server: " + e.getMessage());
            e.printStackTrace();
        }
    }
 private static void handleClient(Socket socket) {
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)
            )
        ) {
            writer.write("CONNECTED\n");
            writer.flush();

            String command;
            while ((command = reader.readLine()) != null) {
                command = command.trim();

                if (command.isEmpty()) {
                    sendResult(writer, "Lenh rong.\nExit code: 1");
                    continue;
                }

                if (command.equalsIgnoreCase("exit")) {
                    sendResult(writer, "Client da thoat.\nExit code: 0");
                    break;
                }

                if (command.equalsIgnoreCase("shutdown_server")) {
                    sendResult(writer, "Server se dung.\nExit code: 0");
                    writer.flush();
                    socket.close();
                    System.exit(0);
                }

                System.out.println("Lenh nhan tu client: " + command);
                String result = executeCommand(command);
                sendResult(writer, result);
            }

        } catch (Exception e) {
            System.out.println("Loi xu ly client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Khong the dong socket.");
            }
        }
    }
    private static void sendResult(BufferedWriter writer, String result) throws Exception {
        writer.write("OUTPUT_BEGIN\n");
        writer.write(result);
        if (!result.endsWith("\n")) {
            writer.write("\n");
        }
        writer.write("OUTPUT_END\n");
        writer.flush();
    }

    private static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder pb;

            if (isWindows()) {
                pb = new ProcessBuilder("cmd", "/c", command);
            } else {
                pb = new ProcessBuilder("bash", "-lc", command);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            )) {
                String line;
                while ((line = br.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            output.append("Exit code: ").append(exitCode);

        } catch (Exception e) {
            output.append("Loi thuc thi lenh: ").append(e.getMessage()).append("\n");
            output.append("Exit code: -1");
        }

        return output.toString();
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }



}

