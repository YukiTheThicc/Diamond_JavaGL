package diamond2DGL.editor;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.engComponents.NonPickable;
import diamond2DGL.listeners.MouseListener;
import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    // ATTRIBUTES
    private Entity activeEntity = null;
    private PickingTexture pickingTexture;
    private float debounce = 0.2f;

    // CONSTRUCTORS
    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    // GETTERS & SETTERS
    public Entity getActiveEntity() {
        return activeEntity;
    }

    // METHODS
    public void update (float dT) {
        debounce -= dT;
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0f) {
            int x = (int)MouseListener.getScreenX();
            int y = (int)MouseListener.getScreenY();
            int entityId = pickingTexture.readPixel(x, y);
            Entity entity = Container.getEnv().getEntity(entityId);
            if (entity != null && entity.getComponent(NonPickable.class) == null) {
                activeEntity = entity;
            } else if (entity == null && !MouseListener.isDragging()) {
                activeEntity = null;
            }
            this.debounce = 0.2f;
        }
        imgui();
    }

    public void imgui() {
        if (activeEntity != null) {
            ImGui.begin("Properties");
            activeEntity.imgui();
            ImGui.end();
        }
    }
}
