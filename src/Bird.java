import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Bird extends Obstacle implements Animatable {
    private AnimatedSprite anim;
    private static final int UKURAN_LEBAR_BURUNG = 60;
    private static final int UKURAN_TINGGI_BURUNG = 60;

    public Bird(double x, double y, double speedX) {
        super(x, y, UKURAN_LEBAR_BURUNG, UKURAN_TINGGI_BURUNG, speedX);
        BufferedImage raw = ImageManager.loadImage("Bird.png");
        if (raw != null) {
            BufferedImage[] frames = AnimatedSprite.sliceFrames(raw);
            for (int i = 0; i < frames.length; i++) {
                frames[i] = ImageManager.scaleImage(frames[i], UKURAN_LEBAR_BURUNG, UKURAN_TINGGI_BURUNG);
            }
            anim = new AnimatedSprite(frames, 90);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage f = anim != null ? anim.getCurrentFrame() : null;
        if (f != null) {
            g2.drawImage(f, (int) x, (int) y, null);
        } else {
            g2.setColor(java.awt.Color.LIGHT_GRAY);
            g2.fillOval((int) x, (int) y, lebar, tinggi);
        }
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
        if (anim != null) anim.update(deltaNanos);
    }

    @Override
    public Rectangle getBounds() {
        int marginX = 15;
        int marginY = 14;

        return new Rectangle(
            (int) x + marginX,
            (int) y + marginY,
            lebar - marginX * 2,
            tinggi - marginY * 2
        );
    }

}
