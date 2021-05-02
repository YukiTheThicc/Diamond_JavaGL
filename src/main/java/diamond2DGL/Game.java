package diamond2DGL;

public abstract class Game {

    private final Container container;
    private Environment currentEnvironment;

    public Game() {
        this.container = new Container(this);
        this.selectEnvironment(0);
    }

    public Environment getCurrentEnvironment() {
        return currentEnvironment;
    }

    public void setCurrentEnvironment(Environment currentEnvironment) {
        this.currentEnvironment = currentEnvironment;
    }

    public void start() {
        this.container.run();
    }

    public abstract void selectEnvironment(int envCode);

    public void update(float dT) {
        this.currentEnvironment.update(dT);
    }

    public void render() {
        this.currentEnvironment.render();
    }
}