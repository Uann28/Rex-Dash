import java.awt.Graphics2D;

public abstract class Obstacle extends GameObject {

    protected double speedX;

    public Obstacle(double x, double y, int w, int h, double speedX) {
        super(x, y, w, h);
        this.speedX = speedX;
    }

    @Override
    public void update(double deltaDetik) {
        x += speedX * deltaDetik;
        if (x + lebar < 0) {
            aktif = false;
        }
    }

    @Override
    public abstract void draw(Graphics2D g2);
}
