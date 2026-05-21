package service;

import interfaces.DiskonStrategi;
import model.Pelanggan;

public class DiskonMemberService implements DiskonStrategi {
    @Override
    public double hitungDiskon(double totalHarga, Pelanggan pelanggan) {
        if (pelanggan == null) {
            return 0; // Tidak ada diskon untuk non-member
        }
        
        // Cek tipe member
        if (pelanggan.getTipeMember().equalsIgnoreCase("VIP")) {
            return totalHarga * 0.10; // Diskon 10% untuk VIP
        } else if (pelanggan.getTipeMember().equalsIgnoreCase("PREMIUM")) {
            return totalHarga * 0.05; // Diskon 5% untuk Premium
        }
        
        return 0; // Member regular tidak dapat diskon persentase
    }
}