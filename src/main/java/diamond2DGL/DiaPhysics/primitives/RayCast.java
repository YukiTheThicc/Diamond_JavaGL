package diamond2DGL.DiaPhysics.primitives;

import org.joml.Vector2f;

public class RayCast {

    // ATTRIBUTES
    private Vector2f point;
    private Vector2f normal;
    private float t;
    private boolean hit;

    // CONSTRUCTORS
    public RayCast (){
        this.point = new Vector2f();
        this.normal = new Vector2f();
        this.t = -1;
        this.hit = false;
    }

    // GETTERS & SETTERS


    // METHODS
    public void init(Vector2f point, Vector2f normal, float t, boolean hit) {
        this.point.set(point);
        this.normal.set(normal);
        this.t = t;
        this.hit = hit;
    }

    public static void reset(RayCast raycast) {
        if (raycast != null) {
            raycast.point.zero();
            raycast.normal.zero();
            raycast.t = -1;
            raycast.hit = false;
        }
    }
}
