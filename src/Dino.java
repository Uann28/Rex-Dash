import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Dino extends GameObject implements Animatable {

    private static final double GRAVITY      = 2200; 
    private static final double JUMP_SPEED   = -900; 
    private static final int    GROUND_Y     = 350;  
    private static final double MAX_FALL_SPEED = 2000;

    private double velY = 0;
    private boolean diTanah = true;

    private boolean hasBarrier = false;
    private boolean efekInvisAktif = false;
    private double sisaInvisDetik = 0;

    private boolean crouch = false;
    private boolean fastFall = false;

    public Dino(double x, double y) {
        super(x, y, 60, 60); 
    }

    @Override
    public void update(double deltaDetik) {
        velY += GRAVITY * deltaDetik;

        if (fastFall && !diTanah) {
            velY += GRAVITY * 2.0 * deltaDetik;
        }

        if (velY >= MAX_FALL_SPEED) {
            velY = MAX_FALL_SPEED;
        }

        y += velY * deltaDetik;

        if (y >= GROUND_Y - tinggi) {
            y = GROUND_Y - tinggi;
            velY = 0;
            diTanah = true;
            fastFall = false;
        } else {
            diTanah = false;
        }

        if (diTanah) {
            if (crouch) {
                tinggi = 40;
            } else {
                tinggi = 60;
            }
            y = GROUND_Y - tinggi;
        }

        if (efekInvisAktif) {
            sisaInvisDetik -= deltaDetik;
            if (sisaInvisDetik <= 0) {
                efekInvisAktif = false;
                sisaInvisDetik = 0;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        Color c = efekInvisAktif ? new Color(150, 150, 255, 200) : new Color(100, 200, 120);
        g2.setColor(c);
        g2.fillRect((int) x, (int) y, lebar, tinggi);

        if (hasBarrier) {
            g2.setColor(new Color(255, 255, 0, 200));
            int pad = 4;
            g2.drawRect((int) x - pad, (int) y - pad, lebar + pad * 2, tinggi + pad * 2);
        }
    }

    public void lompat() {
        if (diTanah && !crouch) {
            velY = JUMP_SPEED;
        }
    }

    public void stopLompatan() {
        if (velY < 0) {
            velY *= 0.55;
        }
    }

    public void setCrouch(boolean c) {
        this.crouch = c;

        if (diTanah) {
            fastFall = false;
            if (crouch) {
                tinggi = 40;
            } else {
                tinggi = 60;
            }
            y = GROUND_Y - tinggi;
        } else {
            fastFall = c;
            if (c) {
                if (velY < 1200) {
                    velY = 1200;
                }
            }
        }
    }

    public boolean isEfekInvisAktif() {
        return efekInvisAktif;
    }

    public void useBarrier() {
        hasBarrier = true;
    }

    public boolean hasBarrier() {
        return hasBarrier;
    }

    public void unUseBarrier() {
        hasBarrier = false;
    }

    public void aktifkanInvis(double durasiDetik) {
        efekInvisAktif = true;
        sisaInvisDetik = durasiDetik;
        hasBarrier = false;
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
    }

    @Override
    public Rectangle getBounds() {
        int margin = 6;
        return new Rectangle(
                (int) x + margin,
                (int) y + margin,
                lebar - margin * 2,
                tinggi - margin * 2
        );
    }
}
