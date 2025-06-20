import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class PhanHoiKhachHang {
    private final int ma;
    private final String ten;
    private final int diem;
    private final String binhLuan;

    public PhanHoiKhachHang(int ma, String ten, int diem, String binhLuan) {
        this.ma = ma;
        this.ten = ten;
        this.diem = diem;
        this.binhLuan = binhLuan;
    }

    public int layMa() { return ma; }
    public String layTen() { return ten; }
    public int layDiem() { return diem; }
    public Optional<String> layBinhLuan() { return Optional.ofNullable(binhLuan); }

    @Override
    public String toString() {
        return String.format("Ma: %d, Ten: %s, Diem: %d, Binh luan: %s",
                ma, ten, diem, binhLuan != null ? binhLuan : "Khong co binh luan");
    }
}

public class PhanHoiXuLy {
    private static final String TEP_NHAP = "phanhoi.csv";
    private static final String TEP_XUAT = "baocao.txt";
    private static final int SO_LUONG_PHAN_HOI = 10_000;

    // Supplier de tao du lieu mau
    private static final Supplier<PhanHoiKhachHang> nhaCungCapPhanHoi = () -> {
        Random random = new Random();
        String[] danhSachTen = {"An", "Binh", "Cuong", "Duy", "Em"};
        String[] danhSachBinhLuan = {"Dich vu tuyet voi", "Hai long", "Can cai thien", "Trai nghiem kem", ""};
        return new PhanHoiKhachHang(
                random.nextInt(10000) + 1000,
                danhSachTen[random.nextInt(danhSachTen.length)],
                random.nextInt(5) + 1,
                danhSachBinhLuan[random.nextInt(danhSachBinhLuan.length)]
        );
    };

    // Predicate de kiem tra khach hang hai long
    private static final Predicate<PhanHoiKhachHang> laHaiLong = ph -> ph.layDiem() >= 4;

    // Function de chuyen doi phan hoi thanh chuoi ghi tep
    private static final Function<PhanHoiKhachHang, String> phanHoiThanhChuoi = ph ->
            String.format("%d,%s,%d,%s",
                    ph.layMa(), ph.layTen(), ph.layDiem(),
                    ph.layBinhLuan().orElse("Khong co binh luan"));

    // Consumer de hien thi phan hoi
    private static final Consumer<PhanHoiKhachHang> hienThiPhanHoi = System.out::println;

    public static void main(String[] args) {
        try {
            // Tao tep phan hoi mau
            taoTepPhanHoiMau();

            // Doc va xu ly phan hoi
            List<PhanHoiKhachHang> danhSachPhanHoi = docTepPhanHoi();
            
            // Xu ly du lieu bang functional programming
            xuLyPhanHoi(danhSachPhanHoi);

            // Xoa tep phan hoi
            xoaTepPhanHoi();
        } catch (IOException e) {
            System.err.println("Loi xu ly tep: " + e.getMessage());
        }
    }

    private static void taoTepPhanHoiMau() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TEP_NHAP))) {
            writer.write("maKhachHang,ten,diem,binhLuan\n");
            Stream.generate(nhaCungCapPhanHoi)
                    .limit(SO_LUONG_PHAN_HOI)
                    .map(phanHoiThanhChuoi)
                    .forEach(dong -> {
                        try {
                            writer.write(dong + "\n");
                        } catch (IOException e) {
                            System.err.println("Loi ghi dong: " + e.getMessage());
                        }
                    });
        }
    }

    private static List<PhanHoiKhachHang> docTepPhanHoi() throws IOException {
        try (Stream<String> danhSachDong = Files.lines(Paths.get(TEP_NHAP))) {
            return danhSachDong.skip(1) // Bo qua header
                    .map(dong -> {
                        try {
                            String[] phan = dong.split(",", 4);
                            return new PhanHoiKhachHang(
                                    Integer.parseInt(phan[0].trim()),
                                    phan[1].trim(),
                                    Integer.parseInt(phan[2].trim()),
                                    phan[3].trim().isEmpty() ? null : phan[3].trim()
                            );
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.err.println("Loi phan tich dong: " + dong);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (NoSuchFileException e) {
            System.err.println("Khong tim thay tep dau vao, su dung du lieu mau");
            return Stream.generate(nhaCungCapPhanHoi)
                    .limit(100)
                    .collect(Collectors.toList());
        }
    }

    private static void xuLyPhanHoi(List<PhanHoiKhachHang> danhSachPhanHoi) throws IOException {
        // Tao tep bao cao (che do ghi de)
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(TEP_XUAT))) {
            // Ghi thoi gian
            writer.write("Bao cao Phan tich Phan hoi - " + LocalDateTime.now() + "\n\n");

            // 1. Loc phan hoi tieu cuc (diem < 3)
            writer.write("Phan hoi Tieu cuc (Diem < 3):\n");
            danhSachPhanHoi.stream()
                    .filter(ph -> ph.layDiem() < 3)
                    .forEach(hienThiPhanHoi.andThen(ph -> {
                        try {
                            writer.write(ph.toString() + "\n");
                        } catch (IOException e) {
                            System.err.println("Loi ghi phan hoi tieu cuc: " + e.getMessage());
                        }
                    }));

            // 2. Dem so luong phan hoi theo diem
            writer.write("\nSo luong Phan hoi theo Diem:\n");
            Map<Integer, Long> demTheoDiem = danhSachPhanHoi.stream()
                    .collect(Collectors.groupingBy(
                            PhanHoiKhachHang::layDiem,
                            Collectors.counting()
                    ));
            demTheoDiem.forEach((diem, soLuong) -> {
                try {
                    writer.write(String.format("Diem %d: %d phan hoi\n", diem, soLuong));
                } catch (IOException e) {
                    System.err.println("Loi ghi thong ke diem: " + e.getMessage());
                }
            });

            // 3. Tinh diem trung binh
            double diemTrungBinh = danhSachPhanHoi.stream()
                    .mapToDouble(PhanHoiKhachHang::layDiem)
                    .average()
                    .orElse(0.0);
            writer.write(String.format("\nDiem Trung binh: %.2f\n", diemTrungBinh));

            // 4. Phan loai phan hoi tich cuc va tieu cuc
            Map<Boolean, List<PhanHoiKhachHang>> phanLoaiPhanHoi = danhSachPhanHoi.stream()
                    .collect(Collectors.partitioningBy(laHaiLong));
            writer.write(String.format("\nKhach hang Hai long: %d\n",
                    phanLoaiPhanHoi.get(true).size()));
            writer.write(String.format("Khach hang Khong hai long: %d\n",
                    phanLoaiPhanHoi.get(false).size()));
        }
    }

    private static void xoaTepPhanHoi() throws IOException {
        try {
            Files.deleteIfExists(Paths.get(TEP_NHAP));
        } catch (SecurityException e) {
            System.err.println("Loi quyen khi xoa tep: " + e.getMessage());
        }
    }
}