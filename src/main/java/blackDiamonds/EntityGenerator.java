package blackDiamonds;

import diamond2DGL.Entity;
import diamond2DGL.Transform;
import diamond2DGL.engComponents.Sprite;
import diamond2DGL.engComponents.SpriteRenderer;
import org.joml.Vector2f;

public class EntityGenerator {

    public static Entity generateTile(Sprite sprite, float sizeX, float sizeY) {
        Entity tile = new Entity("Generated_Tile", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        tile.addComponent(renderer);
        return tile;
    }
}
