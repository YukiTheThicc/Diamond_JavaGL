package diamond2DGL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamond2DGL.GSONDeserializers.ComponentDeserializer;
import diamond2DGL.GSONDeserializers.EntityDeserializer;
import diamond2DGL.engComponents.Component;
import diamond2DGL.engComponents.SpriteRenderer;
import diamond2DGL.engComponents.Transform;
import diamond2DGL.utils.AssetManager;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

public class Entity {

    // ATTRIBUTES
    private static int ID_COUNTER = 0;

    public transient Transform transform;
    public String name;
    private int uid = -1;
    private List<Component> components;
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

    public Entity copy() {
        // TODO - Do it cleaner next time plis
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String entityJson = gson.toJson(this);
        Entity e = gson.fromJson(entityJson, Entity.class);
        e.generateUid();
        for (Component c : e.getComponents()) {
            c.generateId();
        }
        SpriteRenderer sprite = e.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetManager.getTexture(sprite.getTexture().getPath()));
        }
        return e;
    }

    public void generateUid() {
        this.uid = ID_COUNTER++;
    }
}
