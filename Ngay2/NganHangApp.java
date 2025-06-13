import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

// Enum cho loai tai khoan
enum LoaiTaiKhoan {
    TIETKIEM, THANHTOAN
}

// Enum cho loai giao dich
enum LoaiGiaoDich {
    NAPTIEN, RUTTIEN
}

// Lop Nguoi
class Nguoi {
    private String ma;
    private String hoTen;
    private String email;
    private String soDienThoai;

    public Nguoi(String hoTen, String email, String soDienThoai) {
        this.ma = UUID.randomUUID().toString();
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    // Getter va Setter
    public String layMa() { return ma; }
    public String layHoTen() { return hoTen; }
    public void datHoTen(String hoTen) { this.hoTen = hoTen; }
    public String layEmail() { return email; }
    public void datEmail(String email) { this.email = email; }
    public String laySoDienThoai() { return soDienThoai; }
    public void datSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
}

// Giao dien InDuLieu
interface InDuLieu {
    void inTomTat();
}

// Lop tru tuong TaiKhoanNganHang
abstract class TaiKhoanNganHang implements InDuLieu {
    protected String soTaiKhoan;
    protected Nguoi chuTaiKhoan;
    protected double soDu;
    protected LocalDate ngayTao;
    protected LoaiTaiKhoan loaiTaiKhoan;
    protected ArrayList<GiaoDich> danhSachGiaoDich;

    // Lop ben trong GiaoDich
    protected class GiaoDich {
        private String ma;
        private LoaiGiaoDich loai;
        private double soTien;
        private LocalDateTime thoiGian;

        public GiaoDich(LoaiGiaoDich loai, double soTien) {
            this.ma = UUID.randomUUID().toString();
            this.loai = loai;
            this.soTien = soTien;
            this.thoiGian = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return "GiaoDich{" +
                    "ma='" + ma + '\'' +
                    ", loai=" + loai +
                    ", soTien=" + soTien +
                    ", thoiGian=" + thoiGian +
                    '}';
        }
    }

    public TaiKhoanNganHang(Nguoi chuTaiKhoan, double soDuBanDau, LoaiTaiKhoan loaiTaiKhoan) {
        this.soTaiKhoan = UUID.randomUUID().toString();
        this.chuTaiKhoan = chuTaiKhoan;
        this.soDu = soDuBanDau;
        this.ngayTao = LocalDate.now();
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.danhSachGiaoDich = new ArrayList<>();
        if (soDuBanDau > 0) {
            danhSachGiaoDich.add(new GiaoDich(LoaiGiaoDich.NAPTIEN, soDuBanDau));
        }
    }

    public void napTien(double soTien) {
        if (soTien > 0) {
            soDu += soTien;
            danhSachGiaoDich.add(new GiaoDich(LoaiGiaoDich.NAPTIEN, soTien));
            System.out.println("Nap " + soTien + ". So du moi: " + soDu);
        } else {
            System.out.println("So tien nap khong hop le.");
        }
    }

    public abstract void rutTien(double soTien);

    public double laySoDu() {
        return soDu;
    }

    public void inThongTinTaiKhoan() {
        System.out.println("So Tai Khoan: " + soTaiKhoan);
        System.out.println("Chu Tai Khoan: " + chuTaiKhoan.layHoTen());
        System.out.println("Loai Tai Khoan: " + loaiTaiKhoan);
        System.out.println("So Du: " + soDu);
        System.out.println("Ngay Tao: " + ngayTao);
    }

    public void inLichSuGiaoDich() {
        System.out.println("Lich Su Giao Dich cho Tai Khoan: " + soTaiKhoan);
        for (GiaoDich gd : danhSachGiaoDich) {
            System.out.println(gd);
        }
    }
}

// Lop TaiKhoanTietKiem
class TaiKhoanTietKiem extends TaiKhoanNganHang {
    private double laiSuat;

    public TaiKhoanTietKiem(Nguoi chuTaiKhoan, double soDuBanDau, double laiSuat) {
        super(chuTaiKhoan, soDuBanDau, LoaiTaiKhoan.TIETKIEM);
        this.laiSuat = laiSuat;
    }

    @Override
    public void rutTien(double soTien) {
        if (soTien > 0 && soDu - soTien >= 1000) { // Yeu cau so du toi thieu
            soDu -= soTien;
            danhSachGiaoDich.add(new GiaoDich(LoaiGiaoDich.RUTTIEN, soTien));
            System.out.println("Rut " + soTien + ". So du moi: " + soDu);
        } else {
            System.out.println("So tien rut khong hop le hoac vi pham so du toi thieu.");
        }
    }

    @Override
    public void inTomTat() {
        inThongTinTaiKhoan();
        System.out.println("Lai Suat: " + laiSuat + "%");
        inLichSuGiaoDich();
    }
}

// Lop TaiKhoanThanhToan
class TaiKhoanThanhToan extends TaiKhoanNganHang {
    private double hanMucThauChi;

    public TaiKhoanThanhToan(Nguoi chuTaiKhoan, double soDuBanDau, double hanMucThauChi) {
        super(chuTaiKhoan, soDuBanDau, LoaiTaiKhoan.THANHTOAN);
        this.hanMucThauChi = hanMucThauChi;
    }

    @Override
    public void rutTien(double soTien) {
        if (soTien > 0 && soDu - soTien >= -hanMucThauChi) {
            soDu -= soTien;
            danhSachGiaoDich.add(new GiaoDich(LoaiGiaoDich.RUTTIEN, soTien));
            System.out.println("Rut " + soTien + ". So du moi: " + soDu);
        } else {
            System.out.println("So tien rut khong hop le hoac vuot han muc thau chi.");
        }
    }

