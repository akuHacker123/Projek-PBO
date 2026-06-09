/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Olivia Oktaviani
 */
public class User {
    private String idUser, namaLengkap, username, passwordHash, role; // role: "ADMIN" | "KASIR"

    public User() {}
    public User(String idUser, String namaLengkap, String username, String passwordHash, String role) {
        this.idUser = idUser; this.namaLengkap = namaLengkap;
        this.username = username; this.passwordHash = passwordHash; this.role = role;
    }
    // Getters & Setters standar
    public String getIdUser()       { return idUser; }
    public String getNamaLengkap()  { return namaLengkap; }
    public String getUsername()     { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole()         { return role; }
    public void setTipeMember(String r) { this.role = r; }
}
