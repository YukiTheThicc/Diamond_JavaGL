package diamond2DGL.listeners;


import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    // Number of key bindings supported by GLFW
    private static final int NUM_KEYS = 350;

    private static KeyListener listener;
    private boolean keyPressed[] = new boolean[NUM_KEYS];
    private boolean keyBeginPressed[] = new boolean[NUM_KEYS];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (listener == null) {
            listener = new KeyListener();
        }
        return listener;
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
        boolean result = get().keyBeginPressed[keyCode];
        if (result) {
            get().keyBeginPressed[keyCode] = false;
        }
        return result;
    }
}
