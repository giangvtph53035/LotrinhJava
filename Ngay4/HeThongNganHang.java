import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class HeThongNganHang {
    static class TaiKhoan {
        private final String maTaiKhoan;
        private double soDu;
        private final ReentrantLock khoa;

        public TaiKhoan(String maTaiKhoan, double soDuBanDau) {
            this.maTaiKhoan = maTaiKhoan;
            this.soDu = soDuBanDau;
            this.khoa = new ReentrantLock();
        }

        public String layMaTaiKhoan() {
            return maTaiKhoan;
        }

        public double laySoDu() {
            return soDu;
        }

        public ReentrantLock layKhoa() {
            return khoa;
        }

        public boolean rutTien(double soTien) {
            if (soTien <= soDu) {
                soDu -= soTien;
                return true;
            }
            return false;
        }

        public void napTien(double soTien) {
            soDu += soTien;
        }
    }

    static class NganHang {
        private final Map<String, TaiKhoan> danhSachTaiKhoan;
        private final AtomicInteger soGiaoDichThanhCong;
        private final Semaphore gioiHanGiaoDich;
        private final List<String> nhatKyGiaoDich;
        private final ExecutorService quanLyGiaoDich;
        private final ForkJoinPool quanLyBaoCao;

        public NganHang(int soGiaoDichDongThoiToiDa) {
            this.danhSachTaiKhoan = new ConcurrentHashMap<>();
            this.soGiaoDichThanhCong = new AtomicInteger(0);
            this.gioiHanGiaoDich = new Semaphore(soGiaoDichDongThoiToiDa);
            this.nhatKyGiaoDich = Collections.synchronizedList(new ArrayList<>());
            this.quanLyGiaoDich = Executors.newFixedThreadPool(20);
            this.quanLyBaoCao = new ForkJoinPool();
        }

        public void themTaiKhoan(String maTaiKhoan, double soDuBanDau) {
            danhSachTaiKhoan.put(maTaiKhoan, new TaiKhoan(maTaiKhoan, soDuBanDau));
        }

        public boolean chuyenTien(String tuTaiKhoan, String denTaiKhoan, double soTien, boolean laVip) {
            try {
                gioiHanGiaoDich.acquire();

                TaiKhoan taiKhoanNguon = danhSachTaiKhoan.get(tuTaiKhoan);
                TaiKhoan taiKhoanDich = danhSachTaiKhoan.get(denTaiKhoan);

                if (taiKhoanNguon == null || taiKhoanDich == null) {
                    return false;
                }

                // Sap xep khoa de tranh deadlock
                TaiKhoan khoaDau = taiKhoanNguon.layMaTaiKhoan().compareTo(taiKhoanDich.layMaTaiKhoan()) < 0 ? taiKhoanNguon : taiKhoanDich;
                TaiKhoan khoaSau = taiKhoanNguon.layMaTaiKhoan().compareTo(taiKhoanDich.layMaTaiKhoan()) < 0 ? taiKhoanDich : taiKhoanNguon;

                boolean thanhCong = false;
                try {
                    if (khoaDau.layKhoa().tryLock(5, TimeUnit.SECONDS)) {
                        try {
                            if (khoaSau.layKhoa().tryLock(5, TimeUnit.SECONDS)) {
                                try {
                                    // Kiem tra so du
                                    if (taiKhoanNguon.laySoDu() >= soTien) {
                                        if (taiKhoanNguon.rutTien(soTien)) {
                                            taiKhoanDich.napTien(soTien);
                                            soGiaoDichThanhCong.incrementAndGet();
                                            nhatKyGiaoDich.add(String.format("Chuyen tien: %s -> %s, So tien: %.2f", tuTaiKhoan, denTaiKhoan, soTien));
                                            thanhCong = true;

                                            // Xu ly khong dong bo (gui email)
                                            CompletableFuture.runAsync(() -> {
                                                System.out.println("Da gui email xac nhan chuyen tien tu " + tuTaiKhoan + " den " + denTaiKhoan + " so tien " + soTien);
                                            });
                                        }
                                    }
                                } finally {
                                    khoaSau.layKhoa().unlock();
                                }
                            }
                        } finally {
                            khoaDau.layKhoa().unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return thanhCong;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                gioiHanGiaoDich.release();
            }
        }

        public Map<String, Object> taoBaoCao(double nguongSoDu) {
            return quanLyBaoCao.submit(() -> {
                Map<String, Object> baoCao = new HashMap<>();
                double tongSoDu = danhSachTaiKhoan.values().parallelStream()
                        .mapToDouble(TaiKhoan::laySoDu)
                        .sum();
                List<String> taiKhoanSoDuCao = danhSachTaiKhoan.entrySet().parallelStream()
                        .filter(entry -> entry.getValue().laySoDu() > nguongSoDu)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                baoCao.put("tongSoDu", tongSoDu);
                baoCao.put("taiKhoanSoDuCao", taiKhoanSoDuCao);
                baoCao.put("soGiaoDichThanhCong", soGiaoDichThanhCong.get());
                baoCao.put("nhatKyGiaoDich", new ArrayList<>(nhatKyGiaoDich));
                return baoCao;
            }).join();
        }

        public void tatHeThong() {
            quanLyGiaoDich.shutdown();
            quanLyBaoCao.shutdown();
            try {
                if (!quanLyGiaoDich.awaitTermination(10, TimeUnit.SECONDS)) {
                    quanLyGiaoDich.shutdownNow();
                }
                if (!quanLyBaoCao.awaitTermination(10, TimeUnit.SECONDS)) {
                    quanLyBaoCao.shutdownNow();
                }
            } catch (InterruptedException e) {
                quanLyGiaoDich.shutdownNow();
                quanLyBaoCao.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    static class NguoiDung implements Runnable {
        private final NganHang nganHang;
        private final String maTaiKhoan;
        private final Random ngauNhien;
        private final boolean laVip;

        public NguoiDung(NganHang nganHang, String maTaiKhoan, boolean laVip) {
            this.nganHang = nganHang;
            this.maTaiKhoan = maTaiKhoan;
            this.ngauNhien = new Random();
            this.laVip = laVip;
        }

        @Override
        public void run() {
            List<String> danhSachMaTaiKhoan = new ArrayList<>(nganHang.danhSachTaiKhoan.keySet());
            for (int i = 0; i < 5; i++) { // Moi nguoi dung thuc hien 5 giao dich
                String maTaiKhoanDich = danhSachMaTaiKhoan.get(ngauNhien.nextInt(danhSachMaTaiKhoan.size()));
                while (maTaiKhoanDich.equals(maTaiKhoan)) {
                    maTaiKhoanDich = danhSachMaTaiKhoan.get(ngauNhien.nextInt(danhSachMaTaiKhoan.size()));
                }
                double soTien = ngauNhien.nextDouble() * 100.0;
                nganHang.chuyenTien(maTaiKhoan, maTaiKhoanDich, soTien, laVip);
                try {
                    Thread.sleep(ngauNhien.nextInt(100));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        NganHang nganHang = new NganHang(10); // Toi da 10 giao dich dong thoi
        // Khoi tao 100 tai khoan
        for (int i = 0; i < 100; i++) {
            nganHang.themTaiKhoan("TK" + i, 1000.0);
        }

        // Mo phong 200 nguoi dung, 10% la VIP
        ExecutorService quanLyNguoiDung = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 200; i++) {
            boolean laVip = i < 20; // 20 nguoi dung dau la VIP
            quanLyNguoiDung.submit(new NguoiDung(nganHang, "TK" + (i % 100), laVip));
        }

        // Giao dien dong lenh don gian
        Scanner banPhim = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Tao Bao Cao\n2. Thoat");
            System.out.print("Chon mot tuy chon: ");
            String luaChon = banPhim.nextLine();
            if (luaChon.equals("1")) {
                Map<String, Object> baoCao = nganHang.taoBaoCao(1500.0);
                System.out.println("Tong So Du: " + baoCao.get("tongSoDu"));
                System.out.println("Tai Khoan So Du Cao (>1500): " + baoCao.get("taiKhoanSoDuCao"));
                System.out.println("So Giao Dich Thanh Cong: " + baoCao.get("soGiaoDichThanhCong"));
                System.out.println("Nhat Ky Giao Dich Gan Day: " + baoCao.get("nhatKyGiaoDich"));
            } else if (luaChon.equals("2")) {
                break;
            }
        }

        nganHang.tatHeThong();
        quanLyNguoiDung.shutdown();
        try {
            if (!quanLyNguoiDung.awaitTermination(10, TimeUnit.SECONDS)) {
                quanLyNguoiDung.shutdownNow();
            }
        } catch (InterruptedException e) {
            quanLyNguoiDung.shutdownNow();
            Thread.currentThread().interrupt();
        }
        banPhim.close();
    }
}