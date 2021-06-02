package diamond2DGL.engComponents;

import diamond2DGL.editor.DiaImGui;
import org.joml.Vector2f;

public class Transform extends Component{

    public Vector2f position;
    public Vector2f scale;
    public float rotation;
    public int zIndex;

    // CONSTRUCTORS
    public Transform() {
        this.position = new Vector2f();
        this.scale = new Vector2f();
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position) {
        this.position = position;
        this.scale = new Vector2f();
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        this.rotation = 0.0f;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale, float rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.zIndex = 0;
    }

    public Transform(Vector2f position, Vector2f scale, float rotation, int zIndex) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.zIndex = zIndex;
    }

    // METHODS
    public Transform copy() {
        Transform t = new Transform(new Vector2f(this.position), new Vector2f(this.scale), this.rotation, this.zIndex);
        return t;
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
        to.rotation = this.rotation;
        to.zIndex = this.zIndex;
    }

    @Override
    public void imgui() {
        DiaImGui.drawVec2Control("Position", this.position);
        DiaImGui.drawVec2Control("Scale", this.scale, 32.0f);
        this.rotation = DiaImGui.dragFloat("Rotation", this.rotation);
        this.zIndex = DiaImGui.dragInt("ZIndex", this.zIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Transform)) return false;

        Transform t = (Transform) o;
        return t.position.equals(this.position) && t.scale.equals(this.scale) &&
                t.rotation == this.rotation && t.zIndex == this.zIndex;
    }
}
