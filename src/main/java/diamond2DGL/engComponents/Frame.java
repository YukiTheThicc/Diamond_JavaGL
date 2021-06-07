package diamond2DGL.engComponents;

public class Frame {

    // ATTRIBUTES
    public Sprite sprite;
    public float time;

    // CONSTRUCTORS
    public Frame() {

    }

    public Frame(Sprite sprite, float time) {
        this.sprite = sprite;
        this.time = time;
    }

    // GETTERS & SETTERS
    public Sprite getSprite() {
        return sprite;
    }

    public float getTime() {
        return time;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setTime(float time) {
        this.time = time;
    }

    // METHODS
}
