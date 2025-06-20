public class HetVeException extends Exception {
    public HetVeException(String thongBao) {
        super(thongBao);
    }
    public HetVeException(String thongBao, Throwable nguyenNhan) {
        super(thongBao, nguyenNhan);
    }
}
