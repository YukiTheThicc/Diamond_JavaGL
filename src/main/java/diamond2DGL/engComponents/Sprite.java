package diamond2DGL.engComponents;

import diamond2DGL.renderer.Texture;
import org.joml.Vector2f;

public class Sprite {

    // ATTRIBUTES
    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    // CONSTRUCTORS
    public Sprite() {

    }

//    public Sprite(Texture texture) {
//        this.texture = texture;
//        Vector2f[] texCoords = {
//                new Vector2f(1, 1),
//                new Vector2f(1, 0),
//                new Vector2f(0, 0),
//                new Vector2f(0, 1)
//        };
//        this.texCoords = texCoords;
//    }
//
//    public Sprite(Texture texture, Vector2f[] texCoords) {
//        this.texture = texture;
//        this.texCoords = texCoords;
//    }

    // GETTERS & SETTERS
    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }
}
