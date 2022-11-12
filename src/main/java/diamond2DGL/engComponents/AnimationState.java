package diamond2DGL.engComponents;

import diamond2DGL.renderer.Sprite;
import diamond2DGL.utils.AssetManager;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {

    // ATTRIBUTES
    public String title;
    public List<Frame> frames = new ArrayList<>();
    private static Sprite defaultSprite = new Sprite();
    private transient float timer = 0.0f;
    private transient int currentFrame = 0;
    private boolean loops = true;

    // CONSTRUCTORS

    // GETTERS & SETTERS
    public Sprite getCurrentSprite() {
        if (currentFrame < frames.size()) {
            return frames.get(currentFrame).sprite;
        }
        return defaultSprite;
    }

    public boolean doesLoop() {
        return loops;
    }

    public void setLoops(boolean loops) {
        this.loops = loops;
    }

    // METHODS
    public void addFrame(Sprite sprite, float time) {
        frames.add(new Frame(sprite, time));
    }

    public void update(float dt) {
        int size = frames.size();
        if (currentFrame < size) {
            timer -= dt;
            if (timer <= 0) {
                if (currentFrame != size - 1 || loops) {
                    currentFrame = (currentFrame + 1) % size;
                }
                timer = frames.get(currentFrame).time;
            }
        }
    }

    public void refreshTextures() {
        for (Frame frame : frames) {
            frame.sprite.setTexture(AssetManager.getTexture(frame.sprite.getTexture().getPath()));
        }
    }
}
