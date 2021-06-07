package diamond2DGL.listeners;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    // ATTRIBUTES
    private static final int NUM_KEYS = 350;                    // Number of key bindings supported by GLFW
    private static KeyListener listener;
    private boolean keyPressed[] = new boolean[NUM_KEYS];
    private boolean keyBeginPressed[] = new boolean[NUM_KEYS];

    // CONSTRUCTORS
    private KeyListener() {

    }

    // GETTERS & SETTERS

    // METHODS
    public static KeyListener get() {
        if (listener == null) {
            listener = new KeyListener();
        }
        return listener;
    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPressed, false);
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
            get().keyBeginPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
            get().keyBeginPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }

    public static boolean keyBeginPress(int keyCode) {
        return get().keyBeginPressed[keyCode];
    }
}
