/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;
import service.*;
import exception.*;
import interfaces.MetodePembayaran;
import util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 *
 * @author Olivia Oktaviani
 */

public class KasirFrame extends JFrame {

    private JTable tblProduk, tblKeranjang;
    private DefaultTableModel modelProduk, modelKeranjang;
    private JTextField txtNamaPelanggan, txtQty;
    private JComboBox<String> cmbMetodeBayar, cmbTipeMember;
    private JLabel lblTotal, lblDiskon, lblBayar, lblPelangganInfo;
    private JButton btnTambah, btnHapus, btnBayar, btnBersihkan;

    private StokService stokService;
    private KasirService kasirService;
    private List<Pelanggan> daftarMember;
    private Pelanggan pelangganAktif;
    private final NumberFormat rupiahFmt = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public KasirFrame() {
        initData();
        initComponents();
        muatTabelProduk();
    }

    private void initData() {
        stokService = new StokService();
        if (stokService.getDaftarProduk().isEmpty()) {
            stokService.tambahProdukBaru(new Produk("P001", "Laptop ASUS ROG", 15000000, 5));
            stokService.tambahProdukBaru(new Produk("P002", "Mouse Gaming RGB", 500000, 10));
            stokService.tambahProdukBaru(new Produk("P003", "Keyboard Mech", 850000, 7));
            stokService.tambahProdukBaru(new Produk("P004", "Monitor 24 inch", 2800000, 4));
            stokService.tambahProdukBaru(new Produk("P005", "Headset Pro", 350000, 12));
        }
        kasirService = new KasirService();
        daftarMember = JsonUtil.bacaDariJson("data/pelanggan.json",
            new TypeToken<List<Pelanggan>>(){}.getType());
        if (daftarMember == null) daftarMember = new ArrayList<>();
    }

