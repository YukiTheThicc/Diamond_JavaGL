package diamond2DGL;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    // ATTRIBUTES
    private String name;
    private List<Component> components;
    private int zIndex;
    public Transform transform;


    // CONSTRUCTORS
    public Entity(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.zIndex = 0;
        this.transform = new Transform();
    }

    public Entity(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
        this.transform = transform;
    }

    // GETTERS & SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getzIndex() {
        return zIndex;
    }

    // METHODS
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
        this.components.add(c);
        c.parent = this;
    }

    public void update(float dT) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dT);
        }
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imgui() {
        for (Component c : components) {
            c.imgui();
        }
    }
}
