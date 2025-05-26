package com.example.lab24;
import android.graphics.Bitmap;

public class Tygia {
    private String tenNgoaiTe;
    private String maNgoaiTe;
    private Bitmap anhNgoaiTe; // Nếu có ảnh, nếu không có thể bỏ qua
    private String muaTienMat;
    private String muaChuyenKhoan;
    private String banTienMat;
    private String banChuyenKhoan;
    private String tyGiaBanETax; // Vietinbank có thêm cột này

    public Tygia() {
    }

    // Getters and Setters
    public String getTenNgoaiTe() {
        return tenNgoaiTe;
    }

    public void setTenNgoaiTe(String tenNgoaiTe) {
        this.tenNgoaiTe = tenNgoaiTe;
    }

    public String getMaNgoaiTe() {
        return maNgoaiTe;
    }

    public void setMaNgoaiTe(String maNgoaiTe) {
        this.maNgoaiTe = maNgoaiTe;
    }

    public Bitmap getAnhNgoaiTe() {
        return anhNgoaiTe;
    }

    public void setAnhNgoaiTe(Bitmap anhNgoaiTe) {
        this.anhNgoaiTe = anhNgoaiTe;
    }

    public String getMuaTienMat() {
        return muaTienMat;
    }

    public void setMuaTienMat(String muaTienMat) {
        this.muaTienMat = muaTienMat;
    }

    public String getMuaChuyenKhoan() {
        return muaChuyenKhoan;
    }

    public void setMuaChuyenKhoan(String muaChuyenKhoan) {
        this.muaChuyenKhoan = muaChuyenKhoan;
    }

    public String getBanTienMat() {
        return banTienMat;
    }

    public void setBanTienMat(String banTienMat) {
        this.banTienMat = banTienMat;
    }

    public String getBanChuyenKhoan() {
        return banChuyenKhoan;
    }

    public void setBanChuyenKhoan(String banChuyenKhoan) {
        this.banChuyenKhoan = banChuyenKhoan;
    }

    public String getTyGiaBanETax() {
        return tyGiaBanETax;
    }

    public void setTyGiaBanETax(String tyGiaBanETax) {
        this.tyGiaBanETax = tyGiaBanETax;
    }
}