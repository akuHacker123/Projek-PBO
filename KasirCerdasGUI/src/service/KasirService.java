package service;

import model.*; 
import interfaces.MetodePembayaran;
import interfaces.DiskonStrategi;
import exception.PembayaranKurangException;
import util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class KasirService {
    private static final String FILE_PATH_TX = "data/transaksi.json";
    private List<ItemKeranjang> keranjangBelanja = new ArrayList<>();
    private List<Transaksi> riwayatPenjualan;
    private Pelanggan pelangganAktif = null;
    private DiskonStrategi diskonStrategi = null;

    public KasirService() {
        // Otomatis load riwayat transaksi lama dari JSON
        this.riwayatPenjualan = JsonUtil.bacaDariJson(FILE_PATH_TX, new TypeToken<List<Transaksi>>(){}.getType());
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelangganAktif = pelanggan;
    }

    public void setDiskonStrategi(DiskonStrategi diskonStrategi) {
        this.diskonStrategi = diskonStrategi;
    }

    public void tambahKeKeranjang(Produk produk, int kuantitas) {
        keranjangBelanja.add(new ItemKeranjang(produk, kuantitas));
    }

    public double hitungTotalKotor() {
        double total = 0;
        for (ItemKeranjang item : keranjangBelanja) {
            total += item.getSubTotal();
        }
        return total;
    }

    public double hitungTotalTagihan() {
        double totalKotor = hitungTotalKotor();
        if (diskonStrategi != null) {
            double potongan = diskonStrategi.hitungDiskon(totalKotor, pelangganAktif);
            return totalKotor - potongan;
        }
        return totalKotor;
    }

    public void selesaikanTransaksi(MetodePembayaran metode, double jumlahBayar) throws PembayaranKurangException {
        double totalKotor = hitungTotalKotor();
        double totalBersih = hitungTotalTagihan();
        double besarDiskon = totalKotor - totalBersih;
        
        metode.prosesBayar(totalBersih, jumlahBayar);

        for (ItemKeranjang item : keranjangBelanja) {
            item.getProduk().kurangiStok(item.getKuantitas());
        }

        if (pelangganAktif != null) {
            int poinBaru = (int) (totalBersih / 10000);
            pelangganAktif.tambahPoin(poinBaru);
        }

        String idNota = "TX-" + System.currentTimeMillis();
        Transaksi notaBaru = new Transaksi(idNota, new ArrayList<>(keranjangBelanja), totalKotor, besarDiskon, metode);
        riwayatPenjualan.add(notaBaru);

        // DISIMPAN KE FILE JSON
        JsonUtil.simpanKeJson(FILE_PATH_TX, riwayatPenjualan);
    }

    public List<ItemKeranjang> getKeranjangBelanja() { return keranjangBelanja; }
    public List<Transaksi> getRiwayatPenjualan() { return riwayatPenjualan; }

    public void bersihkanKeranjang() {
        keranjangBelanja.clear();
        pelangganAktif = null;
        diskonStrategi = null;
    }
}