import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RollingSpike extends Obstacle implements Animatable {

    private double sudut = 0;
    private BufferedImage bolaImage;

    private static final int DIAMETER = 65; 
    private static final int OFFSET_Y = 15; 

    public RollingSpike(double x, double groundY, double speedX) {
        super(x, groundY - DIAMETER + OFFSET_Y, DIAMETER, DIAMETER, speedX);
        BufferedImage rawImage = ImageManager.loadImage("bola.png");

        if (rawImage != null) {
            int minSize = Math.min(rawImage.getWidth(), rawImage.getHeight());
            int xCrop = (rawImage.getWidth() - minSize) / 2;
            int yCrop = (rawImage.getHeight() - minSize) / 2;
            
            this.bolaImage = rawImage.getSubimage(xCrop, yCrop, minSize, minSize);
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (bolaImage != null) {
            AffineTransform old = g2.getTransform();

            // Titik pusat putaran
            double centerX = x + (lebar / 2.0);
            double centerY = y + (tinggi / 2.0);
            g2.rotate(sudut, centerX, centerY);

            g2.drawImage(bolaImage, (int) x, (int) y, lebar, tinggi, null);
            g2.setTransform(old);
            
        } else {
            int cx = (int) x + lebar / 2;
            int cy = (int) y + tinggi / 2;
            
            g2.setColor(new Color(160, 82, 45)); 
            g2.fillOval((int)x, (int)y, lebar, tinggi);
            
            g2.setColor(new Color(100, 50, 20));
            AffineTransform old = g2.getTransform();
            g2.rotate(sudut, cx, cy);
            g2.drawLine((int)x, cy, (int)x+lebar, cy);
            g2.drawLine(cx, (int)y, cx, (int)y+tinggi);
            g2.setTransform(old);
        }
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
        sudut -= 6 * (deltaNanos / 1_000_000_000.0); 
    }
}