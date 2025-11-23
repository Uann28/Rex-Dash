import java.awt.*;
import javax.swing.*;

class MainMenuPanel extends JPanel {
    private final Main app;
    private final JLabel lblWelcome = new JLabel("Welcome ");
    private Image backgroundImage;

    MainMenuPanel(Main m) {
        this.app = m;

        setLayout(new GridBagLayout());
        loadBackgroundImage("/assets/bg.jpeg");

        JPanel isi = new JPanel();
        isi.setOpaque(false);
        isi.setBackground(Theme.BG);
        isi.setLayout(new BoxLayout(isi, BoxLayout.Y_AXIS));
        isi.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblWelcome.setFont(Theme.H1);
        lblWelcome.setForeground(Theme.PRIMARY);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnMain  = Theme.makeButton("Start");
        JButton btnBoard = Theme.makeButton("Leaderboard");
        JButton btnOut   = Theme.makeButton("Logout");
        btnOut.setBackground(Theme.DISABLED);
        btnOut.setForeground(Color.LIGHT_GRAY);

        Dimension d = new Dimension(260, 38);
        for (JButton b : new JButton[]{btnMain, btnBoard, btnOut}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(d);
            b.setPreferredSize(d);
            b.setMinimumSize(d);
        }

        isi.add(lblWelcome);
        isi.add(Box.createVerticalStrut(18));
        isi.add(btnMain);
        isi.add(Box.createVerticalStrut(10));
        isi.add(btnBoard);
        isi.add(Box.createVerticalStrut(15));
        isi.add(btnOut);
        isi.add(Box.createVerticalGlue());

        add(isi);

        btnMain.addActionListener(e -> {
            app.showPanel("game");
        });
        btnBoard.addActionListener(e -> {
            app.showPanel("leaderboard");
        });
        btnOut.addActionListener(e -> app.logout());
    }

    private void loadBackgroundImage(String path) {
        try {
            backgroundImage = new ImageIcon(getClass().getResource(path)).getImage();
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar background dari path: " + path);
            e.printStackTrace();
            setBackground(Theme.BG);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void setWelcome(String username) {
        lblWelcome.setText(" " + username);
    }
}
