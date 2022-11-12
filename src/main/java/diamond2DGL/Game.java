package diamond2DGL;

import blackDiamonds.envs.EditorEnvFactory;

public abstract class Game {

    // ATTRIBUTES
    protected Environment currentEnvironment;

    // CONSTRUCTORS
    /**
     * When the game is constructed the container context is initialized yet NOT started. It loads and creates the
     * necessary resources
     */
    public Game() {
        Container.init(this);
        this.selectEnvironment(new EditorEnvFactory(), "Editor");
    }

    // GETTERS & SETTERS
    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

    // METHODS
    public void start() {
        Container.get().run();
    }

    /**
     * Select the next environment to load creating it from its factory. Has to be implemented per game.
     *
     * @param factory: An Environment factory
     */
    public abstract void selectEnvironment(EnvironmentFactory factory, String name);

    public void editorUpdate(float dT) {
        this.currentEnvironment.editorUpdate(dT);
    }

    public void update(float dT) {
        this.currentEnvironment.update(dT);
    }

    public void render() {
        this.currentEnvironment.render();
    }
}