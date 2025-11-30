import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sound.sampled.*;

public class AudioPlayer {

    private static final Map<String, Clip> clipCache = new ConcurrentHashMap<>();

    public static void preload(String filename) {
        new Thread(() -> {
            try {
                loadClip(filename);
            } catch (Exception ignored) {}
        }, "audio-preload-" + filename).start();
    }

    private static Clip loadClip(String filename) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (clipCache.containsKey(filename)) return clipCache.get(filename);
        URL url = AudioPlayer.class.getResource("/assets/Audio/" + filename);
        if (url == null) {
            System.err.println("File audio tidak ditemukan: /assets/Audio/" + filename);
            return null;
        }
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clipCache.put(filename, clip);
        return clip;
    }

    public static void play(String filename) {
        try {
            Clip clip = clipCache.get(filename);
            if (clip != null) {
                new Thread(() -> {
                    synchronized (clip) {
                        try {
                            clip.stop();
                            clip.setFramePosition(0);
                            clip.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                return;
            }

            new Thread(() -> {
                try {
                    Clip c = loadClip(filename);
                    if (c != null) {
                        synchronized (c) {
                            c.stop();
                            c.setFramePosition(0);
                            c.start();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Gagal memutar audio: " + e.getMessage());
                }
            }, "audio-play-" + filename).start();

        } catch (Exception e) {
            System.err.println("Gagal memutar audio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cleanup() {
        for (Clip c : clipCache.values()) {
            try {
                c.stop();
                c.close();
            } catch (Exception ignored) {}
        }
        clipCache.clear();
    }
}
