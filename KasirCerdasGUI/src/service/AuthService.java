/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.User;
import util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import java.security.MessageDigest;
import java.util.List;
/**
 *
 * @author Olivia Oktaviani
 */
public class AuthService {
    private static final String PATH = "data/users.json";
    private List<User> users;

    public AuthService() {
        users = JsonUtil.bacaDariJson(PATH, new TypeToken<List<User>>(){}.getType());
        if (users == null) users = new java.util.ArrayList<>();
    }

    /** Kembalikan User jika kredensial valid, null jika gagal */
    public User login(String username, String password) {
        String hash = sha256(password);
        return users.stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username) && u.getPasswordHash().equals(hash))
            .findFirst().orElse(null);
    }

    public void tambahUser(User u) {
        users.add(u);
        JsonUtil.simpanKeJson(PATH, users);
    }

    public List<User> getDaftarUser() { return users; }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte x : b) sb.append(String.format("%02x", x));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}