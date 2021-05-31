package diamond2DGL;

import diamond2DGL.environments.EditorEnvFactory;
import diamond2DGL.environments.Environment;
import diamond2DGL.environments.EnvironmentFactory;

public abstract class Game {

    // ATTRIBUTES
    protected Environment currentEnvironment;

    // CONSTRUCTORS
    public Game() {
        Container.init(this);
        this.selectEnvironment(new EditorEnvFactory());
    }

    // GETTERS & SETTERS
    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

    // METHODS
    public void start() {
        Container.get().run();
    }

    public abstract void selectEnvironment(EnvironmentFactory factory);

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