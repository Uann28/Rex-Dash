import java.awt.*;
import javax.swing.*;

class MainMenuPanel extends JPanel {

    private final Main app;
    private final JLabel lblWelcome = new JLabel("Selamat datang");

    MainMenuPanel(Main m) {
        this.app = m;

        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        JPanel isi = new JPanel();
        isi.setBackground(Theme.BG);
        isi.setLayout(new BoxLayout(isi, BoxLayout.Y_AXIS));
        isi.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblWelcome.setFont(Theme.H1);
        lblWelcome.setForeground(Theme.PRIMARY);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnMain  = Theme.makeButton("Mulai Main");
        JButton btnBoard = Theme.makeButton("Lihat Leaderboard");
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

    public void setWelcome(String username) {
        lblWelcome.setText(" " + username);
    }
}
