import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class GameObject implements Collidable {

    protected double x;
    protected double y;
    protected int lebar;
    protected int tinggi;
    protected boolean aktif = true;

    public GameObject(double x, double y, int lebar, int tinggi) {
        this.x = x;
        this.y = y;
        this.lebar = lebar;
        this.tinggi = tinggi;
    }

    public abstract void update(double deltaDetik);

    public abstract void draw(Graphics2D g2);

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, lebar, tinggi);
    }

    @Override
    public boolean isAktif() {
        return aktif;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
