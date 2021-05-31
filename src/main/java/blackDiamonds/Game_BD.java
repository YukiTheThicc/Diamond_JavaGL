package blackDiamonds;

import diamond2DGL.Game;
import diamond2DGL.environments.Environment;
import diamond2DGL.environments.EnvironmentFactory;

public class Game_BD extends Game {

    public Game_BD() {
        super();
    }

    @Override
    public void selectEnvironment(EnvironmentFactory factory) {
        if (currentEnvironment != null) {
            currentEnvironment.destroy();
        }
        currentEnvironment = new Environment(factory);
        this.getCurrentEnvironment().load();
        this.getCurrentEnvironment().init();
        this.getCurrentEnvironment().start();
    }
}
