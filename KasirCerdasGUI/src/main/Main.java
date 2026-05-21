package main;

import model.*;
import service.*;
import exception.*;
import interfaces.MetodePembayaran;
import interfaces.DiskonStrategi;
import util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("   SISTEM KASIR CERDAS DATABASE JSON     ");
        System.out.println("=========================================");

        // 1. Load Gudang Stok via JSON
        StokService stokGudang = new StokService();
        
        if (stokGudang.getDaftarProduk().isEmpty()) {
            stokGudang.tambahProdukBaru(new Produk("P001", "Laptop ASUS ROG ", 15000000, 5));
            stokGudang.tambahProdukBaru(new Produk("P002", "Mouse Gaming RGB", 500000, 10));
            stokGudang.tambahProdukBaru(new Produk("P003", "Keyboard Mech", 850000, 7));
        }

        // 2. Load Pelanggan via JSON
        List<Pelanggan> daftarMember = JsonUtil.bacaDariJson("data/pelanggan.json", new TypeToken<List<Pelanggan>>(){}.getType());
        Pelanggan pembeli = null;
        
        // Tentukan Nama Target yang ingin disimulasikan sekarang
        String namaTarget = "GalSkuy"; 

        // Cari pelanggan berdasarkan namaTarget secara dinamis di dalam database JSON
        for (Pelanggan p : daftarMember) {
            if (p.getNama().equalsIgnoreCase(namaTarget)) {
                pembeli = p;
                break;
            }
        }
        
        // Jika namaTarget belum ada di file pelanggan.json, sistem otomatis menambahkannya sebagai member baru
        if (pembeli == null) {
            pembeli = new Pelanggan("M-VIP-02", namaTarget, "VIP");
            daftarMember.add(pembeli);
            JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember);
        }

        // 3. Siapkan Mesin Kasir
        KasirService mesinKasir = new KasirService();
        DiskonStrategi strategiDiskon = new DiskonMemberService();
        mesinKasir.setDiskonStrategi(strategiDiskon);
        mesinKasir.setPelanggan(pembeli);

        // Output nama mengambil langsung dari objek pembeli yang aktif (.getNama())
        System.out.println("Pelanggan : " + pembeli.getNama() + " [" + pembeli.getTipeMember() + "]");
        System.out.println("Poin Awal : " + pembeli.getPoin());
        System.out.println("-----------------------------------------");

        try {
            // 4. Kasir Scan Barang (Membeli 1 Laptop ASUS)
            stokGudang.validasiStokBarang("P001", 1);
            mesinKasir.tambahKeKeranjang(stokGudang.cariProdukBerdasarId("P001"), 1);
            System.out.println("-> Berhasil scan 1 unit " + stokGudang.cariProdukBerdasarId("P001").getNama());

            double totalBersih = mesinKasir.hitungTotalTagihan();
            System.out.println("Total Tagihan Setelah Diskon VIP: Rp " + totalBersih);

            // 5. Proses Bayar Pakai E-Wallet
            System.out.println("-----------------------------------------");
            MetodePembayaran opsiBayar = new PembayaranEWallet();
            double uangDibayar = 13500000; 
            
            System.out.println("Memproses pembayaran E-Wallet senilai Rp " + uangDibayar);
            mesinKasir.selesaikanTransaksi(opsiBayar, uangDibayar);
            
            // 6. SIMPAN SEMUA PERUBAHAN KE DATABASE JSON
            stokGudang.simpanPerubahan(); 
            JsonUtil.simpanKeJson("data/pelanggan.json", daftarMember); 

            System.out.println("\nTRANSAKSI SUKSES DAN DATABASE JSON DIPERBARUI!");
            // Menggunakan pembeli.getNama() agar pesan dinamis mengikuti variabel namaTarget
            System.out.println("Poin Member " + pembeli.getNama() + " Sekarang: " + pembeli.getPoin() + " Poin");

        } catch (ProdukTidakDitemukanException | StokTidakCukupException | PembayaranKurangException e) {
            System.out.println("\n[ERROR] Transaksi Gagal: " + e.getMessage());
        }
    }
}