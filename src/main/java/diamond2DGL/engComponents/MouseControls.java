package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.utils.Settings;

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
            holdingEntity.transform.position.x = MouseListener.getOrthoX();
            holdingEntity.transform.position.y = MouseListener.getOrthoY();
            holdingEntity.transform.position.x = (int)(holdingEntity.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingEntity.transform.position.y = (int)(holdingEntity.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
