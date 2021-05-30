package diamond2DGL;

import diamond2DGL.engComponents.Sprite;
import diamond2DGL.engComponents.SpriteRenderer;
import org.joml.Vector2f;

public class EntityFactory {

    public static Entity createSpriteEntity(Sprite sprite, int sizeX, int sizeY) {
        Entity tile = new Entity("Sprite_Entity", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        tile.addComponent(renderer);
        return tile;
    }
}
