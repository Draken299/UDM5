package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
           
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Nhập IP của server: ");
            String serverIp = sc.nextLine();

            System.out.println("Đang kết nối tới " + serverIp + ":7000 ...");
            Socket socket = new Socket(serverIp, 7000);
            System.out.println("Đã kết nối tới server: " + serverIp + ":7000");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("hello");
            System.out.println("Phản hồi từ server: " + reader.readLine());

            Thread receiveThread = new Thread(() -> {
                try {
                    while (true) {
                        String response = reader.readLine();
                        if (response == null) {
                            System.out.println("\nServer đã ngắt kết nối.");
                            break;
                        }

                        if (response.startsWith("SERVER_MSG:")) {
                            System.out.println("\nTin nhắn từ server: " + response.substring(11));
                        } else {
                            System.out.println("\nPhản hồi từ server: " + response);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\nMất kết nối tới server.");
                }
            });

            receiveThread.setDaemon(true);
            receiveThread.start();

            boolean exit = false;

            while (!exit) {
                System.out.println("\nMENU:");
                System.out.println("1. Shutdown");
                System.out.println("2. Restart");
                System.out.println("3. Cancel Shutdown/Restart");
                System.out.println("4. Screenshot");
                System.out.println("5. Gửi tin nhắn tới server");
                System.out.println("0. Thoát");
                System.out.print("Chọn: ");

                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        writer.println("shutdown");
                        break;

                    case 2:
                        writer.println("restart");
                        break;

                    case 3:
                        writer.println("cancel");
                        break;

                    case 4:
                 writer.println("screenshot");

                        int imageSize = Integer.parseInt(reader.readLine());
                        byte[] imageBytes = new byte[imageSize];

                        int totalRead = 0;
                        while (totalRead < imageSize) {
                            int count = socket.getInputStream().read(imageBytes, totalRead, imageSize - totalRead);
                            if (count == -1) {
                                throw new RuntimeException("Mất kết nối khi đang nhận ảnh");
                            }
                            totalRead += count;
                        }

                        System.out.print("Nhập tên file ảnh: ");
                        String fileName = sc.nextLine();

                        Path imagePath = Paths.get(fileName + ".png");
                        Files.write(imagePath, imageBytes);

                        System.out.println("Đã lưu ảnh tại: " + imagePath.toAbsolutePath());
                        break;

                    case 5:
                        System.out.print("Nhập tin nhắn gửi server: ");
                        String msg = sc.nextLine();
                        writer.println("CHAT:" + msg);
                        break;

                    case 0:
                        writer.println("exit");
                        exit = true;
                        System.out.println("Client đang thoát...");
                        socket.close();
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}       