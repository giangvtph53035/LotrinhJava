package com.sync;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sync.model.DonHang;
import com.sync.model.MatHangDonHang;
import com.sync.service.DichVuKho;
import com.sync.service.DichVuVanChuyen;
import com.sync.service.XuLyDonHang;

public class UngDung {
    public static void main(String[] args) throws Exception {
        DonHang donHang = new DonHang(
            "001",
            "Giang",
            Arrays.asList(new MatHangDonHang("P001", 4, 10.0)),
            21.0,
            LocalDateTime.now()
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(new File("donhang.json"), donHang);

        String json = mapper.writeValueAsString(donHang);
        XuLyDonHang xuLy = new XuLyDonHang(new DichVuKho(), new DichVuVanChuyen());
        xuLy.xuly(json);
    }
}