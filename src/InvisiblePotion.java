import java.awt.Color;
import java.awt.Graphics2D;

public class InvisiblePotion extends PowerUp {

    public InvisiblePotion(double x, double groundY, double speedX) {
        super(x, groundY - 30, 30, 40, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(180, 200, 255));
        g2.fillRect((int) x, (int) y, lebar, tinggi);
        g2.setColor(Color.BLACK);
        g2.drawString("I", (int) x + 10, (int) y + 25);
    }
}
