package diamond2DGL;

import diamond2DGL.environments.Environment;
import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.Observer;
import diamond2DGL.observers.events.Event;
import diamond2DGL.observers.handlers.EditorEventHandler;
import diamond2DGL.physics.Physics;
import diamond2DGL.renderer.DebugDraw;

import static org.lwjgl.glfw.GLFW.*;

/*
 * SINGLETON - Game container class
 */
public class Container {

    // ATTRIBUTES
    private static Container container = null;
    private final Game game;
    private final Display display;
    private boolean running;
    private boolean editorPlaying = false;

    // CONSTRUCTOR
    public Container(Game game) {
        this.running = false;
        this.game = game;
        this.display = Display.get();
        EventSystem.addObserver(new EditorEventHandler());
    }

    // GETTERS AND SETTERS
    private Display getDisplay() {
        return display;
    }

    public static Game getGame() {
        return get().game;
    }

    public boolean isRunning() {
        return running;
    }

    public static Environment getEnv() {
        return get().game.getCurrentEnvironment();
    }

    public static Camera getCamera() {
        return get().game.getCurrentEnvironment().getCamera();
    }

    public static void playEditor() {
        getEnv().getPhysics().getWorld().clearForces();
        get().editorPlaying = true;
    }

    public static void stopEditor() {
        get().editorPlaying = false;
    }

    // METHODS
    private void dispose() {
        this.getDisplay().close();
    }

    private boolean shouldClose() {
        return glfwWindowShouldClose(this.display.getGlfwWindow());
    }

    public static void init(Game game) {
        container = new Container(game);
    }

    public static Container get() {
        return container;
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


            this.display.clear();

            if (dt >= 0) {
                if (!editorPlaying) {
                    DebugDraw.beginFrame();
                    DebugDraw.draw();
                    this.game.editorUpdate(dt);
                } else {
                    this.game.update(dt);
                }
                this.game.render();
                this.display.update(dt);
            }

            et = (float) glfwGetTime();
            dt = et - bt;
            bt = et;
            this.running = !this.shouldClose();
        }
        this.dispose();
    }
}


