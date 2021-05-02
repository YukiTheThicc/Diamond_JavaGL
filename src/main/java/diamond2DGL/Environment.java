package diamond2DGL;

import diamond2DGL.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Environment {

    private String name;
    private boolean isRunning = false;
    protected Renderer renderer;
    protected Camera camera;
    protected List<Entity> entities;


    public Environment(String name) {
        this.name = name;
        this.entities = new ArrayList<>();
        this.renderer = new Renderer();
    }

    public int getFPS(float dT) {
        return Math.round(1.0f / dT);
    }

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

    public Camera camera() {
        return this.camera;
    }
}