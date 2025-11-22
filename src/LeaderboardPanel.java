import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class LeaderboardPanel extends JPanel {

    private final Main app;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Username", "Skor", "Level", "Waktu"}, 0
    ) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public LeaderboardPanel(Main m) {
        this.app = m;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel panelJudul = new JPanel();
        panelJudul.setLayout(new BoxLayout(panelJudul, BoxLayout.Y_AXIS));
        panelJudul.setBackground(Theme.BG);

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
        tabel.setFont(Theme.TEXT_FONT);
        tabel.setRowHeight(24);
        tabel.setGridColor(new Color(70, 70, 70));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < model.getColumnCount(); i++) {
            tabel.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JTableHeader header = tabel.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.BLACK);
        header.setFont(Theme.H2);
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane sp = new JScrollPane(tabel);
        sp.setBorder(BorderFactory.createLineBorder(Theme.PRIMARY));
        sp.getViewport().setBackground(Theme.PANEL);
        add(sp, BorderLayout.CENTER);

        JButton btnKembali = Theme.makeButton("« Kembali ke Menu");
        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bawah.setBackground(Theme.BG);
        bawah.add(btnKembali);
        add(bawah, BorderLayout.SOUTH);

        btnKembali.addActionListener(e -> app.showPanel("menu"));
    }

    public void onOpen() {
        model.setRowCount(0);
        java.util.List<Object[]> data = KoneksiDatabase.getLeaderboardTop10();
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}
