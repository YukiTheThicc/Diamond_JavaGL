package diamond2DGL;

import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.handlers.EditorEventHandler;
import diamond2DGL.renderer.DebugDraw;

import static org.lwjgl.glfw.GLFW.*;

/*
 * SINGLETON - Game container class
 */
public class Container {

    // ATTRIBUTES
    private static Container container = null;
    private final Game game;
    private final Window window;
    private boolean running;
    private boolean editorPlaying = false;

    // CONSTRUCTOR
    private Container(Game game) {
        this.running = false;
        this.game = game;
        this.window = Window.get();
        this.window.init();
        EventSystem.addObserver(new EditorEventHandler());
    }

    // GETTERS AND SETTERS
    private Window getDisplay() {
        return window;
    }

    public static Game getGame() {
        return container.game;
    }

    public boolean isRunning() {
        return running;
    }

    public static Environment getEnv() {
        return container.game.getCurrentEnvironment();
    }

    public static Camera getCamera() {
        return container.game.getCurrentEnvironment().getCamera();
    }

    // METHODS
    /**
     * Initializes the Container SINGLETON
     *
     * @param game: Instance of a game
     */
    public static void init(Game game) {
        container = new Container(game);
    }

    /**
     * Returns the current instance of the container
     *
     * @return Current Container
     */
    public static Container get() {
        return container;
    }

    public static void playEditor() {
        getEnv().getPhysics().getWorld().clearForces();
        container.editorPlaying = true;
    }

    public static void stopEditor() {
        container.editorPlaying = false;
    }

    private void dispose() {
        container.getDisplay().close();
    }

    private boolean shouldClose() {
        return glfwWindowShouldClose(container.window.getGlfwWindow());
    }

    public void stop() {
        container.running = false;
    }

    public void run() {
        this.running = true;
        float bt = (float) glfwGetTime();
        float et = (float) glfwGetTime();
        float dt = 0f;
        while (this.running) {
            this.window.pollEvents();
            this.window.texturePickingRender();
            this.window.startFrame();
            if (dt >= 0) {
                if (!editorPlaying) {
                    this.game.editorUpdate(dt);
                } else {
                    this.game.update(dt);
                }
                this.game.render();
                DebugDraw.beginFrame();
                DebugDraw.draw();
            }
            this.window.endFrame();
            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
        this.dispose();
    }
}


