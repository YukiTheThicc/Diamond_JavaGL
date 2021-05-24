package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.Display;
import diamond2DGL.Entity;
import diamond2DGL.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{

    Entity holdingEntity = null;

    public void pickupEntity(Entity e) {
        this.holdingEntity = e;
        Container.get().getGame().getCurrentEnvironment().addEntity(e);
    }

    public void place() {
        holdingEntity = null;
    }

    @Override
    public void update(float dT) {
        if (holdingEntity != null) {
            holdingEntity.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingEntity.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
