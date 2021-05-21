package com.example.quanlyamnhac.database;

public class BaiHatDatabase {
    private String maBaiHat;
    private String tenBaiHat;
    private String namSangTac;
    private String maNhacSi;

    public BaiHatDatabase() {
    }

    public String getMaBaiHat() {
        return maBaiHat;
    }

    public void setMaBaiHat(String maBaiHat) {
        this.maBaiHat = maBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getNamSangTac() {
        return namSangTac;
    }

    public void setNamSangTac(String namSangTac) {
        this.namSangTac = namSangTac;
    }

    public String getMaNhacSi() {
        return maNhacSi;
    }

    public void setMaNhacSi(String maNhacSi) {
        this.maNhacSi = maNhacSi;
    }

    public BaiHatDatabase(String maBaiHat, String tenBaiHat, String namSangTac, String maNhacSi) {
        this.maBaiHat = maBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.namSangTac = namSangTac;
        this.maNhacSi = maNhacSi;
    }
}
