package model;

import interfaces.MetodePembayaran;
import java.util.Date;
import java.util.List;

public class Transaksi {
    private String idTransaksi;
    private Date tanggal;
    private List<ItemKeranjang> listBelanjaan;
    private double totalKotor;
    private double diskon;
    private double totalBersih;
    private MetodePembayaran metodeYangDigunakan;

    // Constructor untuk membuat nota/transaksi baru yang sah
    public Transaksi(String idTransaksi, List<ItemKeranjang> listBelanjaan, double totalKotor, double diskon, MetodePembayaran metodeYangDigunakan) {
        this.idTransaksi = idTransaksi;
        this.tanggal = new Date(); // otomatis mencatat tanggal dan jam detik ini
        this.listBelanjaan = listBelanjaan;
        this.totalKotor = totalKotor;
        this.diskon = diskon;
        this.totalBersih = totalKotor - diskon;
        this.metodeYangDigunakan = metodeYangDigunakan;
    }

    // Enkapsulasi: Getter untuk kebutuhan cetak struk di GUI NetBeans
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public List<ItemKeranjang> getListBelanjaan() {
        return listBelanjaan;
    }

    public double getTotalKotor() {
        return totalKotor;
    }

    public double getDiskon() {
        return diskon;
    }

    public double getTotalBersih() {
        return totalBersih;
    }

    public MetodePembayaran getMetodeYangDigunakan() {
        return metodeYangDigunakan;
    }
}