import java.time.Instant;

public class TyGia implements DiemDuLieu {
    private final Instant thoiGian;
    private final double giaTri;

    public TyGia(Instant thoiGian, double giaTri) {
        this.thoiGian = thoiGian;
        this.giaTri = giaTri;
    }

    @Override
    public Instant layThoiGian() {
        return thoiGian;
    }

    @Override
    public double layGiaTri() {
        return giaTri;
    }

    @Override
    public String toString() {
        return "TyGia{thoiGian=" + thoiGian + ", giaTri=" + giaTri + "}";
    }
}