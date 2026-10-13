package com.example.ngopidiweklokal;

import java.io.Serializable;

public class Tempat implements Serializable {
    private String nama, lokasi, deskripsi, imageResName, linkGmaps, menu, jenis;
    private double latitude, longitude;

    // Atribut Baru untuk Fitur Filter UX Premium
    private String rating, harga, jamBuka, fasilitas;

    public Tempat() {}

    public Tempat(String nama, String lokasi, String deskripsi, String imageResName, String linkGmaps, String menu, String jenis, double latitude, double longitude, String rating, String harga, String jamBuka, String fasilitas) {
        this.nama = nama;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.imageResName = imageResName;
        this.linkGmaps = linkGmaps;
        this.menu = menu;
        this.jenis = jenis;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.harga = harga;
        this.jamBuka = jamBuka;
        this.fasilitas = fasilitas;
    }

    // Getter dan Setter
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getImageResName() { return imageResName; }
    public void setImageResName(String imageResName) { this.imageResName = imageResName; }

    public String getLinkGmaps() { return linkGmaps; }
    public void setLinkGmaps(String linkGmaps) { this.linkGmaps = linkGmaps; }

    public String getMenu() { return menu; }
    public void setMenu(String menu) { this.menu = menu; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getHarga() { return harga; }
    public void setHarga(String harga) { this.harga = harga; }

    public String getJamBuka() { return jamBuka; }
    public void setJamBuka(String jamBuka) { this.jamBuka = jamBuka; }

    public String getFasilitas() { return fasilitas; }
    public void setFasilitas(String fasilitas) { this.fasilitas = fasilitas; }
}