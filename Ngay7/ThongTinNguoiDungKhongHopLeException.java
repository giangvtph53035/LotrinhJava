public class ThongTinNguoiDungKhongHopLeException extends Exception {
    public ThongTinNguoiDungKhongHopLeException(String thongBao) {
        super(thongBao);
    }
    public ThongTinNguoiDungKhongHopLeException(String thongBao, Throwable nguyenNhan) {
        super(thongBao, nguyenNhan);
    }
}
