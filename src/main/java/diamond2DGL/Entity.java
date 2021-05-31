package diamond2DGL;

import diamond2DGL.engComponents.Component;
import diamond2DGL.engComponents.Transform;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    // ATTRIBUTES
    private static int ID_COUNTER = 0;

    private int uid = -1;
    private String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean toSerialize = true;
    private boolean isDead = false;

    // CONSTRUCTORS
    public Entity(String name) {
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    // GETTERS & SETTERS
    public int getUid() {
        return this.uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Component> getComponents() {
        return components;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isToSerialize() {
        return toSerialize;
    }

    public boolean setToSerialize() {
        return toSerialize = true;
    }

    public void setNotToSerialize() {
        this.toSerialize = false;
    }

    // METHODS
    public void destroy() {
        this.isDead = true;
        for (int i=0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void editorUpdate(float dT) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).editorUpdate(dT);
        }
    }

    public void update(float dT) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dT);
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            try {
                if (componentClass.isAssignableFrom(c.getClass())) {
                    return componentClass.cast(c);
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
                assert false : "ERROR: While casting component";
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c) {
        c.generateId();
        this.components.add(c);
        c.parent = this;
    }

    public void imgui() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName())) {
                c.imgui();
            }
        }
    }


}
