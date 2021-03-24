package diamond2DGL;

public abstract class Environment {

    private String name;
    private Renderer renderer;

    public Environment(String name) {
        this.name = name;
    }

    public int getFPS (float dT) {
        return Math.round(1.0f / dT);
    }

    public abstract void init();
    public abstract void update(float dT);
    public abstract void render();
}