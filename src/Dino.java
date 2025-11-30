import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Dino extends GameObject implements Animatable {

    private static final double GRAVITY      = 2200;
    private static final double JUMP_SPEED   = -900;
    private static final double MAX_FALL_SPEED = 2000;

    private double velY = 0;
    private boolean diTanah = true;

    private boolean hasBarrier = false;
    private boolean efekInvisAktif = false;
    private double sisaInvisDetik = 0;

    private boolean crouch = false;
    private boolean fastFall = false;
    
    private BufferedImage trexStanding;
    private BufferedImage trexCrouch;
    private static final int UKURAN_LEBAR_DINO= 70;
    private static final int UKURAN_TINGGI_DINO = 75;
    private static final int CROUCH_REDUCTION = UKURAN_TINGGI_DINO / 3;

    // hitbox margin
    private static final int HITBOX_MARGIN_X = 18;
    private static final int HITBOX_MARGIN_TOP = 22;

    private final int groundY;
    private final int footAdjust; 

    public Dino(double x, int groundY) {
        this(x, groundY, 6);
    }

    public Dino(double x, int groundY, int footAdjust) {
        super(x, groundY - UKURAN_TINGGI_DINO, UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO); 
        this.groundY = groundY;
        this.footAdjust = footAdjust;

        BufferedImage raw = ImageManager.loadImage("trex.png");
        if (raw != null) {
            trexStanding = ImageManager.scaleImage(raw, UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO);
            trexCrouch = ImageManager.scaleImage(raw, UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO - CROUCH_REDUCTION);
        } else {
            trexStanding = trexCrouch = null;
        }

        this.y = groundY - this.tinggi + this.footAdjust;
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

        if (y >= groundY - tinggi + footAdjust) {
            y = groundY - tinggi + footAdjust;
            velY = 0;
            diTanah = true;
            fastFall = false;
        } else {
            diTanah = false;
        }

        if (diTanah) {
            if (crouch) {
                this.tinggi = UKURAN_TINGGI_DINO - (UKURAN_TINGGI_DINO / 3); 
            } else {
                this.tinggi = UKURAN_TINGGI_DINO;
            }
            y = groundY - tinggi + footAdjust;
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
        BufferedImage img = crouch ? trexCrouch : trexStanding;
        if (img != null) {
            if (efekInvisAktif) {
                if (System.currentTimeMillis() % 200 > 100) { 
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                } else {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
            g2.drawImage(img, (int) x, (int) y, null);
            if (efekInvisAktif) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        } else {
            g2.setColor(new java.awt.Color(200, 200, 200));
            g2.fillRect((int) x, (int) y, lebar, tinggi);
        }
        
        if (hasBarrier) {
            g2.setColor(new java.awt.Color(255, 255, 0, 200));
            int pad = 4;
            g2.drawRect((int) x - pad, (int) y - pad, lebar + pad * 2, tinggi + pad * 2);
        }
    }

    public void lompat() {
        if (diTanah && !crouch) {
            velY = JUMP_SPEED;
            AudioPlayer.play("jump.wav");
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
                tinggi = UKURAN_TINGGI_DINO - (UKURAN_TINGGI_DINO / 3);
                AudioPlayer.play("hide.wav");
            } else {
                tinggi = UKURAN_TINGGI_DINO;
            }
            y = groundY - tinggi + footAdjust;
        } else {
            fastFall = c;
            if (c) {
                if (velY < 1200) {
                    velY = 1200;
                }
                AudioPlayer.play("hide.wav");
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
        int w = lebar - HITBOX_MARGIN_X * 2;
        int h = tinggi - HITBOX_MARGIN_TOP;
        if (w < 10) w = lebar;
        if (h < 10) h = tinggi;
        return new Rectangle((int) x + HITBOX_MARGIN_X, (int) y + HITBOX_MARGIN_TOP, w, h);
    }
}
