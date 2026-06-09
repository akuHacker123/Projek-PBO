/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileReader;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private User userAktif;
    private JPanel contentPanel;
    private String activeMenu = "Kelola Produk";

    private static final Color BG = new Color(248, 248, 248);
    private static final Color SIDEBAR = new Color(40, 40, 40);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color RED = new Color(200, 70, 70);

    private final Gson gson = new Gson();
    private final NumberFormat rupiahFmt =
            NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public AdminDashboard(User user) {
        this.userAktif = user;
        setTitle("Dashboard Admin - Chikempruy Computer");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();

        new javax.swing.Timer(3000, e -> tampilPanel(activeMenu)).start();

        setVisible(true);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(210, 0));
        sidebar.setBorder(new EmptyBorder(24, 18, 18, 18));

        String[] menus = {
                "Kelola Produk",
                "Laporan Penjualan",
                "Data Member",
                "Kelola User"
        };

        for (String menu : menus) {
            JButton btn = menuButton(menu);
            btn.addActionListener(e -> tampilPanel(menu));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(14));
        }

        JButton btnLogout = menuButton("Keluar");
        btnLogout.setBackground(RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        contentPanel.setBorder(new EmptyBorder(28, 28, 28, 28));

        root.add(sidebar, BorderLayout.WEST);
        root.add(contentPanel, BorderLayout.CENTER);
        add(root);

        tampilPanel("Kelola Produk");
    }

    private void tampilPanel(String title) {
        activeMenu = title;
        contentPanel.removeAll();

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_DARK);

        JLabel lblSub = new JLabel("Data otomatis diperbarui dari file JSON.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_GRAY);

        header.add(lblTitle);
        header.add(Box.createVerticalStrut(5));
        header.add(lblSub);

        contentPanel.add(header, BorderLayout.NORTH);

        if (title.equals("Kelola Produk")) {
            contentPanel.add(panelProduk(), BorderLayout.CENTER);
        } else if (title.equals("Laporan Penjualan")) {
            contentPanel.add(panelTransaksi(), BorderLayout.CENTER);
        } else if (title.equals("Data Member")) {
            contentPanel.add(panelMember(), BorderLayout.CENTER);
        } else {
            contentPanel.add(panelUser(), BorderLayout.CENTER);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JScrollPane panelProduk() {
        String[] kolom = {"ID Produk", "Nama Produk", "Harga", "Stok"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Map<String, Object> p : bacaJson("data/produk.json")) {
            model.addRow(new Object[]{
                    p.get("idProduk"),
                    p.get("nama"),
                    formatRp(p.get("harga")),
                    angka(p.get("stok"))
            });
        }

        return scrollTable(model);
    }

    private JScrollPane panelMember() {
        String[] kolom = {"ID Member", "Nama", "Tipe Member", "Poin"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Map<String, Object> m : bacaJson("data/pelanggan.json")) {
            model.addRow(new Object[]{
                    m.get("idPelanggan"),
                    m.get("nama"),
                    m.get("tipeMember"),
                    angka(m.get("poin"))
            });
        }

        return scrollTable(model);
    }

    private JScrollPane panelUser() {
        String[] kolom = {"ID User", "Nama Lengkap", "Username", "Role"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Map<String, Object> u : bacaJson("data/users.json")) {
            model.addRow(new Object[]{
                    u.get("idUser"),
                    u.get("namaLengkap"),
                    u.get("username"),
                    u.get("role")
            });
        }

        return scrollTable(model);
    }

    private JScrollPane panelTransaksi() {
        String[] kolom = {"ID Transaksi", "Tanggal", "Jumlah Item", "Total Bersih", "Metode"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);

        for (Map<String, Object> t : bacaJson("data/transaksi.json")) {
            List<?> items = (List<?>) t.get("listBelanjaan");

            Map<?, ?> metode = (Map<?, ?>) t.get("metodeYangDigunakan");
            String metodeNama = "-";
            if (metode != null && metode.get("type") != null) {
                metodeNama = metode.get("type").toString();
            }

            model.addRow(new Object[]{
                    t.get("idTransaksi"),
                    t.get("tanggal"),
                    items == null ? 0 : items.size(),
                    formatRp(t.get("totalBersih")),
                    metodeNama
            });
        }

        return scrollTable(model);
    }

    private JScrollPane scrollTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setEnabled(false);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new CompoundBorder(
                new EmptyBorder(22, 0, 0, 0),
                new LineBorder(new Color(225, 225, 225))
        ));
        scroll.getViewport().setBackground(WHITE);
        return scroll;
    }

    private List<Map<String, Object>> bacaJson(String path) {
        try (FileReader reader = new FileReader(path)) {
            java.lang.reflect.Type tipeData =
                    new TypeToken<List<Map<String, Object>>>() {}.getType();

            List<Map<String, Object>> data = gson.fromJson(reader, tipeData);
            return data == null ? new ArrayList<>() : data;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String formatRp(Object value) {
        if (value == null) return "Rp 0";
        double nominal = Double.parseDouble(value.toString());
        return rupiahFmt.format(nominal);
    }

    private int angka(Object value) {
        if (value == null) return 0;
        return (int) Double.parseDouble(value.toString());
    }

    private JButton menuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(new EmptyBorder(9, 12, 9, 12));
        return btn;
    }
}