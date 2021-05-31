package diamond2DGL.editor;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.engComponents.NonPickable;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.physics.components.BoxCollider;
import diamond2DGL.physics.components.CircleCollider;
import diamond2DGL.physics.components.RigidBody;
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

    public void setActiveEntity(Entity activeEntity) {
        this.activeEntity = activeEntity;
    }

    // METHODS
    public void editorUpdate (float dT) {
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

    public void update (float dT) {
        debounce -= dT;
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0f) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int entityId = pickingTexture.readPixel(x, y);
            Entity entity = Container.getEnv().getEntity(entityId);
            if (entity != null && entity.getComponent(NonPickable.class) == null) {
                activeEntity = entity;
            } else if (entity == null && !MouseListener.isDragging()) {
                activeEntity = null;
            }
            this.debounce = 0.2f;
        }
    }

    public void imgui() {
        if (activeEntity != null) {
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigid Body")) {
                    if (activeEntity.getComponent(RigidBody.class) == null) {
                        activeEntity.addComponent(new RigidBody());
                    }
                }
                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeEntity.getComponent(BoxCollider.class) == null && activeEntity.getComponent(CircleCollider.class) == null) {
                        activeEntity.addComponent(new BoxCollider());
                    }
                }
                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeEntity.getComponent(CircleCollider.class) == null && activeEntity.getComponent(BoxCollider.class) == null) {
                        activeEntity.addComponent(new CircleCollider());
                    }
                }
                ImGui.endPopup();
            }

            activeEntity.imgui();
            ImGui.end();
        }
    }
}
