package com.tutor.cokinfo.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Postingan implements Serializable {
    private String id, judul, jenisCoklat, alamat, harga;
    private Bitmap foto;

    public Postingan(String id,String judul, String jenisCoklat, String alamat, String harga, Bitmap foto) {
        this.id = id;
        this.judul = judul;
        this.jenisCoklat = jenisCoklat;
        this.alamat = alamat;
        this.harga = harga;
        this.foto = foto;
    }
    public Postingan(String id,String judul, String jenisCoklat, String alamat, String harga) {
        this.id = id;
        this.judul = judul;
        this.jenisCoklat = jenisCoklat;
        this.alamat = alamat;
        this.harga = harga;
    }
    public void setFoto(Bitmap bitmap){
        this.foto = bitmap;
    }
    public String getId(){
        return id;
    }
    public String getJudul() {
        return judul;
    }

    public String getJenisCoklat() {
        return jenisCoklat;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getHarga() {
        return harga;
    }

    public Bitmap getFoto() {
        return foto;
    }
}
