package diamond2DGL;

public abstract class Component {

    public Entity parent = null;

    public void start() {

    }

    public abstract void update(float dT);
}
