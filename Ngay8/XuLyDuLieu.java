import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class XuLyDuLieu<T extends DiemDuLieu> {
    private final List<T> danhSachDuLieu = new ArrayList<>();

    public void themDuLieu(T duLieu) {
        danhSachDuLieu.add(duLieu);
    }

    public List<T> locTheoKhoangThoiGian(Instant tu, Instant den) {
        return danhSachDuLieu.stream()
                .filter(dl -> !dl.layThoiGian().isBefore(tu) && !dl.layThoiGian().isAfter(den))
                .collect(Collectors.toList());
    }

    public double tinhTrungBinh() {
        return danhSachDuLieu.stream()
                .mapToDouble(DiemDuLieu::layGiaTri)
                .average()
                .orElse(0.0);
    }

    public double timGiaTriLonNhat() {
        return danhSachDuLieu.stream()
                .mapToDouble(DiemDuLieu::layGiaTri)
                .max()
                .orElse(0.0);
    }

    public double timGiaTriNhoNhat() {
        return danhSachDuLieu.stream()
                .mapToDouble(DiemDuLieu::layGiaTri)
                .min()
                .orElse(0.0);
    }

    public List<? extends DiemDuLieu> layDanhSachDuLieu() {
        return new ArrayList<>(danhSachDuLieu);
    }
}