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

        
    }
}