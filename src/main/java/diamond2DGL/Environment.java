package diamond2DGL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamond2DGL.GSONDeserializers.ComponentDeserializer;
import diamond2DGL.GSONDeserializers.EntityDeserializer;
import diamond2DGL.renderer.Renderer;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Environment {

    // ATTRIBUTES
    private String name;
    private boolean isRunning = false;
    protected boolean loadedEnvironment = false;
    protected Renderer renderer;
    protected Camera camera;
    protected List<Entity> entities;
    protected Entity activeEntity = null;

    // CONSTRUCTORS
    public Environment(String name) {
        this.name = name;
        this.entities = new ArrayList<>();
        this.renderer = new Renderer();
    }

    // GETTERS & SETTERS
    public int getFPS(float dT) {
        return Math.round(1.0f / dT);
    }

    public Camera getCamera() {
        return this.camera;
    }

    // METHODS
    public abstract void init();

    public void start() {
        for (Entity e : this.entities) {
            e.start();
            this.renderer.add(e);
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
        }
    }

    public abstract void update(float dT);

    public abstract void render();

    public void envImgui() {
        if (activeEntity != null) {
            ImGui.begin("Inspector");
            activeEntity.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.entities));
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
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {
            Entity[] entities = gson.fromJson(inFile, Entity[].class);
            for (int i = 0; i < entities.length; i++) {
                addEntity(entities[i]);
            }
            this.loadedEnvironment = true;
        }
    }
}