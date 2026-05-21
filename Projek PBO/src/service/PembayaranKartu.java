package service;

import interfaces.MetodePembayaran;
import exception.PembayaranKurangException;

public class PembayaranKartu implements MetodePembayaran {
    private double kembalian;

    @Override
    public void prosesBayar(double totalTagihan, double jumlahBayar) throws PembayaranKurangException {
        // Pembayaran kartu (Debit/Kredit) memotong langsung sesuai nominal tagihan
        if (jumlahBayar < totalTagihan) {
            throw new PembayaranKurangException("Limit atau Saldo Rekening Kartu Kurang!");
        }
        this.kembalian = 0;
    }

    @Override
    public double getKembalian() {
        return this.kembalian;
    }
}