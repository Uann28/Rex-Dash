import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class ImageManager {
    private static Map<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage loadImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }
        try (InputStream is = ImageManager.class.getResourceAsStream("/assets/" + path)) {
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

    public static BufferedImage scaleImage(BufferedImage src, int targetW, int targetH) {
        if (src == null) return null;
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage dst = gc.createCompatibleImage(targetW, targetH, Transparency.TRANSLUCENT);
        Graphics2D g = dst.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, targetW, targetH, null);
        g.dispose();
        return dst;
    }
}
