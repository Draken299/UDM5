/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.baitap15_remoteclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RemoteClientApp extends JFrame {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private boolean connected = false;

    private static final String OUTPUT_BEGIN = "OUTPUT_BEGIN";
    private static final String OUTPUT_END = "OUTPUT_END";

    private JTextField txtIp;
    private JTextField txtPort;
    private JPasswordField txtPassword;
    private JTextArea txtOutput;
    private JTextField txtCommand;
    private JButton btnConnect;
    private JButton btnDisconnect;
    private JButton btnSend;
    private JButton btnClear;
    private JLabel lblStatus;

    public RemoteClientApp() {
        initComponents();
        initCustomSettings();
    }

    private void initComponents() {
        setTitle("Remote Command Client GUI");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Ket noi"));

        JLabel lblIp = new JLabel("Server IP:");
        lblIp.setFont(font);
        txtIp = new JTextField(10);
        txtIp.setFont(font);

        JLabel lblPort = new JLabel("Port:");
        lblPort.setFont(font);
        txtPort = new JTextField(5);
        txtPort.setFont(font);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(font);
        txtPassword = new JPasswordField(10);
        txtPassword.setFont(font);

        btnConnect = new JButton("Connect");
        btnConnect.setFont(font);

        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.setFont(font);

        lblStatus = new JLabel("Status: Disconnected");
        lblStatus.setFont(font);

        topPanel.add(lblIp);
        topPanel.add(txtIp);
        topPanel.add(lblPort);
        topPanel.add(txtPort);
        topPanel.add(lblPassword);
        topPanel.add(txtPassword);
        topPanel.add(btnConnect);
        topPanel.add(btnDisconnect);
        topPanel.add(lblStatus);

        add(topPanel, BorderLayout.NORTH);

        txtOutput = new JTextArea();
        txtOutput.setFont(font);
        txtOutput.setEditable(false);
        txtOutput.setLineWrap(true);
        txtOutput.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtOutput);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Ket qua"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Dieu khien"));

        JLabel lblCommand = new JLabel("Command:");
        lblCommand.setFont(font);

        txtCommand = new JTextField(30);
        txtCommand.setFont(font);

        btnSend = new JButton("Send");
        btnSend.setFont(font);

        btnClear = new JButton("Clear");
        btnClear.setFont(font);

        bottomPanel.add(lblCommand);
        bottomPanel.add(txtCommand);
        bottomPanel.add(btnSend);
        bottomPanel.add(btnClear);

        add(bottomPanel, BorderLayout.SOUTH);

        btnConnect.addActionListener(e -> connectToServer());
        btnDisconnect.addActionListener(e -> disconnectFromServer());
        btnSend.addActionListener(e -> sendCommand());
        btnClear.addActionListener(e -> txtOutput.setText(""));
        txtCommand.addActionListener(e -> sendCommand());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnectSilentlyOnClose();
                dispose();
                System.exit(0);
            }
        });
    }

    private void initCustomSettings() {
        txtIp.setText("127.0.0.1");
        txtPort.setText("7000");
        txtPassword.setText("");
        txtCommand.setText("");
        txtOutput.setText("===== REMOTE COMMAND CLIENT GUI =====\n");

        btnDisconnect.setEnabled(false);
        btnSend.setEnabled(false);
        txtCommand.setEnabled(false);
    }
