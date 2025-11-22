import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

public class ImageManager {
    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }
        try {
            InputStream is = ImageManager.class.getResourceAsStream("/assets/" + path);
            if (is == null) {
                System.err.println("Error: Gambar tidak ditemukan di classpath: /assets/" + path);
                return null;
            }
            BufferedImage img = ImageIO.read(is);
            if (img != null) {
                images.put(path, img);
            }
            return img;
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar: " + path + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}