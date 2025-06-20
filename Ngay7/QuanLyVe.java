import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class QuanLyVe {
    // Luu so luong ve con lai theo loai ve
    private final ConcurrentHashMap<String, AtomicInteger> soLuongVe = new ConcurrentHashMap<>();
    // Ghi log dat ve (theo thu tu)
    private final List<String> nhatKyDatVe = Collections.synchronizedList(new ArrayList<>());

    public QuanLyVe(Map<String, Integer> khoiTaoVe) {
        for (Map.Entry<String, Integer> dong : khoiTaoVe.entrySet()) {
            soLuongVe.put(dong.getKey(), new AtomicInteger(dong.getValue()));
        }
    }

    // Dat ve thread-safe
    public void datVe(String thongTinNguoiDung, String loaiVe)
            throws ThongTinNguoiDungKhongHopLeException, HetVeException, LoiCongThanhToanException {
        if (thongTinNguoiDung == null || thongTinNguoiDung.isBlank()) {
            throw new ThongTinNguoiDungKhongHopLeException("Thong tin nguoi dung khong hop le");
        }
        AtomicInteger soLuong = soLuongVe.get(loaiVe);
        if (soLuong == null) {
            throw new HetVeException("Loai ve khong ton tai");
        }
        boolean thanhCong = false;
        // Dat ve: giam so luong neu con ve
        while (true) {
            int hienTai = soLuong.get();
            if (hienTai <= 0) {
                throw new HetVeException("Ve da het cho loai: " + loaiVe);
            }
            if (soLuong.compareAndSet(hienTai, hienTai - 1)) {
                thanhCong = true;
                break;
            }
        }
        // Goi cong thanh toan (gia lap, co the nem exception)
        try {
            xuLyThanhToan(thongTinNguoiDung, loaiVe);
        } catch (Exception loi) {
            // Tra lai ve neu thanh toan loi
            soLuong.incrementAndGet();
            throw new LoiCongThanhToanException("Loi khi thanh toan", loi);
        }
        // Ghi log dat ve
        nhatKyDatVe.add("[" + new Date() + "] " + thongTinNguoiDung + " da dat ve loai " + loaiVe);
    }

    // Thong ke: sap xep cac loai ve theo so luong con lai (giam dan)
    public List<Map.Entry<String, Integer>> thongKeSoLuongVe() {
        List<Map.Entry<String, Integer>> ds = new ArrayList<>();
        for (Map.Entry<String, AtomicInteger> dong : soLuongVe.entrySet()) {
            ds.add(Map.entry(dong.getKey(), dong.getValue().get()));
        }
        ds.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return ds;
    }

    // Lay log dat ve
    public List<String> getNhatKyDatVe() {
        return new ArrayList<>(nhatKyDatVe);
    }

    // Mo phong xu ly thanh toan (co the nem exception)
    private void xuLyThanhToan(String thongTinNguoiDung, String loaiVe) throws Exception {
        // Gia lap loi ngau nhien
        if (Math.random() < 0.05) {
            throw new Exception("Ket noi cong thanh toan that bai");
        }
    }
}
