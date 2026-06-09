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
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    // Palet warna
    private static final Color BG = new Color(248, 248, 248);
    private static final Color WHITE = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color ACCENT = new Color(70, 130, 180);
    private static final Color ACCENT_LIGHT = new Color(232, 242, 252);
    private static final Color RED = new Color(210, 70, 70);
    private static final Color GREEN = new Color(60, 160, 90);
    private static final Color HEADER_BG = new Color(52, 52, 52);

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
        setTitle("Kasir — Toko Elektronik");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1020, 700);
        setLocationRelativeTo(null);
        setBackground(BG);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // TOPBAR
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(HEADER_BG);
        topbar.setPreferredSize(new Dimension(0, 50));
        topbar.setBorder(new EmptyBorder(0, 20, 0, 16));

        JLabel lblNamaApp = new JLabel("Toko Elektronik");
        lblNamaApp.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNamaApp.setForeground(Color.WHITE);
        topbar.add(lblNamaApp, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        topRight.setOpaque(false);
        JLabel lblKasir = new JLabel("Kasir: Admin");
        lblKasir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblKasir.setForeground(new Color(180, 180, 180));
        topRight.add(lblKasir);

        JButton btnLogout = flatButton("Keluar", new Color(180, 60, 60), Color.WHITE);
        btnLogout.setPreferredSize(new Dimension(70, 28));
        btnLogout.addActionListener(e -> { dispose(); new LoginFrame(); });
        topRight.add(btnLogout);
        topbar.add(topRight, BorderLayout.EAST);
        root.add(topbar, BorderLayout.NORTH);

        // BODY
        JPanel body = new JPanel(new BorderLayout(12, 0));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(14, 14, 14, 14));

        // PANEL KIRI: produk
        JPanel pKiri = new JPanel(new BorderLayout(0, 10));
        pKiri.setBackground(BG);
        pKiri.setPreferredSize(new Dimension(390, 0));

        JLabel lblProdukTitle = sectionTitle("Daftar Produk");
        pKiri.add(lblProdukTitle, BorderLayout.NORTH);

        modelProduk = new DefaultTableModel(new String[]{"ID", "Nama Produk", "Harga", "Stok"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblProduk = buatTabel(modelProduk);
        tblProduk.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblProduk.getColumnModel().getColumn(1).setPreferredWidth(155);
        tblProduk.getColumnModel().getColumn(2).setPreferredWidth(105);
        tblProduk.getColumnModel().getColumn(3).setPreferredWidth(40);

        JScrollPane scrollProduk = buatScroll(tblProduk);
        pKiri.add(scrollProduk, BorderLayout.CENTER);

        JPanel pQty = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        pQty.setBackground(BG);
        pQty.setBorder(new EmptyBorder(4, 0, 0, 0));
        JLabel lQty = new JLabel("Qty:");
        lQty.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lQty.setForeground(TEXT_DARK);
        txtQty = new JTextField("1", 5);
        styleInput(txtQty);
        btnTambah = flatButton("+ Tambah", ACCENT, Color.WHITE);
        pQty.add(lQty);
        pQty.add(txtQty);
        pQty.add(btnTambah);
        pKiri.add(pQty, BorderLayout.SOUTH);
        body.add(pKiri, BorderLayout.WEST);

        // PANEL KANAN
        JPanel pKanan = new JPanel(new BorderLayout(0, 12));
        pKanan.setBackground(BG);

        // Info pelanggan
        JPanel pPelanggan = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pPelanggan.setBackground(WHITE);
        pPelanggan.setBorder(roundedBorder("Pelanggan"));

        JLabel lNama = smallLabel("Nama");
        txtNamaPelanggan = new JTextField(13);
        styleInput(txtNamaPelanggan);

        JLabel lTipe = smallLabel("Tipe");
        cmbTipeMember = new JComboBox<>(new String[]{"REGULAR", "PREMIUM", "VIP"});
        styleCombo(cmbTipeMember);

        JButton btnSet = flatButton("Set", GREEN, Color.WHITE);
        btnSet.setPreferredSize(new Dimension(55, 28));

        lblPelangganInfo = new JLabel("—");
        lblPelangganInfo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPelangganInfo.setForeground(TEXT_GRAY);

        pPelanggan.add(lNama);
        pPelanggan.add(txtNamaPelanggan);
        pPelanggan.add(lTipe);
        pPelanggan.add(cmbTipeMember);
        pPelanggan.add(btnSet);
        pPelanggan.add(lblPelangganInfo);
        pKanan.add(pPelanggan, BorderLayout.NORTH);

        // Keranjang
        JPanel pKeranjang = new JPanel(new BorderLayout(0, 6));
        pKeranjang.setBackground(BG);
        pKeranjang.add(sectionTitle("Keranjang"), BorderLayout.NORTH);

        modelKeranjang = new DefaultTableModel(new String[]{"Produk", "Harga Satuan", "Qty", "Subtotal"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKeranjang = buatTabel(modelKeranjang);
        pKeranjang.add(buatScroll(tblKeranjang), BorderLayout.CENTER);

        JPanel pAksi = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        pAksi.setBackground(BG);
        btnHapus = flatButton("Hapus", RED, Color.WHITE);
        btnBersihkan = flatButton("Kosongkan", new Color(160, 160, 160), Color.WHITE);
        pAksi.add(btnHapus);
        pAksi.add(btnBersihkan);
        pKeranjang.add(pAksi, BorderLayout.SOUTH);
        pKanan.add(pKeranjang, BorderLayout.CENTER);

        // Panel ringkasan + bayar
        JPanel pBayar = new JPanel(new BorderLayout(16, 0));
        pBayar.setBackground(WHITE);
        pBayar.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_COLOR),
            new EmptyBorder(12, 14, 12, 14)
        ));

        // Ringkasan harga
        JPanel pRingkasan = new JPanel();
        pRingkasan.setLayout(new BoxLayout(pRingkasan, BoxLayout.Y_AXIS));
        pRingkasan.setBackground(WHITE);

        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTotal.setForeground(TEXT_GRAY);

        lblDiskon = new JLabel("Rp 0");
        lblDiskon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDiskon.setForeground(GREEN);

        lblBayar = new JLabel("Rp 0");
        lblBayar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBayar.setForeground(TEXT_DARK);

        pRingkasan.add(barisSummary("Subtotal", lblTotal));
        pRingkasan.add(Box.createVerticalStrut(3));
        pRingkasan.add(barisSummary("Diskon", lblDiskon));
        pRingkasan.add(Box.createVerticalStrut(6));
        pRingkasan.add(new JSeparator());
        pRingkasan.add(Box.createVerticalStrut(6));
        pRingkasan.add(barisSummary("Total", lblBayar));
        pBayar.add(pRingkasan, BorderLayout.CENTER);

        // Metode + tombol bayar
        JPanel pTombolBayar = new JPanel();
        pTombolBayar.setLayout(new BoxLayout(pTombolBayar, BoxLayout.Y_AXIS));
        pTombolBayar.setBackground(WHITE);

        JPanel pMetode = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        pMetode.setBackground(WHITE);
        JLabel lMetode = smallLabel("Metode");
        cmbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "E-Wallet", "Kartu"});
        styleCombo(cmbMetodeBayar);
        pMetode.add(lMetode);
        pMetode.add(cmbMetodeBayar);
        pTombolBayar.add(pMetode);
        pTombolBayar.add(Box.createVerticalStrut(8));

        btnBayar = new JButton("Bayar");
        btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBayar.setBackground(ACCENT);
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setFocusPainted(false);
        btnBayar.setBorderPainted(false);
        btnBayar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBayar.setPreferredSize(new Dimension(130, 38));
        btnBayar.setMaximumSize(new Dimension(130, 38));
        btnBayar.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pTombolBayar.add(btnBayar);
        pBayar.add(pTombolBayar, BorderLayout.EAST);
        pKanan.add(pBayar, BorderLayout.SOUTH);

        body.add(pKanan, BorderLayout.CENTER);
        root.add(body, BorderLayout.CENTER);
        add(root);

        // EVENT
        btnTambah.addActionListener(e -> tambahKeKeranjang());
        btnHapus.addActionListener(e -> hapusItemKeranjang());
        btnBersihkan.addActionListener(e -> bersihkanKeranjang());
        btnBayar.addActionListener(e -> prosesBayar());
        btnSet.addActionListener(e -> setPelanggan());
        tblProduk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) tambahKeKeranjang();
            }
        });

        setVisible(true);
    }

    // HELPER UI

    private JTable buatTabel(DefaultTableModel model) {
        JTable tbl = new JTable(model);
        tbl.setRowHeight(28);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.setBackground(WHITE);
        tbl.setForeground(TEXT_DARK);
        tbl.setGridColor(new Color(235, 235, 235));
        tbl.setShowVerticalLines(false);
        tbl.setIntercellSpacing(new Dimension(0, 1));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tbl.getTableHeader().setBackground(new Color(245, 245, 245));
        tbl.getTableHeader().setForeground(TEXT_GRAY);
        tbl.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tbl.setSelectionBackground(ACCENT_LIGHT);
        tbl.setSelectionForeground(TEXT_DARK);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.getColumnModel().getColumn(0).setCellRenderer(center);
        return tbl;
    }

    private JScrollPane buatScroll(JTable tbl) {
        JScrollPane sp = new JScrollPane(tbl);
        sp.setBorder(new LineBorder(BORDER_COLOR));
        sp.getViewport().setBackground(WHITE);
        return sp;
    }

    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    private JLabel smallLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);
        return lbl;
    }

    private JButton flatButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleInput(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setForeground(TEXT_DARK);
        tf.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR),
            new EmptyBorder(3, 7, 3, 7)
        ));
    }

    private void styleCombo(JComboBox<String> cmb) {
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmb.setBackground(WHITE);
    }

    private Border roundedBorder(String title) {
        return BorderFactory.createTitledBorder(
            new LineBorder(BORDER_COLOR),
            title,
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Segoe UI", Font.PLAIN, 11),
            TEXT_GRAY
        );
    }

    private JPanel barisSummary(String label, JLabel nilai) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(WHITE);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(TEXT_GRAY);
        p.add(lbl, BorderLayout.WEST);
        p.add(nilai, BorderLayout.EAST);
        return p;
    }

    // LOGIKA

    private void muatTabelProduk() {
        modelProduk.setRowCount(0);
        for (Produk p : stokService.getDaftarProduk()) {
            modelProduk.addRow(new Object[]{
                p.getIdProduk(), p.getNama(),
                rupiahFmt.format(p.getHarga()), p.getStok()
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
        pelangganAktif = null;
        for (Pelanggan p : daftarMember) {
            if (p.getNama().equalsIgnoreCase(nama)) { pelangganAktif = p; break; }
        }
        if (pelangganAktif == null) {
            pelangganAktif = new Pelanggan("M-" + System.currentTimeMillis(), nama, tipe);
            daftarMember.add(pelangganAktif);
        } else {
            pelangganAktif.setTipeMember(tipe);
        }
        kasirService = new KasirService();
        modelKeranjang.setRowCount(0);
        kasirService.setPelanggan(pelangganAktif);
        if (!tipe.equalsIgnoreCase("REGULAR")) {
            kasirService.setDiskonStrategi(new DiskonMemberService());
        } else {
            kasirService.setDiskonStrategi(null);
        }
        JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);
        lblPelangganInfo.setText(pelangganAktif.getNama() + "  ·  " + tipe + "  ·  " + pelangganAktif.getPoin() + " poin");
        updateRingkasanHarga();
    }

    private void tambahKeKeranjang() {
        int row = tblProduk.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih produk dulu."); return; }
        int qty;
        try {
            qty = Integer.parseInt(txtQty.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Qty harus angka positif."); return;
        }
        String idProduk = (String) modelProduk.getValueAt(row, 0);
        try {
            stokService.validasiStokBarang(idProduk, qty);
            Produk p = stokService.cariProdukBerdasarId(idProduk);
            kasirService.tambahKeKeranjang(p, qty);
            modelKeranjang.addRow(new Object[]{
                p.getNama(), rupiahFmt.format(p.getHarga()),
                qty, rupiahFmt.format(p.getHarga() * qty)
            });
            updateRingkasanHarga();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void hapusItemKeranjang() {
        int row = tblKeranjang.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus."); return; }
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
        lblDiskon.setText(diskon > 0 ? "- " + rupiahFmt.format(diskon) : rupiahFmt.format(0));
        lblBayar.setText(rupiahFmt.format(bersih));
    }

    private void prosesBayar() {
        if (kasirService.getKeranjangBelanja().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong!"); return;
        }
        if (pelangganAktif == null) {
            int opt = JOptionPane.showConfirmDialog(this,
                "Pelanggan belum diset. Lanjut sebagai tamu?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
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
                "Total: " + rupiahFmt.format(totalBersih) + "\nJumlah uang tunai:");
            if (input == null) return;
            try {
                jumlahBayar = Double.parseDouble(input.replace(",", "").replace(".", ""));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah tidak valid."); return;
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
            pelangganAktif = null;
            lblPelangganInfo.setText("—");
            txtNamaPelanggan.setText("");
            kasirService = new KasirService();
            modelKeranjang.setRowCount(0);
            muatTabelProduk();
            updateRingkasanHarga();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}