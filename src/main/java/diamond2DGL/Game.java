package diamond2DGL;

public abstract class Game {

    private Environment currentEnvironment;

    public Game() {
        Container.init(this);
        this.selectEnvironment(0);
    }

    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

    public void setCurrentEnvironment(Environment currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
    }

    public void start() {
        Container.get().run();
    }

    public abstract void selectEnvironment(int envCode);

    public void update(float dT) {
        this.currentEnvironment.update(dT);
    }

    public void render() {
        this.currentEnvironment.render();
    }
}