package diamond2DGL.engComponents;

import diamond2DGL.Camera;
import diamond2DGL.listeners.KeyListener;
import diamond2DGL.listeners.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DECIMAL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class EditorCamera extends Component{

    // ATTRIBUTES
    private Camera camera;
    private Vector2f clickOrigin;
    private float dragLag = 0.032f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;
    private boolean reset = false;
    private float lerpTime = 0.0f;

    // CONSTRUCTORS
    public EditorCamera(Camera camera) {
        this.camera = camera;
        this.clickOrigin = new Vector2f();
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void update(float dT) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragLag > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragLag -= dT;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos.x, mousePos.y).sub(this.clickOrigin);
            camera.pos.sub(delta.mul(dT).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, dT);
        }

        if (dragLag <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragLag = 0.032f;
        }

        if (MouseListener.getScreenY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY()) * scrollSensitivity, 1 / camera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            camera.addZoom(addValue);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_KP_DECIMAL)) {
            reset = true;
        }

        if (reset) {
            camera.pos.lerp(new Vector2f(), lerpTime);
            camera.setZoom(camera.getZoom() + ((1.0f - camera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dT;
            if (Math.abs(camera.pos.x) <= 5f && Math.abs(camera.pos.y) <= 5f) {
                lerpTime = 0.0f;
                camera.setZoom(1.0f);
                camera.pos.set(0f, 0f);
                reset = false;
            }
        }
    }
}
