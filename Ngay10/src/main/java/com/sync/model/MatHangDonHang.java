package com.sync.model;

public class MatHangDonHang {
    private String maSanPham;
    private int soLuong;
    private double gia;

    public MatHangDonHang() {}

    public MatHangDonHang(String maSanPham, int soLuong, double gia) {
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }
}
