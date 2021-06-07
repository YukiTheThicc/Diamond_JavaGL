package diamond2DGL.engComponents;

import diamond2DGL.Window;
import diamond2DGL.listeners.KeyListener;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;

public class GizmoSystem extends Component{

    // ATTRIBUTES
    private SpriteSheet gizmos;
    private int usingGizmo = 0;

    // CONSTRUCTORS
    public GizmoSystem (SpriteSheet gizmoSprites) {
        this.gizmos = gizmoSprites;
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void start() {
        parent.addComponent(new TranslateGizmo(gizmos.getSprite(1), Window.getImGuiLayer().getPropertiesWindow()));
        parent.addComponent(new ScaleGizmo(gizmos.getSprite(2), Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dT) {
        switch (usingGizmo) {
            case 0:
                parent.getComponent(TranslateGizmo.class).setUsing();
                parent.getComponent(ScaleGizmo.class).setNotUsing();
                break;
            case 1:
                parent.getComponent(TranslateGizmo.class).setNotUsing();
                parent.getComponent(ScaleGizmo.class).setUsing();
                break;
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            usingGizmo = 0;
        } else if (KeyListener.isKeyPressed(GLFW_KEY_R)) {
            usingGizmo = 1;
        }
    }
}
