package com.sync.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sync.model.DonHang;

public class XuLyDonHang {
    private final DichVuKho dichVuKho;
    private final DichVuVanChuyen dichVuVanChuyen;

    public XuLyDonHang(DichVuKho dichVuKho, DichVuVanChuyen dichVuVanChuyen) {
        this.dichVuKho = dichVuKho;
        this.dichVuVanChuyen = dichVuVanChuyen;
    }

    public void xuly(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            DonHang donHang = mapper.readValue(json, DonHang.class);

            if (dichVuKho.kiemTraKhaDung(donHang)) {
                dichVuVanChuyen.vanchuyen(donHang);
            }
        } catch (Exception e) {
            System.err.println("Khong the xu ly don hang: " + e.getMessage());
        }
    }
}