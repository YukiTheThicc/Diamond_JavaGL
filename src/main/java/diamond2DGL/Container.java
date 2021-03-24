package diamond2DGL;
import static org.lwjgl.glfw.GLFW.*;

public class Container implements Runnable {

    private Display display;
    private Game game;

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
        float beginTime = 0;
        float endTime = this.getTime();
        float dT = 0;

        while (this.running) {
            // GAME UPDATE PROCEDURE
            this.game.update(dT);

            // GAME RENDER PROCEDURE
            this.display.update();
            this.game.render();

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
