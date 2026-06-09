/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author Olivia Oktaviani
 */
public class StrukDialog extends JDialog {

    private final NumberFormat rupiahFmt = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public StrukDialog(Frame parent, Transaksi trx, Pelanggan pelanggan,
                       double dibayar, double kembalian, String metodeNama) {
        super(parent, "Struk Pembayaran", true);
        initComponents(trx, pelanggan, dibayar, kembalian, metodeNama);
    }

    private void initComponents(Transaksi trx, Pelanggan pelanggan,
                                 double dibayar, double kembalian, String metodeNama) {
        setSize(380, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        panel.add(centerLabel("TOKO ELEKTRONIK SEJAHTERA", Font.BOLD, 15, new Color(30, 30, 47)));
        panel.add(centerLabel("Jl. Sudirman No. 99, Semarang", Font.PLAIN, 11, Color.GRAY));
        panel.add(centerLabel("Telp: 024-123456", Font.PLAIN, 11, Color.GRAY));
        panel.add(garis());
        panel.add(Box.createVerticalStrut(4));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        panel.add(barisDua("No. Nota:", trx.getIdTransaksi()));
        panel.add(barisDua("Tanggal:", sdf.format(trx.getTanggal())));
        panel.add(barisDua("Kasir:", "Admin"));
        panel.add(barisDua("Pelanggan:", pelanggan.getNama() + " (" + pelanggan.getTipeMember() + ")"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(garis());

        JLabel lblItem = new JLabel("ITEM BELANJA:");
        lblItem.setFont(new Font("Monospaced", Font.BOLD, 12));
        lblItem.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblItem);
        panel.add(Box.createVerticalStrut(4));

        for (ItemKeranjang item : trx.getListBelanjaan()) {
            String nama = item.getProduk().getNama();
            String baris1 = nama.length() > 22 ? nama.substring(0, 22) : nama;
            panel.add(itemLabel(baris1));
            String detail = String.format("  %dx %s = %s",
                item.getKuantitas(),
                rupiahFmt.format(item.getProduk().getHarga()),
                rupiahFmt.format(item.getSubTotal()));
            panel.add(itemLabel(detail));
        }

        panel.add(Box.createVerticalStrut(4));
        panel.add(garis());

        panel.add(barisDua("Total Kotor:", rupiahFmt.format(trx.getTotalKotor())));
        if (trx.getDiskon() > 0) {
            JPanel pDiskon = barisDua("Diskon:", "- " + rupiahFmt.format(trx.getDiskon()));
            for (Component c : pDiskon.getComponents()) {
                if (c instanceof JLabel && ((JLabel) c).getText().startsWith("-")) {
                    ((JLabel) c).setForeground(new Color(39, 174, 96));
                }
            }
            panel.add(pDiskon);
        }
        panel.add(garis());

        JPanel pTotal = barisDua("TOTAL BAYAR:", rupiahFmt.format(trx.getTotalBersih()));
        for (Component c : pTotal.getComponents()) {
            if (c instanceof JLabel) {
                ((JLabel) c).setFont(new Font("Monospaced", Font.BOLD, 14));
                ((JLabel) c).setForeground(new Color(231, 76, 60));
            }
        }
        panel.add(pTotal);

        panel.add(Box.createVerticalStrut(4));
        panel.add(barisDua("Metode:", metodeNama));
        panel.add(barisDua("Dibayar:", rupiahFmt.format(dibayar)));
        if (kembalian > 0) {
            panel.add(barisDua("Kembalian:", rupiahFmt.format(kembalian)));
        }

        panel.add(garis());
        panel.add(Box.createVerticalStrut(4));
        panel.add(barisDua("Poin Terkumpul:", pelanggan.getPoin() + " poin"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(centerLabel("Terima kasih telah berbelanja!", Font.ITALIC, 12, Color.GRAY));
        panel.add(centerLabel("Simpan struk ini sebagai bukti pembayaran", Font.PLAIN, 10, Color.LIGHT_GRAY));
        panel.add(Box.createVerticalStrut(12));

        JButton btnTutup = new JButton("Tutup");
        btnTutup.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTutup.setBackground(new Color(30, 30, 47));
        btnTutup.setForeground(Color.WHITE);
        btnTutup.setFocusPainted(false);
        btnTutup.setBorderPainted(false);
        btnTutup.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTutup.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTutup.setMaximumSize(new Dimension(200, 36));
        btnTutup.addActionListener(e -> dispose());
        panel.add(btnTutup);

        JScrollPane scroll = new JScrollPane(panel);
        scroll.setBorder(null);
        add(scroll);
        setVisible(true);
    }

    private JLabel centerLabel(String text, int style, int size, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", style, size));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        return lbl;
    }

    private JLabel itemLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel barisDua(String kiri, String kanan) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        JLabel lblK = new JLabel(kiri);
        lblK.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JLabel lblN = new JLabel(kanan);
        lblN.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblN.setHorizontalAlignment(SwingConstants.RIGHT);
        p.add(lblK, BorderLayout.WEST);
        p.add(lblN, BorderLayout.EAST);
        return p;
    }

    private JSeparator garis() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        sep.setForeground(new Color(200, 200, 210));
        return sep;
    }
}