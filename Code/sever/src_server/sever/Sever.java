package Sever;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;

public class Sever {

    public static void main(String[] args) {
           System.setProperty("file.encoding", "UTF-8");
        try (ServerSocket serverSocket = new ServerSocket(7000)) {
            System.out.println("Server đang chạy tại port 7000...");

            while (true) {
                System.out.println("Đang chờ client kết nối...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

                Thread thread = new Thread(() -> handleClientRequest(socket));
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleClientRequest(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            Thread sendMessageThread = new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        String serverMsg = consoleReader.readLine();
                        if (serverMsg == null) {
                            break;
                        }

                        if (!serverMsg.trim().isEmpty()) {
                            writer.println("SERVER_MSG:" + serverMsg);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Dừng luồng gửi tin nhắn từ server.");
                }
            });

            sendMessageThread.setDaemon(true);
            sendMessageThread.start();

            while (true) {
                String request = reader.readLine();

                if (request == null) {
                    System.out.println("Client đã thoát: " + socket.getInetAddress().getHostAddress());
                    break;
                }

                if ("hello".equals(request)) {
                    writer.println("Server đã nhận kết nối");
                } else if ("shutdown".equals(request)) {
                    Runtime.getRuntime().exec("shutdown -s -t 3600");
                    writer.println("Máy tính sẽ tắt sau 3600 giây...");
                } else if ("restart".equals(request)) {
                    Runtime.getRuntime().exec("shutdown -r -t 3600");
                    writer.println("Máy tính sẽ khởi động lại sau 3600 giây...");
                } else if ("cancel".equals(request)) {
                    Runtime.getRuntime().exec("shutdown -a");
                    writer.println("Đã hủy lệnh shutdown/restart...");
                } else if ("screenshot".equals(request)) {
                    BufferedImage screenshot = new Robot().createScreenCapture(
                        new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())
                    );

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(screenshot, "png", baos);

                    byte[] imageBytes = baos.toByteArray();
                    baos.close();

                    writer.println(imageBytes.length);
                    socket.getOutputStream().write(imageBytes);
                    socket.getOutputStream().flush();

                    System.out.println("Đã gửi screenshot, kích thước: " + imageBytes.length + " bytes");
                } else if (request.startsWith("CHAT:")) {
                    String clientMsg = request.substring(5);
                    System.out.println("Tin nhắn từ client: " + clientMsg);
                    writer.println("Đã nhận tin nhắn từ client");
                } else if ("exit".equals(request)) {
                    System.out.println("Client yêu cầu thoát: " + socket.getInetAddress().getHostAddress());
                    break;
                } else {
                    writer.println("Lệnh không hợp lệ!");
                }
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi xử lý client.");
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Đã đóng kết nối với client.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}