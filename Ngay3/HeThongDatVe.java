import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

class SuKien {
    private String maSuKien;
    private String tenSuKien;
    private String diaDiem;
    private LocalDate ngayDienRa;
    private int soGheToiDa;

    public SuKien(String tenSuKien, String diaDiem, LocalDate ngayDienRa, int soGheToiDa) {
        this.maSuKien = UUID.randomUUID().toString();
        this.tenSuKien = tenSuKien;
        this.diaDiem = diaDiem;
        this.ngayDienRa = ngayDienRa;
        this.soGheToiDa = soGheToiDa;
    }

    public String layMaSuKien() { return maSuKien; }
    public String layTenSuKien() { return tenSuKien; }
    public String layDiaDiem() { return diaDiem; }
    public LocalDate layNgayDienRa() { return ngayDienRa; }
    public int laySoGheToiDa() { return soGheToiDa; }

    @Override
    public String toString() {
        return "Ma su kien: " + maSuKien + "\nTen su kien: " + tenSuKien + 
               "\nDia diem: " + diaDiem + "\nNgay: " + ngayDienRa + 
               "\nSo ghe toi da: " + soGheToiDa;
    }
}

class DatVe {
    private String emailNguoiDung;
    private String maSuKien;
    private int soGhe;
    private LocalDateTime thoiGianDat;

    public DatVe(String emailNguoiDung, String maSuKien, int soGhe) {
        this.emailNguoiDung = emailNguoiDung;
        this.maSuKien = maSuKien;
        this.soGhe = soGhe;
        this.thoiGianDat = LocalDateTime.now();
    }

    public String layEmailNguoiDung() { return emailNguoiDung; }
    public String layMaSuKien() { return maSuKien; }
    public int laySoGhe() { return soGhe; }
    public LocalDateTime layThoiGianDat() { return thoiGianDat; }

    @Override
    public String toString() {
        return "Email: " + emailNguoiDung + ", Ma su kien: " + maSuKien + 
               ", So ghe: " + soGhe + ", Thoi gian dat: " + thoiGianDat;
    }
}

public class HeThongDatVe {
    private static ArrayList<SuKien> danhSachSuKien = new ArrayList<>();
    private static LinkedList<DatVe> danhSachDatVe = new LinkedList<>();
    private static HashMap<String, HashSet<Integer>> gheDaDat = new HashMap<>();
    private static HashMap<String, Integer> thongKeDatVe = new HashMap<>();
    private static Scanner nhap = new Scanner(System.in);
    private static DateTimeFormatter dinhDangNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== He thong dat ve su kien ===");
            System.out.println("1. Them su kien");
            System.out.println("2. Tim su kien theo ten");
            System.out.println("3. Sap xep su kien theo ten");
            System.out.println("4. Dat ve");
            System.out.println("5. Xem danh sach ve da dat");
            System.out.println("6. Thong ke luot dat ve");
            System.out.println("7. Huy ve");
            System.out.println("8. Thoat");
            System.out.print("Chon chuc nang: ");

