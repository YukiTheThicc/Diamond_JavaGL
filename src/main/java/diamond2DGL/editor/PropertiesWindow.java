package diamond2DGL.editor;

import diamond2DGL.Entity;
import diamond2DGL.engComponents.SpriteRenderer;
import diamond2DGL.physics.components.BoxCollider;
import diamond2DGL.physics.components.CircleCollider;
import diamond2DGL.physics.components.RigidBody;
import diamond2DGL.renderer.PickingTexture;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class PropertiesWindow {

    // ATTRIBUTES
    private List<Entity> activeEntities;
    private List<Vector4f> activeEntitiesColors;
    private Entity activeEntity = null;
    private PickingTexture pickingTexture;

    // CONSTRUCTORS
    public PropertiesWindow(PickingTexture pickingTexture) {
        this.activeEntities = new ArrayList<>();
        this.activeEntitiesColors = new ArrayList<>();
        this.pickingTexture = pickingTexture;
    }

    // GETTERS & SETTERS
    public Entity getActiveEntity() {
        return activeEntities.size() == 1 ? this.activeEntities.get(0) : null;
    }

    public List<Entity> getActiveEntities() {
        return activeEntities;
    }

    public void setActiveEntity(Entity activeEntity) {
        if (activeEntity != null) {
            clearSelected();
            this.activeEntities.add(activeEntity);
        }
    }

    // METHODS
    public void imgui() {
        if (activeEntities.size() == 1 && activeEntities.get(0) != null ) {
            activeEntity = activeEntities.get(0);
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

    public void addActiveEntity(Entity e) {
        SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
        if (spr != null) {
            this.activeEntitiesColors.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        } else {
            this.activeEntitiesColors.add(new Vector4f());
        }
        this.activeEntities.add(e);
    }

    public void clearSelected() {
        if (activeEntitiesColors.size() > 0) {
            int i = 0;
            for (Entity e : activeEntities) {
                SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
                if (spr != null) {
                    spr.setColor(activeEntitiesColors.get(i));
                }
                i++;
            }
        }
        this.activeEntities.clear();
        this.activeEntitiesColors.clear();
    }
}
