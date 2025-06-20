package com.sync.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DonHang implements Serializable {
    private String maDonHang;
    private String tenKhachHang;
    private List<MatHangDonHang> danhSachMatHang;
    private double tongGia;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ngayTao;

    @JsonIgnore
    private String ghiChuNoiBo;

    public DonHang() {}

    public DonHang(String maDonHang, String tenKhachHang, List<MatHangDonHang> danhSachMatHang, double tongGia, LocalDateTime ngayTao) {
        this.maDonHang = maDonHang;
        this.tenKhachHang = tenKhachHang;
        this.danhSachMatHang = danhSachMatHang;
        this.tongGia = tongGia;
        this.ngayTao = ngayTao;
    }

    public String getMaDonHang() { return maDonHang; }
    public void setMaDonHang(String maDonHang) { this.maDonHang = maDonHang; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public List<MatHangDonHang> getDanhSachMatHang() { return danhSachMatHang; }
    public void setDanhSachMatHang(List<MatHangDonHang> danhSachMatHang) { this.danhSachMatHang = danhSachMatHang; }

    public double getTongGia() { return tongGia; }
    public void setTongGia(double tongGia) { this.tongGia = tongGia; }

    @JsonProperty("thoi_gian_tao")
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public String getGhiChuNoiBo() { return ghiChuNoiBo; }
    public void setGhiChuNoiBo(String ghiChuNoiBo) { this.ghiChuNoiBo = ghiChuNoiBo; }
}
