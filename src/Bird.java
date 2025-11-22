import java.awt.Color;
import java.awt.Graphics2D;

public class Bird extends Obstacle implements Animatable {

    private boolean sayapNaik = true;
    private long akumulasiAnim = 0;

    public Bird(double x, double y, double speedX) {
        super(x, y, 50, 40, speedX);
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        int wingOffset = sayapNaik ? -8 : 8;
        g2.fillOval((int) x, (int) y, lebar, tinggi);
        g2.drawLine((int) x + 10, (int) y + tinggi / 2,
                    (int) x - 10, (int) y + tinggi / 2 + wingOffset);
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
        akumulasiAnim += deltaNanos;
        if (akumulasiAnim > 150_000_000L) {
            sayapNaik = !sayapNaik;
            akumulasiAnim = 0;
        }
    }
}
