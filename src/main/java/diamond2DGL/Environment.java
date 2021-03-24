package diamond2DGL;

public abstract class Environment {

    /** Abstract class Environment
     * An environment of the game. Examples would be the main menu, the game itself, level editor...
     *
     * It has its own renderer, as it might be different from the renderer that other environment might have, though
     * could be the same.
     *
     * The Game class updates and renders using this class methods.
     */
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