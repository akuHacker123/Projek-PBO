/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import service.AuthService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblStatus;
    private AuthService authService = new AuthService();

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
        setSize(380, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(BG);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setPreferredSize(new Dimension(315, 360));
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR),
                new EmptyBorder(28, 32, 28, 32)
        ));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/chikempruy.png"));
        Image img = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblLogo);
        card.add(Box.createVerticalStrut(10));

        JLabel lblApp = new JLabel("Chikempruy Computer");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblApp.setForeground(TEXT_DARK);
        lblApp.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblApp);
        card.add(Box.createVerticalStrut(5));

        JLabel lblSub = new JLabel("Sistem kasir toko komputer");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblSub);
        card.add(Box.createVerticalStrut(28));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(220, 210));

        formPanel.add(fieldLabel("Username"));
        formPanel.add(Box.createVerticalStrut(4));

        txtUsername = new JTextField();
        styleField(txtUsername);
        formPanel.add(txtUsername);
        formPanel.add(Box.createVerticalStrut(14));

        formPanel.add(fieldLabel("Password"));
        formPanel.add(Box.createVerticalStrut(4));

        txtPassword = new JPasswordField();
        styleField(txtPassword);
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(6));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(200, 60, 60));
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblStatus);
        formPanel.add(Box.createVerticalStrut(10));

        JButton btnLogin = new JButton("Masuk");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLogin.setBackground(ACCENT);
        btnLogin.setForeground(WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        formPanel.add(btnLogin);

        card.add(formPanel);
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

        model.User hasil = authService.login(user, pass);

        if (hasil != null) {
            dispose();

            if ("ADMIN".equalsIgnoreCase(hasil.getRole())) {
                new AdminDashboard(hasil);
            } else {
                new KasirFrame(hasil);
            }
        } else {
            lblStatus.setText("Username atau password salah.");
            txtPassword.setText("");
        }
    }
}