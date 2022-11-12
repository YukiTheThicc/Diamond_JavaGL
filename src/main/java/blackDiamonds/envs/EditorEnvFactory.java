package blackDiamonds.envs;

import diamond2DGL.*;
import diamond2DGL.audio.Sound;
import diamond2DGL.engComponents.*;
import diamond2DGL.Environment;
import diamond2DGL.EnvironmentFactory;
import diamond2DGL.renderer.Sprite;
import diamond2DGL.utils.AssetManager;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.io.File;
import java.util.Collection;

public class EditorEnvFactory extends EnvironmentFactory {

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
        editor.addComponent(new KeyControls());
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
        AssetManager.addSpriteSheet("assets/testing/items.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/items.png"),
                        16, 43, 26, 0));
        AssetManager.addSpriteSheet("assets/testing/spritesheet.png",
                new SpriteSheet(AssetManager.getTexture("assets/testing/spritesheet.png"),
                        16, 16, 26, 0));

        AssetManager.addSound("assets/sfx/testing/main-theme-overworld.ogg", true);
        AssetManager.addSound("assets/sfx/testing/flagpole.ogg", false);
        AssetManager.addSound("assets/sfx/testing/break_block.ogg", false);
        AssetManager.addSound("assets/sfx/testing/bump.ogg", false);
        AssetManager.addSound("assets/sfx/testing/coin.ogg", false);
        AssetManager.addSound("assets/sfx/testing/gameover.ogg", false);
        AssetManager.addSound("assets/sfx/testing/jump-small.ogg", false);
        AssetManager.addSound("assets/sfx/testing/mario_die.ogg", false);
        AssetManager.addSound("assets/sfx/testing/pipe.ogg", false);
        AssetManager.addSound("assets/sfx/testing/powerup.ogg", false);
        AssetManager.addSound("assets/sfx/testing/powerup_appears.ogg", false);
        AssetManager.addSound("assets/sfx/testing/stage_clear.ogg", false);
        AssetManager.addSound("assets/sfx/testing/stomp.ogg", false);
        AssetManager.addSound("assets/sfx/testing/kick.ogg", false);
        AssetManager.addSound("assets/sfx/testing/invincible.ogg", false);

        for (Entity e : env.getEntities()) {
            if (e.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetManager.getTexture(spr.getTexture().getPath()));
                }
            }
            if (e.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = e.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imgui() {
        ImGui.begin("Level Editor");
        editor.imgui();
        ImGui.end();

        ImGui.begin("Tiles");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Tiles")) {
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
                        Entity tile = EntityFactory.createTile(sprite, 0.25f, 0.25f);
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
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Prefabs")) {
                SpriteSheet playerSprites = AssetManager.getSpriteSheet("assets/testing/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float sprWidth = sprite.getWidth() * 2;
                float sprHeight = sprite.getHeight() * 2;
                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                if (ImGui.imageButton(id, sprWidth, sprHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    Entity tile = EntityFactory.createPlayer();
                    editor.getComponent(MouseControls.class).pickupEntity(tile);
                }
                ImGui.sameLine();
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sounds")) {
                Collection<Sound> sounds = AssetManager.getAllSounds();
                for (Sound sound : sounds) {
                    File tmp = new File(sound.getPath());
                    if (ImGui.button(tmp.getName())) {
                        if (!sound.isPlaying()) {
                            sound.play();
                        } else {
                            sound.stop();
                        }
                    }
                    if (ImGui.getContentRegionAvailX() > 500) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }

            ImGui.endTabBar();
        }
        ImGui.end();
    }
}
