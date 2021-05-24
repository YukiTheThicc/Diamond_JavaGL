package blackDiamonds.envs;

import blackDiamonds.EntityGenerator;
import diamond2DGL.*;
import diamond2DGL.engComponents.*;
import diamond2DGL.utils.AssetManager;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class Menu extends Environment {

    private SpriteSheet sprites;
    MouseControls mouseControls = new MouseControls();

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
        spriteRenderer2.setSprite(sprites.getSprite(1));
        entity2.addComponent(spriteRenderer2);
        this.addEntity(entity2);
    }

    public void loadResources() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.addSpriteSheet("assets/testing/decorationsAndBlocks.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        sprites = AssetManager.getSpriteSheet("assets/testing/decorationsAndBlocks.png");
    }

    @Override
    public void update(float dT) {
        mouseControls.update(dT);
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

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < this.sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float sprWidth = sprite.getWidth() * 4;
            float sprHeight = sprite.getHeight() * 4;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, sprWidth, sprHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                Entity tile = EntityGenerator.generateTile(sprite, sprWidth, sprHeight);
                mouseControls.pickupEntity(tile);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x +sprWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
