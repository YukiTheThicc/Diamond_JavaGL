package diamond2DGL.listeners;

import diamond2DGL.Camera;
import diamond2DGL.Container;
import org.joml.Matrix4f;
import org.joml.Vector2f;
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
    private double posX, posY, lastX, lastY, worldX, worldY, lastWorldX, lastWorldY;
    private boolean buttonsPressed[] = new boolean[NUM_BUTTONS];
    private boolean isDragging;
    private Vector2f viewPortPos = new Vector2f();
    private Vector2f viewPortSize = new Vector2f();
    private int mouseButtonDown = 0;


    // CONSTRUCTORS
    private MouseListener() {
        this.sX = 0.0;
        this.sY = 0.0;
        this.posX = 0.0;
        this.posY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    // GETTERS & SETTERS
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

    public static float getScreenX() {
        float currentX = getX() - get().viewPortPos.x;
        currentX = (currentX / get().viewPortSize.x) * 1920f;
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - get().viewPortPos.y;
        currentY = 1080f -((currentY / get().viewPortSize.y) * 1080f);
        return currentY;
    }

    public static float getOrthoX() {
        return (float)get().worldX;
    }

    public static float getOrthoY() {
        return (float)get().worldY;
    }

    public static float getWorldDx() {
        return (float) (get().lastWorldX - get().worldX);
    }

    public static float getWorldDy() {
        return (float) (get().lastWorldY - get().worldY);
    }

    public Vector2f getViewPortPos() {
        return viewPortPos;
    }

    public Vector2f getViewPortSize() {
        return viewPortSize;
    }

    public static void setViewPortPos(Vector2f viewPortPos) {
        get().viewPortPos.set(viewPortPos);
    }

    public static void setViewPortSize(Vector2f viewPortSize) {
        get().viewPortSize.set(viewPortSize);
    }

    // METHODS
    public static MouseListener get() {
        if (MouseListener.listener == null) {
            MouseListener.listener = new MouseListener();
        }
        return MouseListener.listener;
    }

    private static void calcOrthoX() {
        Camera camera = Container.get().getGame().getCurrentEnvironment().getCamera();

        float currentX = getX() - get().viewPortPos.x;
        currentX = (currentX / get().viewPortSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Matrix4f viewProjection = new Matrix4f();
        camera.getInvView().mul(camera.getInvProj(), viewProjection);
        tmp.mul(viewProjection);
        currentX = tmp.x;
        get().worldX = currentX;
    }

    private static void calcOrthoY() {
        Camera camera = Container.get().getGame().getCurrentEnvironment().getCamera();

        float currentY = getY() - get().viewPortPos.y;
        currentY = -((currentY / get().viewPortSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Matrix4f viewProjection = new Matrix4f();
        camera.getInvView().mul(camera.getInvProj(), viewProjection);
        tmp.mul(viewProjection);
        currentY = tmp.y;
        get().worldY = currentY;
    }

    public static void mousePosCallback(long window, double posX, double posY) {
        if (get().mouseButtonDown > 0) {
            get().isDragging = true;
        }
        get().lastX = get().posX;
        get().lastY = get().posY;
        get().lastWorldX = get().worldX;
        get().lastWorldY = get().worldY;
        get().posX = posX;
        get().posY = posY;
        calcOrthoX();
        calcOrthoY();
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().mouseButtonDown++;
            if (button < NUM_BUTTONS) {
                get().buttonsPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            get().mouseButtonDown--;
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
        get().lastWorldX = get().worldX;
        get().lastWorldY = get().worldY;
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
