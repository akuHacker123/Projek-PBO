/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Olivia Oktaviani
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblStatus;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "1234";

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login Kasir");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 47));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);

        JLabel lblTitle = new JLabel("SISTEM KASIR", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(99, 202, 183));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(lblTitle, gbc);

        JLabel lblSub = new JLabel("Toko Elektronik", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(150, 150, 170));
        gbc.gridy = 1;
        panel.add(lblSub, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gbc.gridx = 0;
        panel.add(lblUser, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        styleTextField(txtUsername);
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(lblPass, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(15);
        styleTextField(txtPassword);
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        lblStatus = new JLabel(" ", SwingConstants.CENTER);
        lblStatus.setForeground(new Color(255, 100, 100));
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        panel.add(lblStatus, gbc);

        gbc.gridy = 5;
        btnLogin = new JButton("LOGIN");
        btnLogin.setBackground(new Color(99, 202, 183));
        btnLogin.setForeground(new Color(30, 30, 47));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());

        add(panel);
        setVisible(true);
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(new Color(50, 50, 70));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 110)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    private void prosesLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            dispose();
            new KasirFrame();
        } else {
            lblStatus.setText("Username atau password salah!");
            txtPassword.setText("");
        }
    }
}