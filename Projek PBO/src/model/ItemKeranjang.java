package model;

public class ItemKeranjang {
    private Produk produk;
    private int kuantitas;

    public ItemKeranjang(Produk produk, int kuantitas) {
        this.produk = produk;
        this.kuantitas = kuantitas;
    }

    public Produk getProduk() { return produk; }
    public int getKuantitas() { return kuantitas; }
    
    // Menghitung subtotal otomatis untuk item ini
    public double getSubTotal() {
        return produk.getHarga() * kuantitas;
    }
}