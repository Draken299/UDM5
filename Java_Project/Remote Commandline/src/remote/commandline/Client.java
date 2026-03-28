package remote.commandline;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        try {

            Socket socket = new Socket("localhost", 6000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Da ket noi toi server!");

            while (true) {

                System.out.print("Nhap: ");
                String input = scanner.nextLine();

                out.println(input);

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                String response;

                while ((response = in.readLine()) != null) {

                    System.out.println(response);

                    if (response.equals("END")) {
                        break;
                    }

                    if (!input.startsWith("cmd:")) {
                        break;
                    }
                }

            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}