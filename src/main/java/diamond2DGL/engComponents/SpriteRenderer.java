package diamond2DGL.engComponents;

import diamond2DGL.Component;

import org.joml.Vector4f;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void start() {
    }

    @Override
    public void update(float dT) {

    }

    public Vector4f getColor() {
        return this.color;
    }
}
