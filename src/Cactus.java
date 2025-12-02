import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Cactus extends Obstacle {
    private BufferedImage cactusImage;
    private static final int UKURAN_LEBAR_CACTUS = 50;
    private static final int UKURAN_TINGGI_CACTUS = 75;
    private static final int Y_OFFSET_CACTUS = 10;
    
    public Cactus(double x, double groundY, double speedX) {
        super(x, groundY - UKURAN_TINGGI_CACTUS, UKURAN_LEBAR_CACTUS, UKURAN_TINGGI_CACTUS, speedX);
        this.cactusImage = ImageManager.loadImage("kaktus.png");
        this.y = groundY - this.tinggi + Y_OFFSET_CACTUS;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (cactusImage != null) {
            g2.drawImage(cactusImage, (int) x, (int) y, lebar, tinggi, null);
        } else {
            g2.setColor(new Color(0, 180, 0));
            g2.fillRect((int) x, (int) y, lebar, tinggi);
        }
    }
}