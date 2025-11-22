import java.awt.*;
import javax.swing.*;

class LoginPanel extends JPanel {

    private final Main app;
    private final JTextField kolomUser = new JTextField();
    private final JPasswordField kolomPass = new JPasswordField();

    LoginPanel(Main m) {
        this.app = m;
        setLayout(new GridBagLayout());
        setBackground(Theme.BG);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JLabel judul = new JLabel("Welcome to Dino Dash");
        judul.setFont(Theme.H1);
        judul.setForeground(Theme.PRIMARY);
        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2;
        add(judul, gc);

        gc.gridwidth = 1; gc.gridy++;
        JLabel lUser = new JLabel("Username:");
        lUser.setForeground(Theme.TEXT);
        add(lUser, gc);

        gc.gridx = 1;
        kolomUser.setColumns(18);
        add(kolomUser, gc);

        gc.gridx = 0; gc.gridy++;
        JLabel lPass = new JLabel("Password:");
        lPass.setForeground(Theme.TEXT);
        add(lPass, gc);

        gc.gridx = 1;
        add(kolomPass, gc);

        gc.gridx = 0; gc.gridy++; gc.gridwidth = 2;
        JButton btnLogin = Theme.makeButton("Login");
        add(btnLogin, gc);

        gc.gridy++;
        JButton btnReg = Theme.makeButton("Register");
        add(btnReg, gc);

        btnLogin.addActionListener(e -> prosesLogin());
        btnReg.addActionListener(e -> prosesRegister());
        kolomPass.addActionListener(e -> prosesLogin());
    }

    private void prosesLogin() {
        String u = kolomUser.getText().trim();
        String p = new String(kolomPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lengkapi username & password.");
            return;
        }

        int id = KoneksiDatabase.loginUser(u, p);
        if (id != -1) {
            JOptionPane.showMessageDialog(this, "Login berhasil!");
            app.loginSuccess(id, u);
            kolomUser.setText("");
            kolomPass.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username atau password salah.",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void prosesRegister() {
        String u = kolomUser.getText().trim();
        String p = new String(kolomPass.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Lengkapi username & password.");
            return;
        }

        boolean ok = KoneksiDatabase.registerUser(u, p);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Registrasi berhasil! Silakan login.");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Registrasi gagal. Username mungkin sudah dipakai.",
                    "Registrasi Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
