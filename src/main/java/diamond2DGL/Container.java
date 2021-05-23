package diamond2DGL;

import static org.lwjgl.glfw.GLFW.*;

public class Container implements Runnable {

    private final Display display;
    private final Game game;
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

    public void stop() {
        this.running = false;
    }

    public void run() {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et = (float) glfwGetTime();
        float dt = 0f;

        while (this.running) {
            this.display.clear();
            this.game.update(dt);
            this.game.render();
            this.display.update(dt, this.game.getCurrentEnvironment());

            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
        this.getGame().getCurrentEnvironment().saveExit();
        this.dispose();
    }

    private void dispose() {
        this.getDisplay().close();
    }
}
