import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

public class Dino extends GameObject implements Animatable {

    private static final double GRAVITY      = 2200;
    private static final double JUMP_SPEED   = -900;
    private static final int    GROUND_Y     = 350;
    private static final double MAX_FALL_SPEED = 2000;
    private static final int Y_OFFSET_DINO = 10;

    private double velY = 0;
    private boolean diTanah = true;

    private boolean hasBarrier = false;
    private boolean efekInvisAktif = false;
    private double sisaInvisDetik = 0;

    private boolean crouch = false;
    private boolean fastFall = false;
    
    private BufferedImage trexImage;
    private static final int UKURAN_LEBAR_DINO= 70;
    private static final int UKURAN_TINGGI_DINO = 75;
    
    public Dino(double x, double y) {
        super(x, y, UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO); 

        this.trexImage = ImageManager.loadImage("trex.png");
        
        // Atur posisi Y agar kaki berada di GROUND_Y sesuai tinggi yang sudah ditetapkan
        this.y = GROUND_Y - this.tinggi + Y_OFFSET_DINO;
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
            y = GROUND_Y - tinggi + Y_OFFSET_DINO;
            velY = 0;
            diTanah = true;
            fastFall = false;
        } else {
            diTanah = false;
        }

        if (diTanah) {
            if (crouch) {
                this.tinggi = UKURAN_TINGGI_DINO - (UKURAN_TINGGI_DINO/ 3); 
            } else {
                this.tinggi = UKURAN_TINGGI_DINO;
            }
            y = GROUND_Y - tinggi + Y_OFFSET_DINO;
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
        if (trexImage != null) {
            if (efekInvisAktif) {
                // Efek berkedip 50% opacity
                if (System.currentTimeMillis() % 200 > 100) { 
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                } else {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                }
            }
            g2.drawImage(trexImage, (int) x, (int) y, lebar, tinggi, null);

            if (efekInvisAktif) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
            
        }
        
        if (hasBarrier) {
            g2.setColor(new Color(255, 255, 0, 200));
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
            y = GROUND_Y - tinggi + Y_OFFSET_DINO;
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
        int marginYTop = 20;
        int marginX =15;
        int tinggiDagingTabrakan = tinggi - marginYTop;

        return new Rectangle(
                (int) x + marginX,
                (int) y + marginYTop,
                lebar - marginX * 2,
                tinggiDagingTabrakan
        );
    }
}