    private void initComponents() {
        setTitle("Sistem Kasir — Toko Elektronik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // HEADER
        JPanel pHeader = new JPanel(new BorderLayout());
        pHeader.setBackground(new Color(30, 30, 47));
        pHeader.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel lblJudul = new JLabel("  SISTEM KASIR TOKO ELEKTRONIK");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblJudul.setForeground(new Color(99, 202, 183));
        pHeader.add(lblJudul, BorderLayout.WEST);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(200, 80, 80));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        pHeader.add(btnLogout, BorderLayout.EAST);
        mainPanel.add(pHeader, BorderLayout.NORTH);

        // KIRI: Daftar Produk
        JPanel pKiri = new JPanel(new BorderLayout(0, 8));
        pKiri.setBackground(new Color(245, 247, 250));
        pKiri.setPreferredSize(new Dimension(400, 0));
        pKiri.add(buatLabelSection("Daftar Produk"), BorderLayout.NORTH);

        modelProduk = new DefaultTableModel(
            new String[]{"ID", "Nama Produk", "Harga", "Stok"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduk = new JTable(modelProduk);
        styleTable(tblProduk);
        tblProduk.getColumnModel().getColumn(0).setPreferredWidth(55);
        tblProduk.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblProduk.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblProduk.getColumnModel().getColumn(3).setPreferredWidth(45);
        pKiri.add(new JScrollPane(tblProduk), BorderLayout.CENTER);

        JPanel pTambah = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pTambah.setBackground(new Color(245, 247, 250));
        JLabel lblQty = new JLabel("Qty:");
        lblQty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pTambah.add(lblQty);
        txtQty = new JTextField("1", 4);
        pTambah.add(txtQty);
        btnTambah = new JButton("+ Tambah ke Keranjang");
        styleButton(btnTambah, new Color(52, 152, 219));
        pTambah.add(btnTambah);
        pKiri.add(pTambah, BorderLayout.SOUTH);
        mainPanel.add(pKiri, BorderLayout.WEST);

        // KANAN
        JPanel pKanan = new JPanel(new BorderLayout(0, 10));
        pKanan.setBackground(new Color(245, 247, 250));

        // Info Pelanggan
        JPanel pPelanggan = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        pPelanggan.setBackground(Color.WHITE);
        pPelanggan.setBorder(BorderFactory.createTitledBorder("Info Pelanggan"));

        JLabel lblNama = new JLabel("Nama:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pPelanggan.add(lblNama);
        txtNamaPelanggan = new JTextField(14);
        pPelanggan.add(txtNamaPelanggan);

        JLabel lblTipe = new JLabel("Tipe:");
        lblTipe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pPelanggan.add(lblTipe);
        cmbTipeMember = new JComboBox<>(new String[]{"REGULAR", "PREMIUM", "VIP"});
        pPelanggan.add(cmbTipeMember);

        JButton btnSetPelanggan = new JButton("Set Pelanggan");
        styleButton(btnSetPelanggan, new Color(39, 174, 96));
        pPelanggan.add(btnSetPelanggan);

        lblPelangganInfo = new JLabel("Belum ada pelanggan");
        lblPelangganInfo.setForeground(new Color(100, 100, 120));
        lblPelangganInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        pPelanggan.add(lblPelangganInfo);
        pKanan.add(pPelanggan, BorderLayout.NORTH);

        // Keranjang
        JPanel pKeranjang = new JPanel(new BorderLayout(0, 6));
        pKeranjang.setBackground(new Color(245, 247, 250));
        pKeranjang.add(buatLabelSection("Keranjang Belanja"), BorderLayout.NORTH);

        modelKeranjang = new DefaultTableModel(
            new String[]{"Nama Produk", "Harga", "Qty", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKeranjang = new JTable(modelKeranjang);
        styleTable(tblKeranjang);
        pKeranjang.add(new JScrollPane(tblKeranjang), BorderLayout.CENTER);

        JPanel pAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pAksi.setBackground(new Color(245, 247, 250));
        btnHapus = new JButton("Hapus Item");
        styleButton(btnHapus, new Color(231, 76, 60));
        btnBersihkan = new JButton("Bersihkan Semua");
        styleButton(btnBersihkan, new Color(149, 165, 166));
        pAksi.add(btnHapus);
        pAksi.add(btnBersihkan);
        pKeranjang.add(pAksi, BorderLayout.SOUTH);
        pKanan.add(pKeranjang, BorderLayout.CENTER);

        // Panel Bayar
        JPanel pBayar = new JPanel(new GridBagLayout());
        pBayar.setBackground(Color.WHITE);
        pBayar.setBorder(BorderFactory.createTitledBorder("Pembayaran"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4, 8, 4, 8);
        g.anchor = GridBagConstraints.WEST;

        g.gridx = 0; g.gridy = 0;
        pBayar.add(labelPlain("Metode Bayar:"), g);
        g.gridx = 1;
        cmbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "E-Wallet", "Kartu"});
        pBayar.add(cmbMetodeBayar, g);

        g.gridx = 0; g.gridy = 1;
        pBayar.add(labelPlain("Total Kotor:"), g);
        g.gridx = 1;
        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pBayar.add(lblTotal, g);

        g.gridx = 0; g.gridy = 2;
        pBayar.add(labelPlain("Diskon:"), g);
        g.gridx = 1;
        lblDiskon = new JLabel("Rp 0");
        lblDiskon.setForeground(new Color(39, 174, 96));
        lblDiskon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pBayar.add(lblDiskon, g);

        g.gridx = 0; g.gridy = 3;
        JLabel lblBayarTitle = new JLabel("TOTAL BAYAR:");
        lblBayarTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pBayar.add(lblBayarTitle, g);
        g.gridx = 1;
        lblBayar = new JLabel("Rp 0");
        lblBayar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBayar.setForeground(new Color(231, 76, 60));
        pBayar.add(lblBayar, g);

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        btnBayar = new JButton("BAYAR SEKARANG");
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBayar.setBackground(new Color(99, 202, 183));
        btnBayar.setForeground(new Color(30, 30, 47));
        btnBayar.setFocusPainted(false);
        btnBayar.setBorderPainted(false);
        btnBayar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBayar.setPreferredSize(new Dimension(0, 42));
        pBayar.add(btnBayar, g);
        pKanan.add(pBayar, BorderLayout.SOUTH);

        mainPanel.add(pKanan, BorderLayout.CENTER);
        add(mainPanel);

        // EVENT LISTENERS
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        btnHapus.addActionListener(e -> hapusItemKeranjang());
        btnBersihkan.addActionListener(e -> bersihkanKeranjang());
        btnBayar.addActionListener(e -> prosesBayar());
        btnSetPelanggan.addActionListener(e -> setPelanggan());
        tblProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) tambahKeKeranjang();
            }
        });

        setVisible(true);
    }

