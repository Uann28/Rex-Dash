import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Hole extends Obstacle {
    
    private BufferedImage holeImage;
    private static final int UKURAN_LEBAR_HOLE = 90;
    private static final int UKURAN_TINGGI_HOLE = 50;
    private static final int Y_OFFSET_HOLE = 40;

    public Hole(double x, double groundY, double speedX) {
        super(x, groundY - UKURAN_TINGGI_HOLE, UKURAN_LEBAR_HOLE, UKURAN_TINGGI_HOLE, speedX);
        this.holeImage = ImageManager.loadImage("hole.png");
        this.y = groundY - this.tinggi + Y_OFFSET_HOLE;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (holeImage != null) {
            g2.drawImage(holeImage, (int) x, (int) y, lebar, tinggi, null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect((int) x, (int) y, lebar, tinggi);
        }
    }
}