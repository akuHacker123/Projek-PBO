package interfaces;

public interface MetodePembayaran {
    void prosesBayar(double totalTagihan, double jumlahBayar) throws exception.PembayaranKurangException;
    double getKembalian();
}