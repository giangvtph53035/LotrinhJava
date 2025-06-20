import java.util.*;
import java.util.concurrent.*;

public class DemoDatVe {
    public static void main(String[] args) throws InterruptedException {
        Map<String, Integer> khoiTaoVe = Map.of(
                "VIP", 10,
                "Standard", 20,
                "EarlyBird", 5,
                "Student", 8
        );
        QuanLyVe quanLyVe = new QuanLyVe(khoiTaoVe);
        int soLuongTienTrinh = 30;
        ExecutorService boXuLy = Executors.newFixedThreadPool(10);
        CountDownLatch dongHo = new CountDownLatch(soLuongTienTrinh);
        Random ngauNhien = new Random();
        String[] cacLoaiVe = khoiTaoVe.keySet().toArray(String[]::new);

        for (int i = 0; i < soLuongTienTrinh; i++) {
            int chiSo = i;
            boXuLy.submit(() -> {
                String nguoiDung = "NguoiDung" + chiSo;
                String loaiVe = cacLoaiVe[ngauNhien.nextInt(cacLoaiVe.length)];
                try {
                    quanLyVe.datVe(nguoiDung, loaiVe);
                    System.out.println(nguoiDung + " dat ve thanh cong loai " + loaiVe);
                } catch (ThongTinNguoiDungKhongHopLeException | HetVeException loi) {
                    System.out.println(nguoiDung + " that bai: " + loi.getMessage());
                } catch (LoiCongThanhToanException loi) {
                    System.out.println(nguoiDung + " loi thanh toan: " + loi.getMessage());
                } catch (Exception loi) {
                    System.out.println(nguoiDung + " loi he thong: " + loi.getMessage());
                } finally {
                    dongHo.countDown();
                }
            });
        }
        dongHo.await();
        boXuLy.shutdown();
        // Thong ke ve con lai
        System.out.println("\nThong ke ve con lai:");
        for (var dong : quanLyVe.thongKeSoLuongVe()) {
            System.out.println(dong.getKey() + ": " + dong.getValue());
        }
        // In log dat ve
        System.out.println("\nNhat ky dat ve:");
        for (String dong : quanLyVe.getNhatKyDatVe()) {
            System.out.println(dong);
        }
    }
}
