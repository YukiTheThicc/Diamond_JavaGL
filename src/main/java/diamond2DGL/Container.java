package diamond2DGL;

import static org.lwjgl.glfw.GLFW.*;

public class Container implements Runnable {

    private final Display display;
    private final Game game;

    private static final float START_TIME = System.nanoTime();
    private boolean running;

    public Container(Game game) {
        this.running = false;
        this.game = game;
        this.display = Display.get();
    }

    // --GETTERS AND SETTERS--

    public Display getDisplay() {
        return display;
    }

    public Game getGame() {
        return game;
    }

    public boolean isRunning() {
        return running;
    }

    //--METHODS--
    private boolean shouldClose() {
        return glfwWindowShouldClose(this.display.getGlfwWindow());
    }

    private float getTime() {
        return (float) ((System.nanoTime() - START_TIME) * 1E-9);
    }

    public void stop() {
        this.running = false;
    }

    public void run() {
        this.running = true;
        float beginTime = this.getTime();
        float endTime = 0;
        float dT = 0;

        while (this.running) {
            // GAME UPDATE PROCEDURE
            this.display.clear();
            this.game.update(dT);

            // GAME RENDER PROCEDURE
            this.game.render(this.getTime());
            this.display.update();

            endTime = this.getTime();
            dT = endTime - beginTime;
            beginTime = endTime;
            if (dT > 0) {

            }
            this.running = !this.shouldClose();
        }
        this.dispose();
    }

    private void dispose() {
        this.getDisplay().close();
    }
}