    private void muatTabelProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : stokService.getDaftarProduk()) {
            modelProduk.addRow(new Object[]{
                p.getIdProduk(),
                p.getNama(),
                rupiahFmt.format(p.getHarga()),
                p.getStok()
            });
        }
    }

    private void setPelanggan() {
        String nama = txtNamaPelanggan.getText().trim();
        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan tidak boleh kosong!");
            return;
        }
        String tipe = (String) cmbTipeMember.getSelectedItem();

        // Cari existing, tapi update tipenya sesuai combobox
        pelangganAktif = null;
        for (Pelanggan p : daftarMember) {
            if (p.getNama().equalsIgnoreCase(nama)) {
                pelangganAktif = p;
                break;
            }
        }
        if (pelangganAktif == null) {
            pelangganAktif = new Pelanggan("M-" + System.currentTimeMillis(), nama, tipe);
            daftarMember.add(pelangganAktif);
        } else {
            pelangganAktif.setTipeMember(tipe);
        }

        // Buat kasirService baru dulu, baru set pelanggan & diskon
        kasirService = new KasirService();
        modelKeranjang.setRowCount(0);

        kasirService.setPelanggan(pelangganAktif);
        if (!tipe.equalsIgnoreCase("REGULAR")) {
            kasirService.setDiskonStrategi(new DiskonMemberService());
        } else {
            kasirService.setDiskonStrategi(null);
        }

        JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);
        lblPelangganInfo.setText(pelangganAktif.getNama() + " | " + tipe + " | Poin: " + pelangganAktif.getPoin());
        updateRingkasanHarga();
    }

    private void tambahKeKeranjang() {
        int row = tblProduk.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih produk dari tabel terlebih dahulu.");
            return;
        }
        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Qty harus berupa angka positif.");
            return;
        }
        String idProduk = (String) modelProduk.getValueAt(row, 0);
        try {
            stokService.validasiStokBarang(idProduk, qty);
            Produk p = stokService.cariProdukBerdasarId(idProduk);
            kasirService.tambahKeKeranjang(p, qty);
            modelKeranjang.addRow(new Object[]{
                p.getNama(),
                rupiahFmt.format(p.getHarga()),
                qty,
                rupiahFmt.format(p.getHarga() * qty)
            });
            updateRingkasanHarga();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void hapusItemKeranjang() {
        int row = tblKeranjang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih item di keranjang yang ingin dihapus.");
            return;
        }
        kasirService.getKeranjangBelanja().remove(row);
        modelKeranjang.removeRow(row);
        updateRingkasanHarga();
    }

    private void bersihkanKeranjang() {
        kasirService = new KasirService();
        if (pelangganAktif != null) {
            kasirService.setPelanggan(pelangganAktif);
            if (!pelangganAktif.getTipeMember().equalsIgnoreCase("REGULAR")) {
                kasirService.setDiskonStrategi(new DiskonMemberService());
            }
        }
        modelKeranjang.setRowCount(0);
        updateRingkasanHarga();
    }

    private void updateRingkasanHarga() {
        double kotor = kasirService.hitungTotalKotor();
        double bersih = kasirService.hitungTotalTagihan();
        double diskon = kotor - bersih;
        lblTotal.setText(rupiahFmt.format(kotor));
        lblDiskon.setText("- " + rupiahFmt.format(diskon));
        lblBayar.setText(rupiahFmt.format(bersih));
    }

    private void prosesBayar() {
        if (kasirService.getKeranjangBelanja().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!");
            return;
        }
        if (pelangganAktif == null) {
            int opt = JOptionPane.showConfirmDialog(this,
                "Pelanggan belum diset. Lanjut sebagai tamu?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
            if (opt != JOptionPane.YES_OPTION) return;
            pelangganAktif = new Pelanggan("GUEST-" + System.currentTimeMillis(), "Tamu", "REGULAR");
            kasirService.setPelanggan(pelangganAktif);
        }

        String metodePilihan = (String) cmbMetodeBayar.getSelectedItem();
        MetodePembayaran metode;
        double jumlahBayar;
        double totalBersih = kasirService.hitungTotalTagihan();

        if ("Tunai".equals(metodePilihan)) {
            String input = JOptionPane.showInputDialog(this,
                "Total tagihan: " + rupiahFmt.format(totalBersih) + "\nMasukkan jumlah uang tunai:");
            if (input == null) return;
            try {
                jumlahBayar = Double.parseDouble(input.replace(",", "").replace(".", ""));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah tidak valid.");
                return;
            }
            metode = new PembayaranTunai();
        } else if ("E-Wallet".equals(metodePilihan)) {
            jumlahBayar = totalBersih;
            metode = new PembayaranEWallet();
        } else {
            jumlahBayar = totalBersih;
            metode = new PembayaranKartu();
        }

        try {
            kasirService.selesaikanTransaksi(metode, jumlahBayar);
            List<model.Transaksi> riwayat = kasirService.getRiwayatPenjualan();
            model.Transaksi trx = riwayat.get(riwayat.size() - 1);

            JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);
            stokService.simpanPerubahan();

            new StrukDialog(this, trx, pelangganAktif, jumlahBayar, metode.getKembalian(), metodePilihan);

            // Reset setelah bayar
            pelangganAktif = null;
            lblPelangganInfo.setText("Belum ada pelanggan");
            txtNamaPelanggan.setText("");
            kasirService = new KasirService();
            modelKeranjang.setRowCount(0);
            muatTabelProduk();
            updateRingkasanHarga();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel buatLabelSection(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(50, 50, 80));
        lbl.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        return lbl;
    }

    private JLabel labelPlain(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private void styleTable(JTable tbl) {
        tbl.setRowHeight(26);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tbl.getTableHeader().setBackground(new Color(30, 30, 47));
        tbl.getTableHeader().setForeground(Color.WHITE);
        tbl.setSelectionBackground(new Color(99, 202, 183));
        tbl.setSelectionForeground(Color.BLACK);
        tbl.setGridColor(new Color(220, 220, 230));
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}