package service;

import model.Produk;
import exception.ProdukTidakDitemukanException;
import exception.StokTidakCukupException;
import util.JsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class StokService {
    private static final String FILE_PATH = "data/produk.json";
    private List<Produk> daftarProduk;

    public StokService() {
        // Otomatis load data dari JSON saat service dibuat
        this.daftarProduk = JsonUtil.bacaDariJson(FILE_PATH, new TypeToken<List<Produk>>(){}.getType());
    }

    public void tambahProdukBaru(Produk produk) {
        daftarProduk.add(produk);
        simpanPerubahan(); // Update file JSON
    }

    public void simpanPerubahan() {
        JsonUtil.simpanKeJson(FILE_PATH, daftarProduk);
    }

    public Produk cariProdukBerdasarId(String idProduk) throws ProdukTidakDitemukanException {
        for (Produk p : daftarProduk) {
            if (p.getIdProduk().equalsIgnoreCase(idProduk)) {
                return p;
            }
        }
        throw new ProdukTidakDitemukanException("Produk dengan ID '" + idProduk + "' tidak ada di sistem!");
    }

    public void validasiStokBarang(String idProduk, int jumlahBeli) 
            throws ProdukTidakDitemukanException, StokTidakCukupException {
        Produk p = cariProdukBerdasarId(idProduk);
        if (p.getStok() < jumlahBeli) {
            throw new StokTidakCukupException("Stok '" + p.getNama() + "' sisa " + p.getStok() + ", tidak cukup untuk beli " + jumlahBeli);
        }
    }

    public List<Produk> getDaftarProduk() {
        return daftarProduk;
    }
}