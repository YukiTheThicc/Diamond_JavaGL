package diamond2DGL.environments;

import diamond2DGL.*;
import diamond2DGL.engComponents.*;
import diamond2DGL.observers.EventSystem;
import diamond2DGL.observers.Observer;
import diamond2DGL.observers.events.Event;
import diamond2DGL.observers.handlers.EditorEventHandler;
import diamond2DGL.utils.AssetManager;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

public class EditorEnvFactory extends EnvironmentFactory{

    private SpriteSheet sprites;
    private SpriteSheet gizmos;
    private Entity editor;

    public EditorEnvFactory() {

    }

    @Override
    public void build(Environment env) {
        sprites = AssetManager.getSpriteSheet("assets/testing/decorationsAndBlocks.png");
        gizmos = AssetManager.getSpriteSheet("assets/testing/gizmos.png");

        editor = EntityFactory.createEntity("Editor");
        editor.setNotToSerialize();
        editor.addComponent(new MouseControls());
        editor.addComponent(new GridLines());
        editor.addComponent(new EditorCamera(env.getCamera()));
        editor.addComponent(new GizmoSystem(gizmos));
        env.addEntity(editor);
    }

    @Override
    public void loadResources(Environment env) {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.addSpriteSheet("assets/testing/decorationsAndBlocks.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetManager.addSpriteSheet("assets/testing/gizmos.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/gizmos.png"),
                        24, 48, 3, 0));
        for (Entity e : env.getEntities()) {
            if (e.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetManager.getTexture(spr.getTexture().getPath()));
                }
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Level Editor Stuff");
        editor.imgui();
        ImGui.end();

        ImGui.begin("Tiles");

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
                Entity tile = EntityFactory.createSpriteEntity(sprite, 32, 32);
                editor.getComponent(MouseControls.class).pickupEntity(tile);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + sprWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
