package diamond2DGL;

import diamond2DGL.renderer.DebugDraw;
import diamond2DGL.renderer.Renderer;
import diamond2DGL.utils.AssetManager;

import static org.lwjgl.glfw.GLFW.*;

/*
* SINGLETON - Game container class
*/
public class Container implements Runnable {

    // ATTRIBUTES
    private static Container container = null;
    private final Game game;
    private final Display display;
    private boolean running;

    // CONSTRUCTOR
    public Container(Game game) {
        this.running = false;
        this.game = game;
        this.display = Display.get();
    }

    // GETTERS AND SETTERS
    private Display getDisplay() {
        return display;
    }

    public Game getGame() {
        return game;
    }

    public boolean isRunning() {
        return running;
    }

    public static Environment getEnv() {
        return Container.get().getGame().getCurrentEnvironment();
    }

    public static Camera getCamera() {
        return Container.get().getGame().getCurrentEnvironment().getCamera();
    }

    // METHODS
    public static void init(Game game) {
        container = new Container(game);
    }

    public static Container get() {
        return container;
    }

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
            this.display.pollEvents();

            this.display.texturePickingRender();

            DebugDraw.beginFrame();
            this.display.clear();

            if (dt >= 0) {
                DebugDraw.draw();
                this.game.update(dt);
                this.game.render();
                this.display.update(dt);
            }

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
