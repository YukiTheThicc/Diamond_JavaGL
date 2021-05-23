package blackDiamonds;

import blackDiamonds.envs.Menu;
import diamond2DGL.Game;

public class Game_BD extends Game {

    public Game_BD() {
        super();
    }

    @Override
    public void selectEnvironment(int envCode) {
        switch (envCode) {
            case 0:
                this.setCurrentEnvironment(new Menu("Main Menu"));
                break;
            case 1:
                break;
            default:
                assert false : "Weird Environment Token '" + envCode + "'";
        }
        this.getCurrentEnvironment().load();
        this.getCurrentEnvironment().init();
        this.getCurrentEnvironment().start();
    }
}
