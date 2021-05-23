package blackDiamonds.envs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diamond2DGL.*;
import diamond2DGL.GSONDeserializers.ComponentDeserializer;
import diamond2DGL.GSONDeserializers.EntityDeserializer;
import diamond2DGL.engComponents.RigidBody;
import diamond2DGL.engComponents.SpriteRenderer;
import diamond2DGL.engComponents.SpriteSheet;
import diamond2DGL.utils.AssetManager;
import imgui.ImGui;
import org.joml.Vector2f;

public class Menu extends Environment {

    public Menu(String name) {
        super(name);
    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (loadedEnvironment) {
            this.activeEntity = entities.get(0);
            return;
        }

        SpriteSheet sprites = AssetManager.getSpriteSheet("assets/testing/spritesheet.png");

        Entity entity1 = new Entity("Entity 1",
                new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 4);
        SpriteRenderer spriteRenderer1 = new SpriteRenderer();
        spriteRenderer1.setSprite(sprites.getSprite(0));
        entity1.addComponent(spriteRenderer1);
        entity1.addComponent(new RigidBody());
        this.addEntity(entity1);

        Entity entity2 = new Entity("Entity 2",
                new Transform(new Vector2f(500, 100), new Vector2f(256, 256)), -2);
        SpriteRenderer spriteRenderer2 = new SpriteRenderer();
        spriteRenderer2.setSprite(sprites.getSprite(0));
        entity2.addComponent(spriteRenderer2);
        this.addEntity(entity2);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .create();
        String res = gson.toJson(entity1);
        System.out.println(res);
    }

    public void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");

        AssetManager.addSpriteSheet("assets/testing/spritesheet.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/spritesheet.png"),
                        16, 16, 26, 0));
    }

    @Override
    public void update(float dT) {
        entities.get(0).transform.position.x += 10*dT;
        for (Entity e : this.entities) {
            e.update(dT);
        }
    }

    @Override
    public void render() {
        this.renderer.render(this.camera);
    }

    @Override
    public void imgui() {
        ImGui.begin("YAYEYYEET");



        ImGui.end();
    }
}
