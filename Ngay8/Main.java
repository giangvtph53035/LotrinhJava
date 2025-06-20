import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        XuLyDuLieu<TyGia> boXuLy = new XuLyDuLieu<>();
        boXuLy.themDuLieu(new TyGia(Instant.now(), 23000.0));
        boXuLy.themDuLieu(new TyGia(Instant.now().minusSeconds(3600), 23500.0));

        Instant tu = Instant.now().minusSeconds(7200);
        Instant den = Instant.now();
        List<TyGia> duLieuLoc = boXuLy.locTheoKhoangThoiGian(tu, den);
        System.out.println("Du lieu loc: " + duLieuLoc);
        System.out.println("Trung binh: " + boXuLy.tinhTrungBinh());
        System.out.println("Lon nhat: " + boXuLy.timGiaTriLonNhat());
        System.out.println("Nho nhat: " + boXuLy.timGiaTriNhoNhat());
    }
}