    @Override
    public void inTomTat() {
        inThongTinTaiKhoan();
        System.out.println("Han Muc Thau Chi: " + hanMucThauChi);
        inLichSuGiaoDich();
    }
}

// Lop chinh NganHangApp
public class NganHangApp {
    private static ArrayList<TaiKhoanNganHang> danhSachTaiKhoan = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== He Thong Quan Ly Ngan Hang ===");
            System.out.println("1. Tao tai khoan");
            System.out.println("2. Nap tien");
            System.out.println("3. Rut tien");
            System.out.println("4. Xem so du");
            System.out.println("5. Xem thong tin tai khoan");
            System.out.println("6. Xem lich su giao dich");
            System.out.println("7. Thoat");
            System.out.print("Chon chuc nang: ");
            int luaChon = scanner.nextInt();
            scanner.nextLine(); // Xoa bo dem

            switch (luaChon) {
                case 1:
                    taoTaiKhoan();
                    break;
                case 2:
                    napTien();
                    break;
                case 3:
                    rutTien();
                    break;
                case 4:
                    xemSoDu();
                    break;
                case 5:
                    xemThongTinTaiKhoan();
                    break;
                case 6:
                    xemLichSuGiaoDich();
                    break;
                case 7:
                    System.out.println("Tam biet!");
                    return;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        }
    }

    private static void taoTaiKhoan() {
        System.out.print("Nhap ho ten: ");
        String hoTen = scanner.nextLine();
        System.out.print("Nhap email: ");
        String email = scanner.nextLine();
        System.out.print("Nhap so dien thoai: ");
        String soDienThoai = scanner.nextLine();
        Nguoi nguoi = new Nguoi(hoTen, email, soDienThoai);

        System.out.print("Chon loai tai khoan (1: Tiet kiem, 2: Thanh toan): ");
        int loai = scanner.nextInt();
        System.out.print("Nhap so du ban dau: ");
        double soDu = scanner.nextDouble();

        TaiKhoanNganHang taiKhoan;
        if (loai == 1) {
            System.out.print("Nhap lai suat (%): ");
            double laiSuat = scanner.nextDouble();
            taiKhoan = new TaiKhoanTietKiem(nguoi, soDu, laiSuat);
            danhSachTaiKhoan.add(taiKhoan);
        } else if (loai == 2) {
            System.out.print("Nhap han muc thau chi: ");
            double hanMuc = scanner.nextDouble();
            taiKhoan = new TaiKhoanThanhToan(nguoi, soDu, hanMuc);
            danhSachTaiKhoan.add(taiKhoan);
        } else {
            System.out.println("Loai tai khoan khong hop le!");
            return;
        }
        System.out.println("Tao tai khoan thanh cong!");
        System.out.println("So tai khoan cua ban la: " + taiKhoan.soTaiKhoan);
    }

    private static void napTien() {
        System.out.print("Nhap so tai khoan: ");
        String soTaiKhoan = scanner.nextLine();
        TaiKhoanNganHang taiKhoan = timTaiKhoan(soTaiKhoan);
        if (taiKhoan != null) {
            System.out.print("Nhap so tien nap: ");
            double soTien = scanner.nextDouble();
            taiKhoan.napTien(soTien);
        } else {
            System.out.println("Tai khoan khong ton tai!");
        }
    }

    private static void rutTien() {
        System.out.print("Nhap so tai khoan: ");
        String soTaiKhoan = scanner.nextLine();
        TaiKhoanNganHang taiKhoan = timTaiKhoan(soTaiKhoan);
        if (taiKhoan != null) {
            System.out.print("Nhap so tien rut: ");
            double soTien = scanner.nextDouble();
            taiKhoan.rutTien(soTien);
        } else {
            System.out.println("Tai khoan khong ton tai!");
        }
    }

    private static void xemSoDu() {
        System.out.print("Nhap so tai khoan: ");
        String soTaiKhoan = scanner.nextLine();
        TaiKhoanNganHang taiKhoan = timTaiKhoan(soTaiKhoan);
        if (taiKhoan != null) {
            System.out.println("So du: " + taiKhoan.laySoDu());
        } else {
            System.out.println("Tai khoan khong ton tai!");
        }
    }

    private static void xemThongTinTaiKhoan() {
        System.out.print("Nhap so tai khoan: ");
        String soTaiKhoan = scanner.nextLine();
        TaiKhoanNganHang taiKhoan = timTaiKhoan(soTaiKhoan);
        if (taiKhoan != null) {
            taiKhoan.inTomTat();
        } else {
            System.out.println("Tai khoan khong ton tai!");
        }
    }

    private static void xemLichSuGiaoDich() {
        System.out.print("Nhap so tai khoan: ");
        String soTaiKhoan = scanner.nextLine();
        TaiKhoanNganHang taiKhoan = timTaiKhoan(soTaiKhoan);
        if (taiKhoan != null) {
            taiKhoan.inLichSuGiaoDich();
        } else {
            System.out.println("Tai khoan khong ton tai!");
        }
    }

    private static TaiKhoanNganHang timTaiKhoan(String soTaiKhoan) {
        for (TaiKhoanNganHang tk : danhSachTaiKhoan) {
            if (tk.soTaiKhoan.equals(soTaiKhoan)) {
                return tk;
            }
        }
        return null;
    }
}