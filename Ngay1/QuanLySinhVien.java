import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

// Lop dai dien cho mot sinh vien
class SinhVien {
    private int id;
    private String ten;
    private int tuoi;
    private double diem;

    // Constructor
    public SinhVien(int id, String ten, int tuoi, double diem) {
        this.id = id;
        this.ten = ten;
        this.tuoi = tuoi;
        this.diem = diem;
    }

    // Getters
    public int getId() { return id; }
    public String getTen() { return ten; }
    public int getTuoi() { return tuoi; }
    public double getDiem() { return diem; }

    @Override
    public String toString() {
        return "ID: " + id + ", Ten: " + ten + ", Tuoi: " + tuoi + ", Diem: " + diem;
    }
}

public class QuanLySinhVien {
    private static ArrayList<SinhVien> danhSachSinhVien = new ArrayList<>();
    private static int idTiepTheo = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int luaChon;
        do {
            hienThiMenu();
            System.out.print("Nhap lua chon cua ban: ");
            luaChon = scanner.nextInt();
            scanner.nextLine(); // Xoa bo dem

            switch (luaChon) {
                case 1:
                    themSinhVien();
                    break;
                case 2:
                    hienThiDanhSachSinhVien();
                    break;
                case 3:
                    timSinhVienTheoTen();
                    break;
                case 4:
                    timSinhVienDiemCaoNhat();
                    break;
                case 5:
                    sapXepTheoDiemGiamDan();
                    break;
                case 6:
                    tinhDiemTrungBinh();
                    break;
                case 7:
                    tinhGiaiThuaTuoiSinhVienDauTien();
                    break;
                case 0:
                    System.out.println("Thoat chuong trinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long thu lai.");
            }
        } while (luaChon != 0);
    }

    // Hien thi menu
    private static void hienThiMenu() {
        System.out.println("\n=== He Thong Quan Ly Sinh Vien ===");
        System.out.println("1. Them sinh vien moi");
        System.out.println("2. Hien thi danh sach sinh vien");
        System.out.println("3. Tim sinh vien theo ten");
        System.out.println("4. Tim sinh vien co diem cao nhat");
        System.out.println("5. Sap xep sinh vien theo diem giam dan");
        System.out.println("6. Tinh diem trung binh");
        System.out.println("7. Tinh giai thua tuoi sinh vien dau tien");
        System.out.println("0. Thoat");
    }

    // Them sinh vien moi
    private static void themSinhVien() {
        System.out.print("Nhap ten: ");
        String ten = scanner.nextLine();

        System.out.print("Nhap tuoi: ");
        int tuoi = scanner.nextInt();

        System.out.print("Nhap diem: ");
        double diem = scanner.nextDouble();

        // Kiem tra dau vao
        if (tuoi <= 0) {
            System.out.println("Loi: Tuoi phai lon hon 0!");
            return;
        }
        if (diem < 0 || diem > 10) {
            System.out.println("Loi: Diem phai tu 0 den 10!");
            return;
        }

        SinhVien sinhVien = new SinhVien(idTiepTheo++, ten, tuoi, diem);
        danhSachSinhVien.add(sinhVien);
        System.out.println("Them sinh vien thanh cong!");
    }

    // Hien thi danh sach sinh vien
    private static void hienThiDanhSachSinhVien() {
        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
            return;
        }
        System.out.println("\nDanh sach sinh vien:");
        for (SinhVien sinhVien : danhSachSinhVien) {
            inSinhVien(sinhVien);
        }
    }

    // Method overloading: In mot sinh vien
    private static void inSinhVien(SinhVien sinhVien) {
        System.out.println(sinhVien);
    }

    // Method overloading: In danh sach sinh vien
    private static void inSinhVien(ArrayList<SinhVien> danhSach) {
        for (SinhVien sinhVien : danhSach) {
            System.out.println(sinhVien);
        }
    }

    // Tim sinh vien theo ten
    private static void timSinhVienTheoTen() {
        System.out.print("Nhap ten can tim: ");
        String tenTim = scanner.nextLine().toLowerCase();
        ArrayList<SinhVien> danhSachTimThay = new ArrayList<>();

        for (SinhVien sinhVien : danhSachSinhVien) {
            if (sinhVien.getTen().toLowerCase().contains(tenTim)) {
                danhSachTimThay.add(sinhVien);
            }
        }

        if (danhSachTimThay.isEmpty()) {
            System.out.println("Khong tim thay sinh vien nao co ten chua: " + tenTim);
        } else {
            System.out.println("\nSinh vien tim thay:");
            inSinhVien(danhSachTimThay);
        }
    }

    // Tim sinh vien co diem cao nhat
    private static void timSinhVienDiemCaoNhat() {
        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
            return;
        }

        SinhVien sinhVienDiemCao = Collections.max(danhSachSinhVien, Comparator.comparingDouble(SinhVien::getDiem));
        System.out.println("\nSinh vien co diem cao nhat:");
        inSinhVien(sinhVienDiemCao);
    }

    // Sap xep theo diem giam dan
    private static void sapXepTheoDiemGiamDan() {
        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
            return;
        }

        danhSachSinhVien.sort(Comparator.comparingDouble(SinhVien::getDiem).reversed());
        System.out.println("\nDanh sach sinh vien sau khi sap xep theo diem giam dan:");
        hienThiDanhSachSinhVien();
    }

    // Tinh diem trung binh
    private static void tinhDiemTrungBinh() {
        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
            return;
        }

        double tongDiem = 0;
        for (SinhVien sinhVien : danhSachSinhVien) {
            tongDiem += sinhVien.getDiem();
        }
        double trungBinh = tongDiem / danhSachSinhVien.size();
        System.out.printf("Diem trung binh: %.2f\n", trungBinh);
    }

    // Tinh giai thua de quy
    private static long giaiThua(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * giaiThua(n - 1);
    }

    // Tinh giai thua tuoi cua sinh vien dau tien
    private static void tinhGiaiThuaTuoiSinhVienDauTien() {
        if (danhSachSinhVien.isEmpty()) {
            System.out.println("Danh sach sinh vien trong!");
            return;
        }

        int tuoi = danhSachSinhVien.get(0).getTuoi();
        try {
            long ketQua = giaiThua(tuoi);
            System.out.println("Giai thua cua tuoi sinh vien dau tien (" + tuoi + ") = " + ketQua);
        } catch (StackOverflowError e) {
            System.out.println("Loi: Tuoi qua lon de tinh giai thua!");
        }
    }
}