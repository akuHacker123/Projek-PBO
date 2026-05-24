package controller;

import model.*;
import service.*;
import exception.*;
import interfaces.MetodePembayaran;
import interfaces.DiskonStrategi;
import util.JsonUtil;
import view.KasirView;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class KasirController {

    private KasirView view;
    private StokService stokGudang;
    private KasirService mesinKasir;
    private List<Pelanggan> daftarMember;
    private Pelanggan pembeli;

    public KasirController() {
        this.view = new KasirView();
        jalankan();
    }

    private void jalankan() {
        view.tampilkanHeader();

        // 1. Load Stok
        stokGudang = new StokService();
        if (stokGudang.getDaftarProduk().isEmpty()) {
            stokGudang.tambahProdukBaru(new Produk("P001", "Laptop ASUS ROG", 15000000, 5));
            stokGudang.tambahProdukBaru(new Produk("P002", "Mouse Gaming RGB", 500000, 10));
            stokGudang.tambahProdukBaru(new Produk("P003", "Keyboard Mech", 850000, 7));
        }

        // 2. Load Pelanggan
        daftarMember = JsonUtil.bacaDariJson("data/pelanggan.json", new TypeToken<List<Pelanggan>>(){}.getType());
        String namaTarget = "GalSkuy";
        pembeli = null;

        for (Pelanggan p : daftarMember) {
            if (p.getNama().equalsIgnoreCase(namaTarget)) {
                pembeli = p;
                break;
            }
        }

        if (pembeli == null) {
            pembeli = new Pelanggan("M-REG-" + System.currentTimeMillis(), namaTarget, "REGULAR");
            daftarMember.add(pembeli);
            JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);
        }

        // 3. Setup Kasir
        mesinKasir = new KasirService();
        if (!pembeli.getTipeMember().equalsIgnoreCase("REGULAR")) {
            mesinKasir.setDiskonStrategi(new DiskonMemberService());
        }
        mesinKasir.setPelanggan(pembeli);

        view.tampilkanInfoPelanggan(pembeli);

        try {
            // 4. Scan Barang
            stokGudang.validasiStokBarang("P001", 1);
            mesinKasir.tambahKeKeranjang(stokGudang.cariProdukBerdasarId("P001"), 1);
            view.tampilkanScanBarang(stokGudang.cariProdukBerdasarId("P001").getNama());

            double totalBersih = mesinKasir.hitungTotalTagihan();
            view.tampilkanTotalTagihan(totalBersih);

            // 5. Proses Bayar
            MetodePembayaran opsiBayar = new PembayaranEWallet();
            double uangDibayar = 13500000;
            view.tampilkanProsesBayar(uangDibayar);
            mesinKasir.selesaikanTransaksi(opsiBayar, uangDibayar);
            view.tampilkanKembalian(opsiBayar.getKembalian());

            // 6. Simpan
            stokGudang.simpanPerubahan();
            JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);

            view.tampilkanSukses(pembeli);

        } catch (ProdukTidakDitemukanException | StokTidakCukupException | PembayaranKurangException e) {
            view.tampilkanError(e.getMessage());
        }
    }
}