package diamond2DGL;

import diamond2DGL.renderer.PickingTexture;
import diamond2DGL.listeners.KeyListener;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.renderer.FrameBuffer;
import diamond2DGL.renderer.Renderer;
import diamond2DGL.renderer.Shader;
import diamond2DGL.utils.AssetManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    // ATTRIBUTES
    private int sizeX, sizeY;
    private IntBuffer posX, posY;
    private String title;
    private long glfwWindow;
    private static Window window = null;
    private long audioContext;
    private long audioDevice;
    private ImGUILayer imGUILayer;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;

    // TODO - CHANGE SHADER SYSTEM
    private Shader defaultShader;
    private Shader pickingShader;

    // CONSTRUCTORS
    private Window() {
        this.sizeX = 1600;
        this.sizeY = 900;
        stackPush();
        this.posX = stackCallocInt(1);
        stackPush();
        this.posY = stackCallocInt(1);
        this.title = "Diamond2DGL v0.0.1[PRE-ALPHA]";
        stackPop();
        stackPop();
    }

    // GETTERS & SETTERS
    public String getTitle() {
        return title;
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public static int getWidth() {
        return get().sizeX;
    }

    public static int getHeight() {
        return get().sizeY;
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

    public static PickingTexture getPickingTexture() {
        return get().pickingTexture;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void setWidth(int newX) {
        get().sizeX = newX;
    }

    public static void setHeight(int newY) {
        get().sizeY = newY;
    }

    public static Vector2f getPosition() {
        Window window = get();
        Vector2f pos = new Vector2f();
        org.lwjgl.glfw.GLFW.glfwGetWindowPos(window.glfwWindow, window.posX, window.posY);
        pos.x = window.posX.get(0);
        pos.y = window.posY.get(0);
        return pos;
    }

    // METHODS
    public void resizeCallback() {
        System.out.println("Resizing");
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        // Set default window state
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        glfwWindow = glfwCreateWindow(sizeX, sizeY, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newX, newY) -> {
            Window.setWidth(newX);
            Window.setHeight(newY);
        });

        // Make the OpenGL current context the engine window
        glfwMakeContextCurrent(glfwWindow);
        // Enabled buffer swapping (v-sync) by default
        glfwSwapInterval(1);
        // Show the window
        glfwShowWindow(glfwWindow);
        // Initialize audio
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported";
        }

        // Makes the OpenGL bindings available for use
        GL.createCapabilities();

        glDisable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        frameBuffer = new FrameBuffer(sizeX, sizeY);
        pickingTexture = new PickingTexture(sizeX, sizeY);
        glViewport((int)getPosition().x,(int)getPosition().y, sizeX, sizeY);
        imGUILayer = new ImGUILayer(glfwWindow);
        imGUILayer.initImGui();

        this.defaultShader = AssetManager.getShader("assets/shaders/default.glsl");
        this.pickingShader = AssetManager.getShader("assets/shaders/pickingShader.glsl");
    }

    public static Window get() {
        if (window == null) {
            window = new Window();
        }
        return Window.window;
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public void texturePickingRender() {
        // TODO - Change to only do texture picking if in editor
        glDisable(GL_BLEND);
        pickingTexture.enableWriting();
        glViewport(0, 0, sizeX, sizeY);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer.bindShader(pickingShader);
        Container.getEnv().render();
        pickingTexture.disableWriting();
        glEnable(GL_BLEND);
    }

    public void startFrame() {
        frameBuffer.bind();
        glClearColor(1, 1, 1, 1);
        glClear(GL_COLOR_BUFFER_BIT);
        Renderer.bindShader(defaultShader);
    }

    public void endFrame() {
        frameBuffer.unBind();
        imGUILayer.update();
        MouseListener.endFrame();
        KeyListener.endFrame();
        glfwSwapBuffers(glfwWindow);
    }

    public void close() {
        // Free allocated memory
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        posX = null;
        posY = null;

        // Termination of GLFW
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
