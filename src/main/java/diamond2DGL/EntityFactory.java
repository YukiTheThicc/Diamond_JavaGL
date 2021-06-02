package diamond2DGL;

import diamond2DGL.engComponents.Sprite;
import diamond2DGL.engComponents.SpriteRenderer;
import diamond2DGL.engComponents.Transform;
import org.joml.Vector2f;

public class EntityFactory {

    public static Entity createEntity(String name) {
        Entity e = new Entity(name);
        e.addComponent(new Transform());
        e.transform = e.getComponent(Transform.class);
        return e;
    }

    public static Entity createSpriteEntity(Sprite sprite, float sizeX, float sizeY) {
        Entity entity = createEntity("Sprite_Entity");
        entity.transform.scale.x = sizeX;
        entity.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        entity.addComponent(renderer);
        return entity;
    }
}
