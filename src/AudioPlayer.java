import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {

    public static void play(String filename) {
        try {
            URL url = AudioPlayer.class.getResource("/assets/Audio/" + filename);
            if (url == null) {
                System.err.println("File audio tidak ditemukan: /assets/Audio/" + filename);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Gagal memutar audio: " + e.getMessage());
            e.printStackTrace();
        }
    }
}