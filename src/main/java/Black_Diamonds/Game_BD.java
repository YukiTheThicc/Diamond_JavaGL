package Black_Diamonds;

import Diamond2DGL.Container;
import Diamond2DGL.IGame;
import Diamond2DGL.Renderer;

public class Game_BD implements IGame {

    private Container container;

    public Game_BD(int maxFPS) {
        this.container = new Container(maxFPS, this);
    }

    @Override
    public void start() {
        this.container.run();
    }

    @Override
    public void update(Container c, float dt) {

    }

    @Override
    public void render(Container c, Renderer r) {

    }
}
