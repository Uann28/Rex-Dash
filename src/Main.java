import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;

public class Main extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelUtama = new JPanel(cardLayout);

    LoginPanel panelLogin;
    MainMenuPanel panelMenu;
    public GamePanel panelGame;
    public LeaderboardPanel panelLeaderboard;

    private int    currentUserId   = -1;
    private String currentUsername = "";

    public Main() {
        super("Rex Dash: Endless Runner Dino Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG);

        try {
            Connection c = KoneksiDatabase.getConnection();
            if (c == null) {
                JOptionPane.showMessageDialog(this,
                        "Koneksi database gagal (null).",
                        "Error DB", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal inisialisasi database:\n" + ex.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
        }

        panelLogin = new LoginPanel(this);
        panelMenu = new MainMenuPanel(this);
        panelGame = new GamePanel(this);
        panelLeaderboard = new LeaderboardPanel(this);

        panelUtama.add(panelLogin, "login");
        panelUtama.add(panelMenu, "menu");
        panelUtama.add(panelGame, "game");
        panelUtama.add(panelLeaderboard, "leaderboard");

        add(panelUtama, BorderLayout.CENTER);
        showPanel("login");
    }

    public void showPanel(String name) {
        cardLayout.show(panelUtama, name);
        if ("game".equals(name)) {
            panelGame.onPanelShown();
        } else if ("leaderboard".equals(name)) {
            panelLeaderboard.onOpen();
        }
    }

    public void loginSuccess(int userId, String username) {
        this.currentUserId = userId;
        this.currentUsername = username;
        panelMenu.setWelcome("Selamat datang, " + username);
        showPanel("menu");
    }

    public void logout() {
        this.currentUserId = -1;
        this.currentUsername = "";
        showPanel("login");
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main f = new Main();
            f.setVisible(true);
        });
    }
}
