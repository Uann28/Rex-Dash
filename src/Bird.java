import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bird extends Obstacle { 
    private BufferedImage birdImage;
    private static final int UKURAN_LEBAR_BURUNG = 60;
    private static final int UKURAN_TINGGI_BURUNG = 60;

    public Bird(double x, double y, double speedX) {
        super(x, y, UKURAN_LEBAR_BURUNG, UKURAN_TINGGI_BURUNG, speedX);
        this.birdImage = ImageManager.loadImage("burung.png");
    }

    @Override
    public void draw(Graphics2D g2) {
        if (birdImage != null) {
            g2.drawImage(birdImage, (int) x, (int) y, lebar, tinggi, null);
        } else {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval((int) x, (int) y, lebar, tinggi);
        }
    }
}