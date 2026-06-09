/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Olivia Oktaviani
 */
public class StrukDialog extends JDialog {

    private static final int PANEL_W = 300;
    private static final Font FONT_MONO_S = new Font("Monospaced", Font.PLAIN, 11);
    private static final Font FONT_UI_S   = new Font("Segoe UI",   Font.PLAIN, 11);
    private static final Font FONT_UI_B   = new Font("Segoe UI",   Font.BOLD,  13);

    public StrukDialog(Frame parent, Transaksi trx, Pelanggan pelanggan,
                       double dibayar, double kembalian, String metodeNama) {
        super(parent, "Struk Pembayaran", true);
        initComponents(trx, pelanggan, dibayar, kembalian, metodeNama);
    }

    private void initComponents(Transaksi trx, Pelanggan pelanggan,
                                 double dibayar, double kembalian, String metodeNama) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);

        // ── ROOT PANEL pakai BoxLayout Y, lebar fixed ──
        JPanel panel = new JPanel() {
            @Override public Dimension getPreferredSize() {
                return new Dimension(PANEL_W, super.getPreferredSize().height);
            }
        };
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 20, 16, 20));

        // LOGO
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/chikempruy.png"));
        Image img = logoIcon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);

        JLabel lblLogo = new JLabel(new ImageIcon(img));
        lblLogo.setPreferredSize(new Dimension(PANEL_W, 46));
        lblLogo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblLogo);
        panel.add(Box.createVerticalStrut(6));

        // HEADER TOKO
        panel.add(rowCenter("Chikempruy Computer", new Font("Segoe UI", Font.BOLD, 14)));
        panel.add(rowCenter("Jl. Prof. Sudarto No 16D, Tembalang, Semarang", FONT_UI_S));
        panel.add(rowCenter("No. Telp 081299887321", FONT_UI_S));
        panel.add(Box.createVerticalStrut(8));
        panel.add(dash());
        panel.add(Box.createVerticalStrut(6));

        // ── INFO TRANSAKSI ──
        SimpleDateFormat sdfTgl = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfJam = new SimpleDateFormat("HH:mm:ss");
        String tgl = sdfTgl.format(trx.getTanggal());
        String jam = sdfJam.format(trx.getTanggal());

        panel.add(row2col(tgl,  "Kasir", FONT_UI_S));
        panel.add(row2col(jam,  "Galang Bintang", FONT_UI_S));
        panel.add(Box.createVerticalStrut(4));
        panel.add(rowLeft("No. " + buatIdStruk(), FONT_UI_S));
        panel.add(Box.createVerticalStrut(6));
        panel.add(dash());
        panel.add(Box.createVerticalStrut(6));

        // ── ITEM BELANJA ──
        int nomorItem = 1, totalQty = 0;
        for (ItemKeranjang item : trx.getListBelanjaan()) {
            totalQty += item.getKuantitas();
            panel.add(rowLeft(nomorItem + ". " + item.getProduk().getNama(),
                              new Font("Segoe UI", Font.BOLD, 12)));
            panel.add(row2col(
                "  " + item.getKuantitas() + " x " + formatRp(item.getProduk().getHarga()),
                formatRp(item.getSubTotal()), FONT_MONO_S));
            panel.add(Box.createVerticalStrut(3));
            nomorItem++;
        }

        panel.add(Box.createVerticalStrut(4));
        panel.add(dash());
        panel.add(Box.createVerticalStrut(4));

        // Total QTY
        panel.add(rowLeft("Total QTY : " + totalQty, FONT_UI_S));
        panel.add(Box.createVerticalStrut(4));

        // ── RINGKASAN ──
        panel.add(row2col("Sub Total", formatRp(trx.getTotalKotor()), FONT_UI_S));
        if (trx.getDiskon() > 0) {
            panel.add(row2col("Diskon", "- " + formatRp(trx.getDiskon()), FONT_UI_S));
        }
        panel.add(row2col("Total", formatRp(trx.getTotalBersih()), FONT_UI_B));
        panel.add(Box.createVerticalStrut(4));
        panel.add(row2col("Bayar (" + metodeNama + ")", formatRp(dibayar), FONT_UI_S));
        panel.add(row2col("Kembali", formatRp(Math.max(kembalian, 0)), FONT_UI_S));

        panel.add(Box.createVerticalStrut(8));
        panel.add(dash());
        panel.add(Box.createVerticalStrut(8));
        panel.add(rowCenter("Terimakasih Telah Berbelanja", FONT_UI_S));
        panel.add(Box.createVerticalStrut(14));

        // Tombol tutup
        JButton btnTutup = new JButton("Tutup");
        btnTutup.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTutup.setBackground(new Color(50, 50, 50));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.setFocusPainted(false);
        btnTutup.setBorderPainted(false);
        btnTutup.setOpaque(true);
        btnTutup.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutup.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btnTutup.addActionListener(e -> dispose());
        panel.add(btnTutup);

        // ScrollPane hanya vertikal, no horizontal bar
        JScrollPane scroll = new JScrollPane(panel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(Color.WHITE);
        scroll.getViewport().setBackground(Color.WHITE);

        add(scroll);
        pack();
        // Batasi tinggi max 580
        setSize(PANEL_W + 40, Math.min(getHeight(), 580));
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    // ── HELPERS ──

    /** Baris 2 kolom pakai GridBagLayout agar lebar benar */
    private JPanel row2col(String kiri, String kanan, Font font) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setOpaque(true);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gcL = new GridBagConstraints();
        gcL.gridx = 0; gcL.gridy = 0;
        gcL.weightx = 1; gcL.fill = GridBagConstraints.HORIZONTAL;
        gcL.anchor = GridBagConstraints.WEST;

        GridBagConstraints gcR = new GridBagConstraints();
        gcR.gridx = 1; gcR.gridy = 0;
        gcR.weightx = 0; gcR.anchor = GridBagConstraints.EAST;

        JLabel lK = new JLabel(kiri);  lK.setFont(font); lK.setForeground(Color.BLACK);
        JLabel lR = new JLabel(kanan); lR.setFont(font); lR.setForeground(Color.BLACK);
        p.add(lK, gcL);
        p.add(lR, gcR);
        return p;
    }

    private JLabel rowLeft(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(Color.BLACK);
        lbl.setOpaque(false);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return lbl;
    }

    private JLabel rowCenter(String text, Font font) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(font);
        lbl.setForeground(Color.BLACK);
        lbl.setOpaque(false);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return lbl;
    }

    private JLabel dash() {
        JLabel lbl = new JLabel("- ".repeat(24));
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 10));
        lbl.setForeground(Color.BLACK);
        lbl.setOpaque(false);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 14));
        return lbl;
    }

    private String buatIdStruk() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(12);
        java.util.Random rng = new java.util.Random();
        for (int i = 0; i < 12; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
        return sb.toString();
    }

    private String formatRp(double nominal) {
        return "Rp " + String.format("%,.0f", nominal);
    }
}