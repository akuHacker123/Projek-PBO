package service;

import interfaces.MetodePembayaran;
import exception.PembayaranKurangException;

public class PembayaranTunai implements MetodePembayaran {
    private double kembalian;

    @Override
    public void prosesBayar(double totalTagihan, double jumlahBayar) throws PembayaranKurangException {
        if (jumlahBayar < totalTagihan) {
            double kurangnya = totalTagihan - jumlahBayar;
            throw new PembayaranKurangException("Uang tunai kurang sebesar Rp " + kurangnya);
        }
        this.kembalian = jumlahBayar - totalTagihan;
    }

    @Override
    public double getKembalian() {
        return this.kembalian;
    }
}