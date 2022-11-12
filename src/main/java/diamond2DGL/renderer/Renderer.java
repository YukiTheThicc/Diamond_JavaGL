package diamond2DGL.renderer;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.engComponents.SpriteRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    // ATTRIBUTES
    private final int MAX_BATCH_SIZE = 1024;
    private List<RenderBatch> batches;
    private static Shader currentShader;

    // CONSTRUCTORS
    public Renderer() {
        this.batches = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public static Shader getBoundShader() {
        return currentShader;
    }

    // METHODS
    public static void bindShader(Shader shader) {
        currentShader = shader;
    }

    public void add(Entity e) {
        SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (!batch.isSpriteFull() && batch.getzIndex() == sprite.parent.transform.zIndex) {
                Texture texture = sprite.getTexture();
                if (texture == null || (!batch.isTextureFull() || batch.hasTexture(texture))) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE, sprite.parent.transform.zIndex, this);
            batch.start();
            batches.add(batch);
            batch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void destroyEntity(Entity e) {
        if (e.getComponent(SpriteRenderer.class) == null) return;
        for (RenderBatch batch : batches) {
            if (batch.destroyEntity(e)) {
                return;
            }
        }
    }

    public void render() {
        currentShader.use();
        for (int i = 0; i < batches.size(); i++) {
            batches.get(i).render(Container.getCamera());
        }
    }
}
