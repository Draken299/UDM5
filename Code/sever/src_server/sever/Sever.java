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
<<<<<<< HEAD

=======
            
            //thiet lap xu ly da luong
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
            while (true) {
                Socket socket = serverSocket.accept();
                String clientIp = socket.getInetAddress().getHostAddress();
                System.out.println("Client dang ket noi: " + clientIp);
<<<<<<< HEAD

=======
                
                //moi client 1 thread rieng
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
                Thread clientThread = new Thread(() -> handleClient(socket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Loi server: " + e.getMessage());
            e.printStackTrace();
        }
    }
<<<<<<< HEAD

=======
    //Co che xac thuc mat khau
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
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
<<<<<<< HEAD
=======
            //giao thuc xac thuc
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
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
<<<<<<< HEAD

    private static Path resolveCd(String command, Path currentDirectory) {
        try {
            String target = command.substring(2).trim();
=======
    //xu ly thu muc va cac lenh pwd, cd
    //ham xu ly lenh CD
    private static Path resolveCd(String command, Path currentDirectory) {
        try {
            String target = command.substring(2).trim();

            if (target.isEmpty()) {
                return currentDirectory;
            }
            
            //xu ly dau ngoac kep va duong dan
            if ((target.startsWith("\"") && target.endsWith("\"")) ||
                (target.startsWith("'") && target.endsWith("'"))) {
                target = target.substring(1, target.length() - 1).trim();
            }
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7

            if (target.isEmpty()) {
                return currentDirectory;
            }

<<<<<<< HEAD
            if ((target.startsWith("\"") && target.endsWith("\"")) ||
                (target.startsWith("'") && target.endsWith("'"))) {
                target = target.substring(1, target.length() - 1).trim();
            }

            if (target.isEmpty()) {
                return currentDirectory;
            }

            Path newPath = Paths.get(target);

            if (!newPath.isAbsolute()) {
                newPath = currentDirectory.resolve(newPath);
            }

            return newPath.normalize().toAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private static String executeCommand(String command, Path currentDirectory) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder pb;

            if (isWindows()) {
                pb = new ProcessBuilder("cmd", "/c", command);
            } else {
                pb = new ProcessBuilder("bash", "-lc", command);
            }

            pb.directory(currentDirectory.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();

=======
            Path newPath = Paths.get(target);

            if (!newPath.isAbsolute()) {
                newPath = currentDirectory.resolve(newPath);
            }

            return newPath.normalize().toAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
    //thuc thi lenh he thong
    private static String executeCommand(String command, Path currentDirectory) {
        StringBuilder output = new StringBuilder();

        try {
            ProcessBuilder pb;

            if (isWindows()) {
                pb = new ProcessBuilder("cmd", "/c", command);
            } else {
                pb = new ProcessBuilder("bash", "-lc", command);
            }

            pb.directory(currentDirectory.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();

>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            )) {
                String line;
                while ((line = br.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();

            output.append("Current directory: ")
                  .append(currentDirectory.toAbsolutePath().normalize())
                  .append("\n");
            output.append("Exit code: ").append(exitCode);

        } catch (Exception e) {
            output.append("Loi thuc thi lenh: ").append(e.getMessage()).append("\n");
            output.append("Current directory: ")
                  .append(currentDirectory.toAbsolutePath().normalize())
                  .append("\n");
            output.append("Exit code: -1");
        }

        return output.toString();
    }
<<<<<<< HEAD

    private static void sendResult(BufferedWriter writer, String result) throws IOException {
        writer.write(OUTPUT_BEGIN);
        writer.write("\n");
        writer.write(result);
        if (!result.endsWith("\n")) {
            writer.write("\n");
        }
        writer.write(OUTPUT_END);
        writer.write("\n");
        writer.flush();
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }
=======
>>>>>>> 7aebb849867c0d0b84a3b15863c9f497e1c073c7
}