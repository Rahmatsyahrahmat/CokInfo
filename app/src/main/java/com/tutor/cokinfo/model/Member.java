package com.tutor.cokinfo.model;

import java.io.Serializable;

public class Member implements Serializable {
    private String username, nama, tanggalLahir, jenisKelamin, nomorTelepon;

    public Member(String username, String nama, String tanggalLahir, String jenisKelamin, String nomorTelepon) {
        this.username = username;
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.nomorTelepon = nomorTelepon;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }
}
