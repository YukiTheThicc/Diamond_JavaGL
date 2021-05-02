package diamond2DGL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {

    private int x, y;
    private String title = "Diamond2DGL v0.0.1[PRE-ALPHA]";
    private static Display display = null;
    private long glfwWindow;

    private Display() {
        this.x = 1600;
        this.y = 900;

        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        // Set default window state
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        this.glfwWindow = glfwCreateWindow(this.x, this.y, this.title, NULL, NULL);
        if (this.glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        glfwSetCursorPosCallback(this.glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(this.glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(this.glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL current context the engine window
        glfwMakeContextCurrent(this.glfwWindow);
        // Enabled buffer swapping (v-sync) by default
        glfwSwapInterval(1);
        // Show the window
        glfwShowWindow(this.glfwWindow);
        // Makes the OpenGL bindings available for use
        GL.createCapabilities();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Display getDisplay() {
        return display;
    }

    public static void setDisplay(Display display) {
        Display.display = display;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static Display get() {
        if (Display.display == null) {
            Display.display = new Display();
        }
        return Display.display;
    }

    public void clear() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(this.glfwWindow);
    }

    public void close() {
        // Free allocated memory
        glfwFreeCallbacks(this.glfwWindow);
        glfwDestroyWindow(this.glfwWindow);

        // Termination of GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
