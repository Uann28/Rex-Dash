import java.awt.image.BufferedImage;

public class AnimatedSprite {
    private final BufferedImage[] frames;
    private final long frameNanos;
    private int idx = 0;
    private long accNanos = 0;

    public AnimatedSprite(BufferedImage[] frames, long frameMillis) {
        this.frames = frames != null ? frames : new BufferedImage[0];
        this.frameNanos = Math.max(1, frameMillis) * 1_000_000L;
    }

    public BufferedImage getCurrentFrame() {
        if (frames.length == 0) return null;
        return frames[idx];
    }

    public void update(long deltaNanos) {
        if (frames.length <= 1) return;
        accNanos += deltaNanos;
        while (accNanos >= frameNanos) {
            accNanos -= frameNanos;
            idx = (idx + 1) % frames.length;
        }
    }

    public static BufferedImage[] sliceFrames(BufferedImage src) {
        if (src == null) return new BufferedImage[0];
        int h = src.getHeight();
        if (h <= 0) return new BufferedImage[0];
        int frameCount = Math.max(1, src.getWidth() / h);
        int frameW = src.getWidth() / frameCount;
        if (frameW <= 0) return new BufferedImage[0];
        BufferedImage[] out = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            out[i] = src.getSubimage(i * frameW, 0, frameW, h);
        }
        return out;
    }
}
