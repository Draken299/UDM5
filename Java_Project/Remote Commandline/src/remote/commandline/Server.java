package remote.commandline;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {

        try {

            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("Server dang cho client ket noi...");

            Socket socket = serverSocket.accept();
            System.out.println("Client da ket noi!");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Scanner serverInput = new Scanner(System.in);

            String input;

            while ((input = in.readLine()) != null) {

                // ===== EXIT =====
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                // ===== CHAT =====
                if (input.startsWith("msg:")) {

                    String message = input.substring(4);
                    System.out.println("Client message: " + message);

                    out.println("Server da nhan tin nhan!");

                }

                // ===== COMMAND =====
                else if (input.startsWith("cmd:")) {

                    String command = input.substring(4);
                    System.out.println("Client command: " + command);

                    Process process = Runtime.getRuntime()
                            .exec(new String[]{"cmd.exe", "/c", command});

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(process.getInputStream()));

                    String line;

                    while ((line = reader.readLine()) != null) {
                        out.println(line);
                    }

                    out.println("END");
                }

                // ===== SHUTDOWN REQUEST =====
                else if (input.equalsIgnoreCase("req:shutdown")) {

                    System.out.println("Client yeu cau TAT MAY. Cho phep? (yes/no)");

                    String answer = serverInput.nextLine();

                    if (answer.equalsIgnoreCase("yes")) {

                        out.println("Server chap nhan shutdown");

                        Runtime.getRuntime().exec("shutdown /s /t 0");

                    } else {

                        out.println("Server tu choi shutdown");
                    }
                }

                // ===== RESTART REQUEST =====
                else if (input.equalsIgnoreCase("req:restart")) {

                    System.out.println("Client yeu cau RESTART. Cho phep? (yes/no)");

                    String answer = serverInput.nextLine();

                    if (answer.equalsIgnoreCase("yes")) {

                        out.println("Server chap nhan restart");

                        Runtime.getRuntime().exec("shutdown /r /t 0");

                    } else {

                        out.println("Server tu choi restart");
                    }
                }

                // ===== INVALID =====
                else {
                    out.println("Sai cu phap. Dung msg:, cmd:, req:");
                }

            }

            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}