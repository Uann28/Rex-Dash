import java.awt.Color;
import java.awt.Graphics2D;

public class Hole extends Obstacle {

    public Hole(double x, double groundY, double speedX) {
        super(x, groundY - 10, 80, 10, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect((int) x, (int) y, lebar, tinggi);
    }
}
