package Diamond2DGL;

import java.sql.Time;

import static org.lwjgl.glfw.GLFW.*;

public class Container implements Runnable {

    private Display display;
    private IGame game;

    private static final float START_TIME = System.nanoTime();
    private boolean running;
    private int maxFPS;
    private double update_cap;

    public Container(int maxFPS, IGame game) {
        this.maxFPS = maxFPS;
        this.update_cap = 1.0 / this.maxFPS;
        this.running = false;
        this.game = game;
        this.display = Display.get();
    }

    // --GETTERS AND SETTERS--
    public Display getDisplay() {
        return display;
    }

    public IGame getGame() {
        return game;
    }

    public boolean isRunning() {
        return running;
    }

    public int getMaxFPS() {
        return maxFPS;
    }

    public double getUpdate_cap() {
        return update_cap;
    }

    //--METHODS--
    private boolean shouldClose() {
        return glfwWindowShouldClose(this.display.getGlfwWindow());
    }

    private float getTime() {
        return (float) ((System.nanoTime() - START_TIME) * 1E-9);
    }

    public void stop() {

    }

    public void run() {
        this.running = true;

        boolean render = false;
        float beginTime = 0;
        float endTime = this.getTime();
        float dT = 0;
        double unprocessedTime = 0;

        int fps = 0;

        while (this.running) {
            // GAME UPDATE PROCEDURE
            this.game.update(this, fps);

            // GAME RENDER PROCEDURE
            this.display.update();

            endTime = this.getTime();
            dT = endTime - beginTime;
            beginTime = endTime;
            this.running = !this.shouldClose();
        }
        this.dispose();
    }

    private void dispose() {
        this.getDisplay().close();
    }
}
