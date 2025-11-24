import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;

public class Bola extends Obstacle implements Animatable {

    private double sudut = 0;
    private BufferedImage bolaImage;
    
    // Atur ukuran bola (sesuaikan dengan ukuran asli gambar atau keinginan)
    private static final int UKURAN_LEBAR_BOLA = 45;
    private static final int UKURAN_TINGGI_BOLA = 45;

    public Bola(double x, double groundY, double speedX) {
        // Sesuaikan posisi Y agar pas di tanah (groundY - tinggi)
        super(x, groundY - UKURAN_TINGGI_BOLA, UKURAN_LEBAR_BOLA, UKURAN_TINGGI_BOLA, speedX);
        
        // Memuat gambar bola.png
        this.bolaImage = ImageManager.loadImage("src/assets/bola.png");
    }

    @Override
    public void draw(Graphics2D g2) {
        // Jika gambar ditemukan, gambar dengan rotasi
        if (bolaImage != null) {
            // Simpan kondisi grafis sebelum rotasi
            AffineTransform oldTransform = g2.getTransform();

            // Hitung titik tengah objek untuk poros putaran
            double centerX = x + lebar / 2.0;
            double centerY = y + tinggi / 2.0;

            // Lakukan rotasi sesuai variabel 'sudut' pada poros tengah
            g2.rotate(sudut, centerX, centerY);

            // Gambar bola
            g2.drawImage(bolaImage, (int) x, (int) y, lebar, tinggi, null);

            // Kembalikan kondisi grafis ke semula (agar objek lain tidak ikut miring)
            g2.setTransform(oldTransform);
        } 
        // Fallback: Jika gambar gagal dimuat, gambar bentuk manual (kode lama)
        else {
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
    }

    @Override
    public void updateAnimasi(long deltaNanos) {
        // Mengatur kecepatan putaran. 
        // Tanda minus (-) agar berputar ke kiri (melawan arah jarum jam) seolah menggelinding ke kiri
        sudut -= 5 * (deltaNanos / 1_000_000_000.0); 
    }
}