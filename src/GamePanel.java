// Tambahan untuk Dino.java
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel implements Runnable {

    private final Main app;
    private BufferedImage backgroundImage;
    private Dino dino;
    private final List<GameObject> listObjek = new ArrayList<>();
    private final List<Obstacle>   listRintangan = new ArrayList<>();
    private final List<PowerUp>    listPower = new ArrayList<>();

    private GameState state = GameState.MENU;

    private Thread gameThread;
    private volatile boolean jalan = false;

    private long lastNanos;
    private long skor = 0;
    private int highScore = 0;
    private double countdown = 0;


    private double baseSpeed = -350;
    private double speedMultiplier = 1.0;

    private double timeAfterObstacles = 0;
    private double timeAfterPotion = 0;
    private double nextPowerUpDelay = 12.0;
    private final Random rand = new Random();

    private static final int GROUND_Y = 350;

    GamePanel(Main app) {
        this.app = app;
        setBackground(Theme.BG);
        setFocusable(true);
        setDoubleBuffered(true);

        this.backgroundImage = ImageManager.loadImage("langit.png");
        dino = new Dino(120, GROUND_Y - 60);
        listObjek.add(dino);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();

                if (state == GameState.PLAYING || state == GameState.READY) {
                    if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
                        dino.lompat();
                    } else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                        dino.setCrouch(true);
                    }
                }

                if (state == GameState.GAME_OVER) {
                    if (code == KeyEvent.VK_ENTER) {
                        mulaiBaru();
                    } else if (code == KeyEvent.VK_ESCAPE) {
                        app.showPanel("menu");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) {
                    dino.stopLompatan();
                } else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
                    dino.setCrouch(false);
                }
            }
        });
    }

    public void onPanelShown() {
        requestFocusInWindow();
        if (gameThread == null || !jalan) {
            mulaiBaru();
        }
    }

    private void mulaiBaru() {
        listRintangan.clear();
        listPower.clear();
        listObjek.clear();

        dino = new Dino(140, GROUND_Y - 60); 
        listObjek.add(dino);

        skor = 0;
        baseSpeed = -350;
        speedMultiplier = 1.0;
        timeAfterObstacles = 0;
        timeAfterPotion = 0;
        nextPowerUpDelay = 10.0 + rand.nextDouble() * 8.0;

        countdown = 3.0;              
        state = GameState.READY;     

        if (gameThread == null || !jalan) {
            jalan = true;
            gameThread = new Thread(this, "game-loop");
            gameThread.start();
        } else {
            jalan = true;
        }
    }

    @Override
    public void run() {
        lastNanos = System.nanoTime();
        final double targetFps = 60.0;
        final double nanosPerFrame = 1_000_000_000.0 / targetFps;

        while (jalan) {
            long now = System.nanoTime();
            double deltaDetik = (now - lastNanos) / 1_000_000_000.0;
            long deltaNanos = now - lastNanos;
            lastNanos = now;

            updateGame(deltaDetik, deltaNanos);
            repaint();

            long sleepNanos = (long) (nanosPerFrame - (System.nanoTime() - now));
            if (sleepNanos > 0) {
                try {
                    Thread.sleep(sleepNanos / 1_000_000L,
                                (int) (sleepNanos % 1_000_000L));
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void gameOver() {
        if (state == GameState.GAME_OVER) return;
        state = GameState.GAME_OVER;
        jalan = false;

        int userId = app.getCurrentUserId();
        int nilai = (int) skor;

        if (userId > 0) {
            new javax.swing.SwingWorker<Integer, Void>() {
                @Override
                protected Integer doInBackground() {
                    KoneksiDatabase.saveScore(userId, nilai);
                    return KoneksiDatabase.getHighScoreForUser(userId);
                }

                @Override
                protected void done() {
                    try {
                        highScore = get();
                    } catch (Exception e) {
                        highScore = Math.max(highScore, nilai);
                    }
                    repaint();
                }
            }.execute();
        } else {
            highScore = Math.max(highScore, nilai);
        }
    }

    private void updateGame(double deltaDetik, long deltaNanos) {
        if (state == GameState.READY) {
            countdown -= deltaDetik;
            if (countdown <= 0) {
                countdown = 0;
                state = GameState.PLAYING;
            }
            return;
        }
        if (state != GameState.PLAYING) return;

        skor += (long) (deltaDetik * 100);   
        speedMultiplier = 1.0 + skor / 2000.0;
        if (speedMultiplier > 2.7) {
            speedMultiplier = 2.7;         
        }
        double speedObstaclesPot = baseSpeed * speedMultiplier;

        timeAfterObstacles += deltaDetik;
        timeAfterPotion   += deltaDetik;

        double minGapObstacle = 1.5 / speedMultiplier;
        if (minGapObstacle < 0.70) minGapObstacle = 0.70;

        if (timeAfterObstacles > minGapObstacle) {
            buatObstacles(speedObstaclesPot);
            timeAfterObstacles = 0;
        }

        if (timeAfterPotion > nextPowerUpDelay) {
            buatPowerUp(speedObstaclesPot);
            timeAfterPotion = 0;
            nextPowerUpDelay = 10.0 + rand.nextDouble() * 7.0;
        }

        dino.update(deltaDetik);
        
        if (dino instanceof Animatable) {
            ((Animatable) dino).updateAnimasi(deltaNanos);
        }

        for (Obstacle o : listRintangan) {
            o.update(deltaDetik);
            if (o instanceof Animatable) {
                ((Animatable) o).updateAnimasi(deltaNanos);
            }
        }
        
        // Loop PowerUp untuk pembaruan posisi
        for (PowerUp p : listPower) {
            p.update(deltaDetik);
        }

        Iterator<PowerUp> itP = listPower.iterator();
        while (itP.hasNext()) {
            PowerUp p = itP.next();
            if (!p.isAktif()) {
                itP.remove();
                continue;
            }

            if (p.getBounds().intersects(dino.getBounds())) {
                if (dino.isEfekInvisAktif()) {
                    p.aktif = false;
                    itP.remove();
                    continue;
                }

                if (p instanceof BarrierPotion) {
                    dino.useBarrier();
                } else if (p instanceof InvisiblePotion) {
                    dino.aktifkanInvis(5.0);
                }
                p.aktif = false;
                itP.remove();
            }
        }

        Iterator<Obstacle> itO = listRintangan.iterator();
        while (itO.hasNext()) {
            Obstacle o = itO.next();
            if (!o.isAktif()) {
                itO.remove();
                continue;
            }
            if (dino.getBounds().intersects(o.getBounds())) {
                if (dino.isEfekInvisAktif()) {
                    o.aktif = false;
                    itO.remove();
                } else if (dino.hasBarrier()) {
                    dino.unUseBarrier();
                    o.aktif = false;
                    itO.remove();
                } else {
                    gameOver();
                }
            }
        }

    }

    private void buatObstacles(double speedX) {
        int roll = rand.nextInt(10);

        if (roll < 4) {
            listRintangan.add(new Cactus(getWidth(), GROUND_Y, speedX)); 
        } else if (roll < 7) {
            int yBird = GROUND_Y - 120 + rand.nextInt(40);
            listRintangan.add(new Bird(getWidth(), yBird, speedX * 1.1)); 
        } else if (roll < 9) {
            listRintangan.add(new Hole(getWidth(), GROUND_Y, speedX));
        } else {
            listRintangan.add(new RollingSpike(getWidth(), GROUND_Y, speedX * 1.1));
        }
    }


    private void buatPowerUp(double speedX) {
        int roll = rand.nextInt(10);
        if (roll < 5) {
            listPower.add(new BarrierPotion(getWidth(), GROUND_Y, speedX));
        } else {  
            int y = GROUND_Y - 110;
            listPower.add(new InvisiblePotion(getWidth(), y, speedX));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // background
        if (backgroundImage != null) {
            // diskalakan agar sesuai dengan lebar dan area langit
            g2.drawImage(backgroundImage, 
                         0, 0, 
                         getWidth(), GROUND_Y, 
                         null);
        } else {
            g2.setColor(Theme.BG); 
            g2.fillRect(0, 0, getWidth(), GROUND_Y); 
        }

        // tanah
        g2.setColor(new Color(80, 60, 30));
        g2.fillRect(0, GROUND_Y, getWidth(), getHeight() - GROUND_Y);

        // gambar objek
        dino.draw(g2);
        for (Obstacle o : listRintangan) o.draw(g2);
        for (PowerUp p : listPower) p.draw(g2);

        // skor di pojok kanan atas
        g2.setFont(Theme.H2);
        g2.setColor(Theme.TEXT);
        String scoreText = "Skor: " + skor;
        FontMetrics fm = g2.getFontMetrics();
        int sw = fm.stringWidth(scoreText);
        g2.drawString(scoreText, getWidth() - sw - 20, 40);

        // countdown
        if (state == GameState.READY && countdown > 0) {
            String teks;
            if (countdown <= 0.5) {
                teks = "GO!";
            } else {
                int angka = (int) Math.ceil(countdown);
                teks = String.valueOf(angka);
            }

            g2.setFont(new Font("Segoe UI", Font.BOLD, 96));
            FontMetrics fm0 = g2.getFontMetrics();
            int w = fm0.stringWidth(teks);
            int h = fm0.getAscent();

            int x = (getWidth()  - w) / 2;
            int y = (getHeight() + h) / 2;

            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawString(teks, x, y);
        }

        // overlay hasil saat game over
        if (state == GameState.GAME_OVER) {
            int cardW = getWidth() / 2;
            int cardH = 220;
            int cardX = (getWidth() - cardW) / 2;
            int cardY = getHeight() / 2 - cardH / 2;

            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 25, 25);

            g2.setColor(Theme.PRIMARY);
            g2.setFont(Theme.H1);
            String title = "Game Over";
            int tw = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, cardX + (cardW - tw) / 2, cardY + 50);

            g2.setColor(Theme.TEXT);
            g2.setFont(Theme.H2);
            String skorText = "Skor: " + skor;
            String highText = "High Score: " + highScore;

            int sw1 = g2.getFontMetrics().stringWidth(skorText);
            int sw2 = g2.getFontMetrics().stringWidth(highText);

            g2.drawString(skorText, cardX + (cardW - sw1) / 2, cardY + 100);
            g2.drawString(highText, cardX + (cardW - sw2) / 2, cardY + 135);

            // HINT: ENTER & ESC
            g2.setFont(Theme.TEXT_FONT);
            String hint1 = "ENTER - Main lagi";
            String hint2 = "ESC - Kembali ke menu";
            FontMetrics fm2 = g2.getFontMetrics();
            int h1w = fm2.stringWidth(hint1);
            int h2w = fm2.stringWidth(hint2);

            g2.drawString(hint1, cardX + (cardW - h1w) / 2, cardY + 175);
            g2.drawString(hint2, cardX + (cardW - h2w) / 2, cardY + 200);
        }

        g2.dispose();
    }
}