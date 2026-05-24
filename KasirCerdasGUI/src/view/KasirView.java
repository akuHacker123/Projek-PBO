package view;

import model.Pelanggan;

public class KasirView {

    public void tampilkanHeader() {
        System.out.println("=========================================");
        System.out.println("   SISTEM KASIR CERDAS DATABASE JSON     ");
        System.out.println("=========================================");
    }

    public void tampilkanInfoPelanggan(Pelanggan p) {
        System.out.println("Pelanggan : " + p.getNama() + " [" + p.getTipeMember() + "]");
        System.out.println("Poin Awal : " + p.getPoin());
        System.out.println("-----------------------------------------");
    }

    public void tampilkanScanBarang(String namaProduk) {
        System.out.println("-> Berhasil scan 1 unit " + namaProduk);
    }

    public void tampilkanTotalTagihan(double total) {
        System.out.println("Total Tagihan Setelah Diskon VIP: Rp " + total);
        System.out.println("-----------------------------------------");
    }

    public void tampilkanProsesBayar(double jumlah) {
        System.out.println("Memproses pembayaran E-Wallet senilai Rp " + jumlah);
    }

    public void tampilkanSukses(Pelanggan p) {
        System.out.println("\nTRANSAKSI SUKSES DAN DATABASE JSON DIPERBARUI!");
        System.out.println("Poin Member " + p.getNama() + " Sekarang: " + p.getPoin() + " Poin");
    }

    public void tampilkanError(String pesan) {
        System.out.println("\n[ERROR] Transaksi Gagal: " + pesan);
    }
    
    public void tampilkanKembalian(double kembalian) {
        if (kembalian > 0) {
            System.out.println("Kembalian: Rp " + kembalian);
        }
    }
}