package diamond2DGL.renderer;

import diamond2DGL.Camera;
import diamond2DGL.Entity;
import diamond2DGL.engComponents.RenderBatch;
import diamond2DGL.engComponents.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(Entity e) {
        SpriteRenderer spr = e.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer spr) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasSpace()) {
                batch.addSprite(spr);
                added = true;
                break;
            }
        }
        if (!added) {
            RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE);
            batch.start();
            batches.add(batch);
            batch.addSprite(spr);
        }
    }

    public void render(Camera camera) {
        for(RenderBatch batch : batches) {
            batch.render(camera);
        }
    }
}
