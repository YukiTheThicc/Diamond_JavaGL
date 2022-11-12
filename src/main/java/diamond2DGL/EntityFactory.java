package diamond2DGL;

import diamond2DGL.engComponents.*;
import diamond2DGL.physics.components.BoxCollider;
import diamond2DGL.physics.components.RigidBody;
import diamond2DGL.physics.enums.BodyType;
import diamond2DGL.renderer.Sprite;
import diamond2DGL.utils.AssetManager;
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

    public static Entity createTile(Sprite sprite, float sizeX, float sizeY) {
        Entity tile = createSpriteEntity(sprite, sizeX, sizeY);
        RigidBody rb = new RigidBody();
        rb.setBodyType(BodyType.Static);
        tile.addComponent(rb);
        BoxCollider bc = new BoxCollider();
        bc.setHalfSize(new Vector2f(sizeX, sizeY));
        tile.addComponent(bc);
        return tile;
    }

    public static Entity createPlayer() {
        SpriteSheet sprites = AssetManager.getSpriteSheet("assets/testing/spritesheet.png");
        Entity player = createSpriteEntity(sprites.getSprite(0), 0.25f, 0.25f);

        AnimationState run = new AnimationState();
        run.title = "Running";
        float defaultFrameTime = 0.25f;
        run.addFrame(sprites.getSprite(0), defaultFrameTime);
        run.addFrame(sprites.getSprite(2), defaultFrameTime);
        run.addFrame(sprites.getSprite(3), defaultFrameTime);
        run.addFrame(sprites.getSprite(2), defaultFrameTime);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        player.addComponent(stateMachine);
        return player;
    }
}
