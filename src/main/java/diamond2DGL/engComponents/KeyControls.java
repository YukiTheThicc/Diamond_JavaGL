package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.editor.PropertiesWindow;
import diamond2DGL.listeners.KeyListener;
import diamond2DGL.Window;
import diamond2DGL.utils.Settings;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component {
    @Override
    public void editorUpdate(float dt) {
        PropertiesWindow propertiesWindow = Window.getImGuiLayer().getPropertiesWindow();
        Entity activeGameObject = propertiesWindow.getActiveEntity();
        List<Entity> activeEntities = propertiesWindow.getActiveEntities();

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) && activeGameObject != null) {
            Entity entity = activeGameObject.copy();
            Container.getEnv().addEntity(entity);
            entity.transform.position.add(Settings.GRID_WIDTH, 0.0f);
            propertiesWindow.setActiveEntity(entity);
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) && activeEntities.size() > 1) {
            List<Entity> entities = new ArrayList<>(activeEntities);
            propertiesWindow.clearSelected();
            for (Entity e : entities) {
                Entity copy = e.copy();
                Container.getEnv().addEntity(copy);
                propertiesWindow.addActiveEntity(copy);
            }
        } else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {
            for (Entity e : activeEntities) {
                e.destroy();
            }
            propertiesWindow.clearSelected();
        }
    }
}
