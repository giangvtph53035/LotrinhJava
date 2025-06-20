import java.time.Instant;

public class GiaCoPhieu implements DiemDuLieu {
    private final Instant thoiGian;
    private final double giaTri;

    public GiaCoPhieu(Instant thoiGian, double giaTri) {
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
}