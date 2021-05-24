package diamond2DGL;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/*
 * SINGLETON CLASS
 */
public class MouseListener {

    // CONSTANTS
    private static final int NUM_BUTTONS = 3;

    // ATTRIBUTES
    private static MouseListener listener;
    private double sX, sY;
    private double posX, posY, lastX, lastY;
    private boolean buttonsPressed[] = new boolean[NUM_BUTTONS];
    private boolean isDragging;

    // CONSTRUCTORS
    private MouseListener() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    // METHODS
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

    public static float getOrthoX() {
        float currentX = getX();
        currentX = (currentX / (float)Display.getWidth()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        Environment currentEnv = Container.get().getGame().getCurrentEnvironment();
        tmp.mul(currentEnv.getCamera().getInvProj()).mul(currentEnv.getCamera().getInvView());
        currentX = tmp.x;
        return currentX;
    }

    public static float getOrthoY() {
        Environment currentEnv = Container.get().getGame().getCurrentEnvironment();
        float currentY = Display.getHeight() - getY();
        currentY = (currentY / (float)Display.getHeight()) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(currentEnv.getCamera().getInvProj()).mul(currentEnv.getCamera().getInvView());
        currentY = tmp.y;
        return currentY;
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
