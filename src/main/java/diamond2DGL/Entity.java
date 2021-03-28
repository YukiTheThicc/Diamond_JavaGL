package diamond2DGL;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    private String name;
    private List<Component> components;
    public Transform transform;

    public Entity(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public Entity(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
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
}
