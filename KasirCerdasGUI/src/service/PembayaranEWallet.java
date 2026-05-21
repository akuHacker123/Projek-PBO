package service;

import interfaces.MetodePembayaran; 
import exception.PembayaranKurangException;

public class PembayaranEWallet implements MetodePembayaran {
    private double kembalian;

    @Override
    public void prosesBayar(double totalTagihan, double jumlahBayar) throws PembayaranKurangException {
        // Pembayaran E-Wallet/QRIS biasanya harus pas nilainya
        if (jumlahBayar < totalTagihan) {
            throw new PembayaranKurangException("Saldo E-Wallet tidak cukup! Kurang: Rp " + (totalTagihan - jumlahBayar));
        }
        // E-Wallet umumnya tidak ada kembalian tunai fisik
        this.kembalian = 0; 
    }

    @Override
    public double getKembalian() {
        return this.kembalian;
    }
}