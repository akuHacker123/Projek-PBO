/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
/**
 *
 * @author Olivia Oktaviani
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "1234";

    private static final Color BG = new Color(245, 245, 245);
    private static final Color WHITE = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    private static final Color TEXT_GRAY = new Color(130, 130, 130);
    private static final Color ACCENT = new Color(70, 130, 180);

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR),
            new EmptyBorder(28, 32, 28, 32)
        ));

        JLabel lblApp = new JLabel("Toko Elektronik");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblApp.setForeground(TEXT_DARK);
        lblApp.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblApp);

        JLabel lblSub = new JLabel("Masuk ke sistem kasir");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(TEXT_GRAY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblSub);
        card.add(Box.createVerticalStrut(20));

        card.add(fieldLabel("Username"));
        card.add(Box.createVerticalStrut(4));
        txtUsername = new JTextField();
        styleField(txtUsername);
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(12));

        card.add(fieldLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        txtPassword = new JPasswordField();
        styleField(txtPassword);
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(6));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(200, 60, 60));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblStatus);
        card.add(Box.createVerticalStrut(10));

        JButton btnLogin = new JButton("Masuk");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(ACCENT);
        btnLogin.setForeground(WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        card.add(btnLogin);

        add(card);

        btnLogin.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());

        setVisible(true);
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void styleField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setForeground(TEXT_DARK);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR),
            new EmptyBorder(5, 8, 5, 8)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void prosesLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());
        if (user.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            dispose();
            new KasirFrame();
        } else {
            lblStatus.setText("Username atau password salah.");
            txtPassword.setText("");
        }
    }
}