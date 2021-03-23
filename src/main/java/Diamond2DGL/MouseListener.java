package Diamond2DGL;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {

    private static final int NUM_BUTTONS = 3;

    private static MouseListener listener;
    private double sX, sY;
    private double posX, posY, lastX, lastY;
    private boolean buttonsPressed[] = new boolean[NUM_BUTTONS];
    private boolean isDragging;

    private MouseListener() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.listener == null) {
            MouseListener.listener = new MouseListener();
        }
        return MouseListener.listener;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().posX = posX;
        get().posY = posY;
        get().isDragging = get().buttonsPressed[0] || get().buttonsPressed[1] || get().buttonsPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < NUM_BUTTONS) {
                get().buttonsPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().buttonsPressed.length) {
                get().buttonsPressed[button] = false;
            }
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double offX, double offY) {
        get().sX = offX;
        get().sY = offY;
    }

    public static void update() {
        get().sX = 0;
        get().sY = 0;
        get().lastX = get().posX;
        get().lastY = get().posY;
    }

    public static float getX() {
        return (float) get().posX;
    }

    public static float getY() {
        return (float) get().posY;
    }

    public static float getDx() {
        return (float) (get().lastX - get().posX);
    }

    public static float getDy() {
        return (float) (get().lastY - get().posY);
    }

    public static float getScrollX() {
        return (float) get().sX;
    }

    public static float getScrollY() {
        return (float) get().sY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < NUM_BUTTONS) {
            return get().buttonsPressed[button];
        } else {
            return false;
        }
    }
}
