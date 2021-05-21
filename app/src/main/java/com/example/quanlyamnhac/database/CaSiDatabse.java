package com.example.quanlyamnhac.database;

public class CaSiDatabse {
    private String macasi;
    private String tencasi;
    private String url;

    public CaSiDatabse() {
    }

    public CaSiDatabse(String macasi, String tencasi, String url) {
        this.macasi = macasi;
        this.tencasi = tencasi;
        this.url = url;
    }

    public String getMacasi() {
        return macasi;
    }

    public void setMacasi(String macasi) {
        this.macasi = macasi;
    }

    public String getTencasi() {
        return tencasi;
    }

    public void setTencasi(String tencasi) {
        this.tencasi = tencasi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
