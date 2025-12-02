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
    
    private AnimatedSprite trexRunning;
    private BufferedImage trexJumping;
    private BufferedImage trexCrouch;
    
    private static final long FRAME_DELAY_MILLIS = 100;
    private static final int UKURAN_LEBAR_DINO= 80;
    private static final int UKURAN_TINGGI_DINO = 90;
    private static final int CROUCH_REDUCTION = UKURAN_TINGGI_DINO / 3;

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

        BufferedImage rawSheet = ImageManager.loadImage("trex1.png");
        
        if (rawSheet != null) {
            BufferedImage[] rawFrames = AnimatedSprite.sliceFrames(rawSheet);
            
            if (rawFrames.length > 0) {
                BufferedImage[] scaledRunningFrames = new BufferedImage[rawFrames.length];
                for(int i=0; i < rawFrames.length; i++) {
                    scaledRunningFrames[i] = ImageManager.scaleImage(rawFrames[i], UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO);
                }

                this.trexRunning = new AnimatedSprite(scaledRunningFrames, FRAME_DELAY_MILLIS);
                this.trexJumping = scaledRunningFrames[0];
                this.trexCrouch = ImageManager.scaleImage(rawFrames[0], UKURAN_LEBAR_DINO, UKURAN_TINGGI_DINO - CROUCH_REDUCTION);
            } else {
                System.err.println("Dino: Sprite sheet trex.png tidak memiliki frame yang valid setelah slice.");
            }
        } else {
            System.err.println("Dino: Gagal memuat image trex.png. Dino akan digambar sebagai kotak abu-abu.");
            trexRunning = null;
            trexJumping = null;
            trexCrouch = null;
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
                this.tinggi = UKURAN_TINGGI_DINO - CROUCH_REDUCTION;
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
    public void updateAnimasi(long deltaNanos) {
        if (diTanah && !crouch && trexRunning != null) {
            trexRunning.update(deltaNanos);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage img = null;
        if (crouch) {
            img = trexCrouch;
        } else if (!diTanah) {
            img = trexJumping;
        } else {
            if (trexRunning != null) {
                img = trexRunning.getCurrentFrame();
            }
        }
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
            // Fallback: Kotak abu-abu
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
    public Rectangle getBounds() {
        int w = lebar - HITBOX_MARGIN_X * 2;
        int h = tinggi - HITBOX_MARGIN_TOP;
        if (w < 10) w = lebar;
        if (h < 10) h = tinggi;
        return new Rectangle((int) x + HITBOX_MARGIN_X, (int) y + HITBOX_MARGIN_TOP, w, h);
    }
}
