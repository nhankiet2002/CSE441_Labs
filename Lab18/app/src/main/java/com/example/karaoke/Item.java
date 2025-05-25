package com.example.karaoke;


public class Item {
    private String maso;
    private String tieude;
    private Integer thich; // 0 for unlike, 1 for like

    public Item() {
    }

    public Item(String maso, String tieude, Integer thich) {
        this.maso = maso;
        this.tieude = tieude;
        this.thich = thich;
    }

    public String getMaso() {
        return maso;
    }

    public void setMaso(String maso) {
        this.maso = maso;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public Integer getThich() {
        return thich;
    }

    public void setThich(Integer thich) {
        this.thich = thich;
    }
}