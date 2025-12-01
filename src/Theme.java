import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.*;

class Theme {

    // ---------- Warna dasar aplikasi ----------
    public static final Color BG       = new Color(25, 25, 25);
    public static final Color PANEL    = new Color(35, 35, 35);
    public static final Color PRIMARY  = new Color(253, 208, 23);
    public static final Color PRIMARY_DARK = new Color(61, 35, 10);
    public static final Color TEXT 	 = new Color(230, 230, 230);
    public static final Color DISABLED = new Color(70, 70, 70);

    // --- Warna Popup ---
    public static final Color DIALOG_BG = new Color(62,105, 133); 
    public static final Color DIALOG_ACCENT = new Color(4, 9, 15); 

    //---------- Font ----------
    public static final Font H2        = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font MONO      = new Font("Consolas", Font.PLAIN, 16);
    
    // Variabel Font Khusus
    public static final Font H0;
    public static final Font H1;
    
    private static Font PIXEL_TITLE_FONT; 
    public static Font PIXEL_FONT; 

    static {
        PIXEL_TITLE_FONT = loadCustomFont("/assets/Daydream DEMO.otf");
        PIXEL_FONT = loadCustomFont("/assets/PixelOperatorMono-Bold.ttf");

        H0 = PIXEL_TITLE_FONT.deriveFont(Font.BOLD, 28f);
        H1 = PIXEL_TITLE_FONT.deriveFont(Font.BOLD, 24f);
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
        b.setFont(PIXEL_FONT.deriveFont(16f)); 
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return b;
    }

    private static Font loadCustomFont(String resourcePath) {
        try {
            InputStream is = Theme.class.getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("Gagal memuat font (tidak ditemukan): " + resourcePath);
                return new Font("Dialog", Font.BOLD, 18); 
            }
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException | FontFormatException e) {
            System.err.println("Error load font: " + resourcePath);
            return new Font("Dialog", Font.BOLD, 18);
        }
    }

    public static void setupStandardPopupStyle() {
        try {
            Font popupMsgFont = PIXEL_FONT.deriveFont(16f);
            Font popupButtonFont = PIXEL_FONT.deriveFont(14f);

            ColorUIResource bgColor = new ColorUIResource(DIALOG_BG);
            UIManager.put("OptionPane.background", bgColor);
            UIManager.put("Panel.background", bgColor);

            ColorUIResource fgColor = new ColorUIResource(DIALOG_ACCENT);
            UIManager.put("OptionPane.messageForeground", fgColor);
            UIManager.put("OptionPane.messageFont", popupMsgFont);

            UIManager.put("Button.font", popupButtonFont);
            UIManager.put("Button.foreground", fgColor);
            UIManager.put("Button.background", bgColor);
            UIManager.put("Button.select", new ColorUIResource(DIALOG_BG.brighter()));
            UIManager.put("Button.focus", new ColorUIResource(new Color(0,0,0,0)));

            UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(DIALOG_ACCENT, 2), 
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));

            UIManager.put("Button.ui", BasicButtonUI.class.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}