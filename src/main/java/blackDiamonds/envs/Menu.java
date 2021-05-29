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
    Entity testingStuff = new Entity("testingStuff", new Transform(new Vector2f()), 0);

    public Menu(String name) {
        super(name);
    }

    @Override
    public void init() {
        testingStuff.addComponent(new MouseControls());
        testingStuff.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(0, 0));
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
        testingStuff.update(dT);
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
            float sprWidth = sprite.getWidth() * 2;
            float sprHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, sprWidth, sprHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                Entity tile = EntityGenerator.generateTile(sprite, 32, 32);
                testingStuff.getComponent(MouseControls.class).pickupEntity(tile);
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
