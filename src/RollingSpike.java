import java.awt.Color;
import java.awt.Graphics2D;

public class RollingSpike extends Obstacle implements Animatable {

    private double sudut = 0;

    public RollingSpike(double x, double groundY, double speedX) {
        super(x, groundY - 40, 40, 40, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        int cx = (int) x + lebar / 2;
        int cy = (int) y + tinggi / 2;
        int r  = lebar / 2;

        g2.setColor(Color.GRAY);
        g2.fillOval((int) x, (int) y, lebar, tinggi);

        g2.setColor(Color.BLACK);
        for (int i = 0; i < 8; i++) {
            double angle = sudut + i * Math.PI / 4;
            int x2 = cx + (int) (Math.cos(angle) * r);
            int y2 = cy + (int) (Math.sin(angle) * r);
            g2.drawLine(cx, cy, x2, y2);
        }
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
        sudut += 4 * (deltaNanos / 1_000_000_000.0); 
    }
}
