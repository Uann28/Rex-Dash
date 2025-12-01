import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class LeaderboardPanel extends JPanel {

    private final Main app;
    private Image backgroundImage;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Username", "Skor", "Waktu"}, 0
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public LeaderboardPanel(Main m) {
        this.app = m;
        setLayout(new BorderLayout());

        loadBackgroundImage("/assets/bg.jpeg");

        JPanel panelJudul = new JPanel();
        panelJudul.setLayout(new BoxLayout(panelJudul, BoxLayout.Y_AXIS));
        panelJudul.setOpaque(false);

        JLabel lblJudul = new JLabel("Top 10 Leaderboard", SwingConstants.CENTER);
        lblJudul.setFont(Theme.H1);
        lblJudul.setForeground(Theme.PRIMARY);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelJudul.add(Box.createVerticalStrut(15));
        panelJudul.add(lblJudul);
        panelJudul.add(Box.createVerticalStrut(15));
        add(panelJudul, BorderLayout.NORTH);

        JTable tabel = new JTable(model);
        tabel.setFillsViewportHeight(true);
        tabel.setBackground(Theme.PANEL);
        tabel.setForeground(Theme.TEXT);
        
        // --- UBAH FONT TABEL JADI PIXEL ---
        tabel.setFont(Theme.PIXEL_FONT.deriveFont(14f)); 
        tabel.setRowHeight(28); // Tinggi baris dibesarkan sedikit
        // ----------------------------------
        
        tabel.setGridColor(new Color(70, 70, 70));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            tabel.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JTableHeader header = tabel.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.BLACK);
        
        // --- UBAH FONT HEADER JADI PIXEL ---
        header.setFont(Theme.PIXEL_FONT.deriveFont(14f));
        // -----------------------------------
        
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY));
        sp.getViewport().setBackground(Theme.PANEL);
        add(sp, BorderLayout.CENTER);

        JButton btnKembali = Theme.makeButton("« Kembali ke Menu");
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bawah.setOpaque(false);
        bawah.add(btnKembali);
        add(bawah, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> app.showPanel("menu"));
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
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        } else {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void onOpen() {
        model.setRowCount(0);
        java.util.List<Object[]> data = KoneksiDatabase.getLeaderboardTop10();
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}