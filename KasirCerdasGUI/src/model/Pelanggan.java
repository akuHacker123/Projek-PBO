package model;

public class Pelanggan {
    private String idPelanggan;
    private String nama;
    private String tipeMember; // REGULAR, PREMIUM, VIP
    private int poin;

    public Pelanggan(String idPelanggan, String nama, String tipeMember) {
        this.idPelanggan = idPelanggan;
        this.nama = nama;
        this.tipeMember = tipeMember;
        this.poin = 0;
    } 

    public String getIdPelanggan() { return idPelanggan; }
    public String getNama() { return nama; }
    public String getTipeMember() { return tipeMember; }
    public int getPoin() { return poin; }

    public void tambahPoin(int poinBaru) {
        this.poin += poinBaru;
    }
}