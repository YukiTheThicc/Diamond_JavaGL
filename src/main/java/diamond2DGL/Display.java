package diamond2DGL;

import diamond2DGL.editor.PickingTexture;
import diamond2DGL.listeners.KeyListener;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.renderer.FrameBuffer;
import diamond2DGL.renderer.Renderer;
import diamond2DGL.renderer.Shader;
import diamond2DGL.utils.AssetManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Display {

    // ATTRIBUTES
    private int x, y;
    private String title = "Diamond2DGL v0.0.1[PRE-ALPHA]";
    private static Display display = null;
    private long glfwWindow;
    private ImGUILayer imGUILayer;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;
    // TODO - CHANGE SHADER SYSTEM
    private Shader defaultShader;
    private Shader pickingShader;

    // CONSTRUCTORS
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
        /* Chaos ensues if this is uncommented, uncomment at your own will */
        /*glfwSetWindowSizeCallback(this.glfwWindow, (w, newX, newY) -> {
            Display.setWidth(newX);
            Display.setHeight(newY);
        });*/

        // Make the OpenGL current context the engine window
        glfwMakeContextCurrent(this.glfwWindow);
        // Enabled buffer swapping (v-sync) by default
        glfwSwapInterval(1);
        // Show the window
        glfwShowWindow(this.glfwWindow);
        // Makes the OpenGL bindings available for use
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.frameBuffer = new FrameBuffer(1920, 1080);
        this.pickingTexture = new PickingTexture(1920, 1080);
        this.imGUILayer = new ImGUILayer(this.glfwWindow, this.pickingTexture);
        this.imGUILayer.initImGui();

        glViewport(0,0, 1920, 1080);

        this.defaultShader = AssetManager.getShader("assets/shaders/default.glsl");
        this.pickingShader = AssetManager.getShader("assets/shaders/pickingShader.glsl");
    }

    // GETTERS & SETTERS
    public String getTitle() {
        return title;
    }

    public static Display getDisplay() {
        return display;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static int getWidth() {
        return get().x;
    }

    public static int getHeight() {
        return get().y;
    }

    public static FrameBuffer getFrameBuffer() {
        return get().frameBuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }

    public static ImGUILayer getImGuiLayer() {
        return get().imGUILayer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void setDisplay(Display display) {
        Display.display = display;
    }

    public static void setWidth(int newX) {
        get().x = newX;
    }

    public static void setHeight(int newY) {
        get().y = newY;
    }

    // METHODS
    public static Display get() {
        if (Display.display == null) {
            Display.display = new Display();
        }
        return Display.display;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void texturePickingRender() {
        // TODO - Change to only do texture picking if in editor
        glDisable(GL_BLEND);
        pickingTexture.enableWriting();
        glViewport(0,0, 1920, 1080);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer.bindShader(pickingShader);
        Container.getEnv().render();
        pickingTexture.disableWriting();
        glEnable(GL_BLEND);
    }

    public void clear() {
        this.frameBuffer.bind();
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
        Renderer.bindShader(defaultShader);
    }

    public void update(float dT) {
        this.frameBuffer.unBind();
        this.imGUILayer.update(dT);
        glfwSwapBuffers(this.glfwWindow);
        MouseListener.update();
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
