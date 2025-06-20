public class LoiCongThanhToanException extends RuntimeException {
    public LoiCongThanhToanException(String thongBao) {
        super(thongBao);
    }
    public LoiCongThanhToanException(String thongBao, Throwable nguyenNhan) {
        super(thongBao, nguyenNhan);
    }
}
