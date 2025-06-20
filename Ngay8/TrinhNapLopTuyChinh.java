import java.net.URL;
import java.net.URLClassLoader;

public class TrinhNapLopTuyChinh extends URLClassLoader {
    public TrinhNapLopTuyChinh(URL[] urls, ClassLoader cha) {
        super(urls, cha);
    }

    @Override
    public Class<?> loadClass(String ten) throws ClassNotFoundException {
        Class<?> lopDaNap = findLoadedClass(ten);
        if (lopDaNap != null) {
            return lopDaNap;
        }

        try {
            return findClass(ten);
        } catch (ClassNotFoundException e) {
            return super.loadClass(ten);
        }
    }
}