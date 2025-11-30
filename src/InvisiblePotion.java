import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class InvisiblePotion extends PowerUp {
    private BufferedImage potionImage;
    private static final int UKURAN_LEBAR_POTION = 60;
    private static final int UKURAN_TINGGI_POTION = 60;

    public InvisiblePotion(double x, double groundY, double speedX) {
        super(x, groundY - 30, UKURAN_LEBAR_POTION, UKURAN_TINGGI_POTION, speedX);
        
        this.potionImage = ImageManager.loadImage("invisPotion.png");
    }

    @Override
    public void draw(Graphics2D g2) {
        if (potionImage != null) {
            g2.drawImage(potionImage, (int) x, (int) y, lebar, tinggi, null);
        } else {
            g2.setColor(new Color(180, 200, 255));
            g2.fillRect((int) x, (int) y, lebar, tinggi);
            g2.setColor(Color.BLACK);
            g2.drawString("I", (int) x + 10, (int) y + 25);
        }
    }
}