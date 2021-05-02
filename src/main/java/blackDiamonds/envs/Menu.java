package blackDiamonds.envs;

import diamond2DGL.Camera;
import diamond2DGL.Entity;
import diamond2DGL.Environment;
import diamond2DGL.Transform;
import diamond2DGL.engComponents.SpriteRenderer;
import diamond2DGL.utils.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Menu extends Environment {

    public Menu(String name) {
        super(name);
    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOff = 10;
        int yOff = 10;

        float totX = (float) (600 - xOff * 2);
        float totY = (float) (300 - yOff * 2);
        float sizeX = totX / 100f;
        float sizeY = totY / 100f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOff + (x * sizeX);
                float yPos = yOff + (y + sizeY);
                Entity e = new Entity("Obj_" + x + "_" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                e.addComponent(new SpriteRenderer(new Vector4f(xPos / totX, yPos / totY, 1, 1)));
                this.addEntity(e);
            }
        }

        loadResources();
    }

    public void loadResources() {
        ResourceManager.getShader("src/main/resources/shaders/default.glsl");
    }

    @Override
    public void update(float dT) {
        for (Entity e : this.entities) {
            e.update(dT);
        }
        System.out.println(this.getFPS(dT));
    }

    @Override
    public void render() {
        this.renderer.render(this.camera);
    }
}
