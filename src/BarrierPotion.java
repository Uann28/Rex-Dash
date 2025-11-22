import java.awt.Color;
import java.awt.Graphics2D;

public class BarrierPotion extends PowerUp {

    public BarrierPotion(double x, double groundY, double speedX) {
        super(x, groundY - 40, 30, 40, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(255, 230, 120));
        g2.fillRect((int) x, (int) y, lebar, tinggi);
        g2.setColor(Color.BLACK);
        g2.drawString("B", (int) x + 10, (int) y + 25);
    }
}
