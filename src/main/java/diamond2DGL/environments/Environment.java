package diamond2DGL.environments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamond2DGL.Camera;
import diamond2DGL.Entity;
import diamond2DGL.GSONDeserializers.ComponentDeserializer;
import diamond2DGL.GSONDeserializers.EntityDeserializer;
import diamond2DGL.engComponents.Component;
import diamond2DGL.observers.Observer;
import diamond2DGL.observers.events.Event;
import diamond2DGL.physics.Physics;
import diamond2DGL.renderer.Renderer;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Environment {

    // ATTRIBUTES
    private boolean isRunning;
    private Renderer renderer;
    private Camera camera;
    private List<Entity> entities;
    private EnvironmentFactory factory;
    private Physics physics;

    // CONSTRUCTORS
    public Environment(EnvironmentFactory factory) {
        this.factory = factory;
        this.physics = new Physics();
        this.renderer = new Renderer();
        this.entities = new ArrayList<>();
        this.isRunning = false;
    }

    // GETTERS & SETTERS
    public int getFPS(float dT) {
        return Math.round(1.0f / dT);
    }

    public Camera getCamera() {
        return this.camera;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(int id) {
        Optional<Entity> result = this.entities.stream()
                .filter(entity -> entity.getUid() == id)
                .findFirst();
        return result.orElse(null);
    }

    public Physics getPhysics() {
        return physics;
    }

    // METHODS
    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.factory.loadResources(this);
        this.factory.build(this);
    }

    public void start() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.start();
            this.renderer.add(e);
            this.physics.add(e);
        }
        isRunning = true;
    }

    public void addEntity(Entity e) {
        if (!isRunning) {
            entities.add(e);
        } else {
            entities.add(e);
            e.start();
            this.renderer.add(e);
            this.physics.add(e);
        }
    }

    public void editorUpdate(float dT) {
        this.camera.changeProjection();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.editorUpdate(dT);
            if (e.isDead()) {
                entities.remove(i);
                this.renderer.destroyEntity(e);
                this.physics.destroyEntity(e);
                i--;
            }
        }
    }

    public void update(float dT) {
        this.camera.changeProjection();
        this.physics.update(dT);
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.update(dT);
            if (e.isDead()) {
                entities.remove(i);
                this.renderer.destroyEntity(e);
                this.physics.destroyEntity(e);
                i--;
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public void imgui() {
        this.factory.imgui();
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            List<Entity> toSerialize = new ArrayList<>();
            for (Entity e : entities) {
                if (e.isToSerialize()) {
                    toSerialize.add(e);
                }
            }
            writer.write(gson.toJson(toSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {
            int maxEntityId = -1;
            int maxCompId = -1;
            Entity[] entities = gson.fromJson(inFile, Entity[].class);
            for (int i = 0; i < entities.length; i++) {
                addEntity(entities[i]);

                for (Component c : entities[i].getComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }

                if (entities[i].getUid() > maxEntityId) {
                    maxEntityId = entities[i].getUid();
                }
            }

            maxEntityId++;
            maxCompId++;
            Entity.init(maxEntityId);
            Component.init(maxCompId);
        }
    }

    public void destroy() {
        for (Entity e : entities) {
            e.destroy();
        }
    }
}