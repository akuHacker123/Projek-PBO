package util; 

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import interfaces.MetodePembayaran;
import service.PembayaranTunai;
import service.PembayaranEWallet;
import service.PembayaranKartu;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    private static final Gson gson;

    static {
        RuntimeTypeAdapterFactory<MetodePembayaran> typeAdapterFactory = RuntimeTypeAdapterFactory
                .of(MetodePembayaran.class, "type")
                .registerSubtype(PembayaranTunai.class, "Tunai")
                .registerSubtype(PembayaranEWallet.class, "EWallet")
                .registerSubtype(PembayaranKartu.class, "Kartu");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(typeAdapterFactory)
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
    }

    public static <T> void simpanKeJson(String pathFile, List<T> data) {
        try (Writer writer = new FileWriter(pathFile)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data ke " + pathFile + ": " + e.getMessage());
        }
    }

    public static <T> List<T> bacaDariJson(String pathFile, Type typeToken) {
        File file = new File(pathFile);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(file)) {
            List<T> data = gson.fromJson(reader, typeToken);
            return data != null ? data : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Gagal membaca data dari " + pathFile + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}