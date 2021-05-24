package diamond2DGL;

import diamond2DGL.engComponents.Component;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    // ATTRIBUTES
    private static int ID_COUNTER = 0;

    private int uid = -1;
    private String name;
    private List<Component> components;
    private int zIndex;
    public Transform transform;


    // CONSTRUCTORS
    public Entity(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.zIndex = zIndex;
        this.transform = transform;

        this.uid = ID_COUNTER++;
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

    public List<Component> getComponents() {
        return components;
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
        c.generateId();
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

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public int getUid() {
        return this.uid;
    }
}
