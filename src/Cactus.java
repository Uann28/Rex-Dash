import java.awt.Color;
import java.awt.Graphics2D;

public class Cactus extends Obstacle {

    public Cactus(double x, double groundY, double speedX) {
        super(x, groundY - 60, 40, 60, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(new Color(0, 180, 0));
        g2.fillRect((int) x, (int) y, lebar, tinggi);
    }
}
