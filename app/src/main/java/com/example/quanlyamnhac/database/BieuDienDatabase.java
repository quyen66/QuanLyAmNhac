package com.example.quanlyamnhac.database;

public class BieuDienDatabase {
    private String maBaiHat;
    private String maCaSi;
    private String tenCasi;
    private String ngayBieuDien;
    private String diaDiem;

    public BieuDienDatabase() {
    }

    public BieuDienDatabase(String maBaiHat, String maCaSi, String tenCasi, String ngayBieuDien, String diaDiem) {
        this.maCaSi = maCaSi;
        this.tenCasi = tenCasi;
        this.ngayBieuDien = ngayBieuDien;
        this.diaDiem = diaDiem;
        this.maBaiHat = maBaiHat;
    }

    public String getMaCaSi() {
        return maCaSi;
    }

    public void setMaCaSi(String maCaSi) {
        this.maCaSi = maCaSi;
    }

    public String getTenCasi() {
        return tenCasi;
    }

    public void setTenCasi(String tenCasi) {
        this.tenCasi = tenCasi;
    }

    public String getNgayBieuDien() {
        return ngayBieuDien;
    }

    public void setNgayBieuDien(String ngayBieuDien) {
        this.ngayBieuDien = ngayBieuDien;
    }

    public String getDiaDiem() {
        return diaDiem;
    }

    public void setDiaDiem(String diaDiem) {
        this.diaDiem = diaDiem;
    }

    public String getMaBaiHat() {
        return maBaiHat;
    }

    public void setMaBaiHat(String maBaiHat) {
        this.maBaiHat = maBaiHat;
    }
}
