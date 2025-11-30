import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.text.*;

class Theme {

    // ---------- Warna dasar ----------
    public static final Color BG       = new Color(25, 25, 25);
    public static final Color PANEL    = new Color(35, 35, 35);
    public static final Color PRIMARY  = new Color(253, 208, 23);
    public static final Color PRIMARY_DARK = new Color(61, 35, 10);
    public static final Color TEXT 	 = new Color(230, 230, 230);
    public static final Color DISABLED = new Color(70, 70, 70);

    //---------- Font ----------
    public static final Font H0;
    public static final Font H1;
    public static final Font H2        = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font MONO      = new Font("Consolas", Font.PLAIN, 16);

    private static Font PIXEL_BASE_FONT;

    static {
        PIXEL_BASE_FONT = loadCustomFont("/assets/Daydream DEMO.otf");
        H0 = PIXEL_BASE_FONT.deriveFont(Font.BOLD, 28f);
        H1 = PIXEL_BASE_FONT.deriveFont(Font.BOLD, 28f);
    }

    // ---------- Style JTextPane ----------
    public static final SimpleAttributeSet STYLE_NORMAL;
    public static final SimpleAttributeSet STYLE_OK;
    public static final SimpleAttributeSet STYLE_ERR;

    static {
        STYLE_NORMAL = new SimpleAttributeSet();
        StyleConstants.setForeground(STYLE_NORMAL, TEXT);
        StyleConstants.setFontFamily(STYLE_NORMAL, "Segoe UI");
        StyleConstants.setFontSize(STYLE_NORMAL, 16);

        STYLE_OK = new SimpleAttributeSet(STYLE_NORMAL);
        StyleConstants.setForeground(STYLE_OK, new Color(60, 179, 113));

        STYLE_ERR = new SimpleAttributeSet(STYLE_NORMAL);
        StyleConstants.setForeground(STYLE_ERR, new Color(205, 92, 92));
    }

    public static JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setBackground(PRIMARY);
        b.setForeground(Color.BLACK);
        b.setFont(H2);
        b.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        return b;
    }

    private static Font loadCustomFont(String resourcePath) {
        try {
            InputStream is = Theme.class.getResourceAsStream(resourcePath);
            
            if (is == null) {
                System.err.println("File font tidak ditemukan pada path: " + resourcePath);
                throw new IOException("File resource tidak ditemukan.");
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            
            return customFont;
        } catch (IOException | FontFormatException e) {
            System.err.println("Gagal memuat font: " + resourcePath);
            e.printStackTrace();
            return new Font("Monospaced", Font.PLAIN, 12);
        }
    }

}
