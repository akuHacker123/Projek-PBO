package interfaces;

import model.Pelanggan;

// Interface cerdas untuk fleksibilitas strategi diskon (Polimorfisme)
public interface DiskonStrategi {
    double hitungDiskon(double totalHarga, Pelanggan pelanggan);
}