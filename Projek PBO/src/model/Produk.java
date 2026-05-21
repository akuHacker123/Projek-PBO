package model;

public class Produk {
    private String idProduk;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String idProduk, String nama, double harga, int stok) {
        this.idProduk = idProduk;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    // Enkapsulasi: Getter dan Setter
    public String getIdProduk() { return idProduk; }
    public String getNama() { return nama; }
    
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
    
    // Fungsi pembantu untuk kurangi stok
    public void kurangiStok(int jumlah) {
        this.stok -= jumlah;
    }
}