            try {
                int luaChon = Integer.parseInt(nhap.nextLine());
                switch (luaChon) {
                    case 1: themSuKien(); break;
                    case 2: timSuKien(); break;
                    case 3: sapXepSuKien(); break;
                    case 4: datVe(); break;
                    case 5: xemDanhSachVe(); break;
                    case 6: thongKeLuotDatVe(); break;
                    case 7: huyVe(); break;
                    case 8: System.out.println("Tam biet!"); return;
                    default: System.out.println("Lua chon khong hop le");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen hop le");
            }
        }
    }

    private static void themSuKien() {
        System.out.print("Nhap ten su kien: ");
        String tenSuKien = nhap.nextLine().trim();
        if (tenSuKien.isEmpty()) {
            System.out.println("Ten su kien khong duoc de trong");
            return;
        }
        System.out.print("Nhap dia diem: ");
        String diaDiem = nhap.nextLine().trim();
        if (diaDiem.isEmpty()) {
            System.out.println("Dia diem khong duoc de trong");
            return;
        }
        System.out.print("Nhap ngay dien ra (dd/MM/yyyy): ");

        LocalDate ngayDienRa = null;
        try {
            ngayDienRa = LocalDate.parse(nhap.nextLine().trim(), dinhDangNgay);
            if (ngayDienRa.isBefore(LocalDate.now())) {
                System.out.println("Ngay dien ra phai tu hom nay tro di");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Dinh dang ngay khong hop le");
            return;
        }

        System.out.print("Nhap so ghe toi da: ");
        try {
            int soGheToiDa = Integer.parseInt(nhap.nextLine().trim());
            if (soGheToiDa <= 0) {
                System.out.println("So ghe phai lon hon 0");
                return;
            }
            SuKien suKien = new SuKien(tenSuKien, diaDiem, ngayDienRa, soGheToiDa);
            danhSachSuKien.add(suKien);
            gheDaDat.put(suKien.layMaSuKien(), new HashSet<>());
            System.out.println("Them su kien thanh cong!");
            System.out.println("Ma su kien: " + suKien.layMaSuKien());
        } catch (NumberFormatException e) {
            System.out.println("So ghe phai la so nguyen");
        }
    }

    private static void timSuKien() {
        System.out.print("Nhap ten su kien can tim: ");
        String tuKhoa = nhap.nextLine().toLowerCase();
        boolean timThay = false;

        for (SuKien sk : danhSachSuKien) {
            if (sk.layTenSuKien().toLowerCase().contains(tuKhoa)) {
                System.out.println("\n" + sk);
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay su kien nao phu hop");
        }
    }

    private static void sapXepSuKien() {
        danhSachSuKien.sort((sk1, sk2) -> sk1.layTenSuKien().compareToIgnoreCase(sk2.layTenSuKien()));
        System.out.println("Danh sach su kien sau khi sap xep:");
        for (SuKien sk : danhSachSuKien) {
            System.out.println("\n" + sk);
        }
    }

    private static void datVe() {
        System.out.print("Nhap email nguoi dung: ");
        String email = nhap.nextLine().trim();
        if (email.isEmpty()) {
            System.out.println("Email khong duoc de trong");
            return;
        }
        System.out.print("Nhap ma su kien: ");
        String maSuKien = nhap.nextLine().trim();

        SuKien suKien = null;
        for (SuKien sk : danhSachSuKien) {
            if (sk.layMaSuKien().equals(maSuKien)) {
                suKien = sk;
                break;
            }
        }

        if (suKien == null) {
            System.out.println("Ma su kien khong ton tai. Danh sach ma su kien hien co:");
            for (SuKien sk : danhSachSuKien) {
                System.out.println(" - " + sk.layTenSuKien() + " (Ma: " + sk.layMaSuKien() + ")");
            }
            return;
        }

        System.out.print("Nhap so ghe (1-" + suKien.laySoGheToiDa() + "): ");
        try {
            int soGhe = Integer.parseInt(nhap.nextLine().trim());
            if (soGhe < 1 || soGhe > suKien.laySoGheToiDa()) {
                System.out.println("So ghe khong hop le");
                return;
            }

            HashSet<Integer> gheCuaSuKien = gheDaDat.get(maSuKien);
            if (gheCuaSuKien == null) {
                System.out.println("Loi he thong: khong tim thay danh sach ghe cua su kien nay");
                return;
            }
            if (gheCuaSuKien.contains(soGhe)) {
                System.out.println("Ghe da duoc dat, vui long chon ghe khac");
                return;
            }

            gheCuaSuKien.add(soGhe);
            DatVe datVe = new DatVe(email, maSuKien, soGhe);
            danhSachDatVe.addLast(datVe);
            thongKeDatVe.put(maSuKien, thongKeDatVe.getOrDefault(maSuKien, 0) + 1);
            System.out.println("Dat ve thanh cong!");
        } catch (NumberFormatException e) {
            System.out.println("So ghe phai la so nguyen");
        }
    }

    private static void xemDanhSachVe() {
        System.out.println("Danh sach ve da dat (theo thu tu dat):");
        Iterator<DatVe> iterator = danhSachDatVe.iterator();
        if (!iterator.hasNext()) {
            System.out.println("Chua co ve nao duoc dat");
            return;
        }

        while (iterator.hasNext()) {
            System.out.println("\n" + iterator.next());
        }
    }

    private static void thongKeLuotDatVe() {
        System.out.println("Thong ke luot dat ve theo su kien:");
        if (thongKeDatVe.isEmpty()) {
            System.out.println("Chua co luot dat ve nao");
            return;
        }

        for (Map.Entry<String, Integer> entry : thongKeDatVe.entrySet()) {
            String maSuKien = entry.getKey();
            int luotDat = entry.getValue();
            String tenSuKien = "Khong tim thay";
            for (SuKien sk : danhSachSuKien) {
                if (sk.layMaSuKien().equals(maSuKien)) {
                    tenSuKien = sk.layTenSuKien();
                    break;
                }
            }
            System.out.println("Su kien: " + tenSuKien + " (Ma: " + maSuKien + ") - Luot dat: " + luotDat);
        }
    }

    private static void huyVe() {
        System.out.print("Nhap email nguoi dung: ");
        String email = nhap.nextLine().trim();
        System.out.print("Nhap ma su kien: ");
        String maSuKien = nhap.nextLine().trim();
        System.out.print("Nhap so ghe: ");
        try {
            int soGhe = Integer.parseInt(nhap.nextLine().trim());
            Iterator<DatVe> iterator = danhSachDatVe.iterator();
            boolean daHuy = false;

            while (iterator.hasNext()) {
                DatVe datVe = iterator.next();
                if (datVe.layEmailNguoiDung().equals(email) &&
                    datVe.layMaSuKien().equals(maSuKien) &&
                    datVe.laySoGhe() == soGhe) {
                    iterator.remove();
                    HashSet<Integer> gheSet = gheDaDat.get(maSuKien);
                    if (gheSet != null) gheSet.remove(soGhe);
                    if (thongKeDatVe.containsKey(maSuKien)) {
                        thongKeDatVe.put(maSuKien, thongKeDatVe.get(maSuKien) - 1);
                        if (thongKeDatVe.get(maSuKien) == 0) {
                            thongKeDatVe.remove(maSuKien);
                        }
                    }
                    daHuy = true;
                    System.out.println("Huy ve thanh cong!");
                    break;
                }
            }

            if (!daHuy) {
                System.out.println("Khong tim thay ve phu hop de huy");
            }
        } catch (NumberFormatException e) {
            System.out.println("So ghe phai la so nguyen");
        }
    }
}