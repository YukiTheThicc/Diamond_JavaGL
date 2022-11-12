package blackDiamonds;

import diamond2DGL.Game;
import diamond2DGL.Environment;
import diamond2DGL.EnvironmentFactory;

public class Game_BD extends Game {

    public Game_BD() {
        super();
    }

    @Override
    public void selectEnvironment(EnvironmentFactory factory, String name) {
        if (currentEnvironment != null) {
            currentEnvironment.destroy();
        }
        currentEnvironment = new Environment(factory, name);
        this.getCurrentEnvironment().load();
        this.getCurrentEnvironment().init();
        this.getCurrentEnvironment().start();
    }
}
