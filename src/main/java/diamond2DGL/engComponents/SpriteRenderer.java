package diamond2DGL.engComponents;

import diamond2DGL.imgui.DiaImGui;
import diamond2DGL.renderer.Sprite;
import diamond2DGL.renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient Transform lastTransform;
    private transient boolean hasChanged = true;

    // CONSTRUCTORS
    public SpriteRenderer() {

    }

    // GETTERS & SETTERS
    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return this.sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return this.sprite.getTexCoords();
    }

    public boolean isChanged() {
        return this.hasChanged;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color = color;
            this.hasChanged = true;
        }
    }

    public void setTexture(Texture texture) {
        this.sprite.setTexture(texture);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.hasChanged = true;
    }

    public void hasChanged() {
        this.hasChanged = true;
    }

    public void wasChanged() {
        this.hasChanged = false;
    }

    //METHODS
    @Override
    public void start() {
        this.lastTransform = parent.transform.copy();
    }

    @Override
    public void imgui() {
        if (DiaImGui.colorPicker4("Color Picker", this.color)) {
            this.hasChanged = true;
        }
    }

    @Override
    public void editorUpdate(float dT) {
        if (!this.lastTransform.equals(this.parent.transform)) {
            this.parent.transform.copy(this.lastTransform);
            this.hasChanged = true;
        }
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.parent.transform)) {
            this.parent.transform.copy(this.lastTransform);
            this.hasChanged = true;
        }
    }
}
