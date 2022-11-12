package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.Window;
import diamond2DGL.Entity;
import diamond2DGL.renderer.PickingTexture;
import diamond2DGL.Environment;
import diamond2DGL.listeners.KeyListener;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.renderer.DebugDraw;
import diamond2DGL.utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControls extends Component {

    Entity holdingEntity = null;
    private float debounceTime = 0.2f;
    private float debounce = debounceTime;
    private boolean multiSelect = false;
    private Vector2f multiSelectStart = new Vector2f();
    private Vector2f multiSelectEnd = new Vector2f();

    public void pickupEntity(Entity e) {
        if (this.holdingEntity != null) {
            this.holdingEntity.destroy();
        }
        this.holdingEntity = e;
        this.holdingEntity.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingEntity.addComponent(new NonPickable());
        Container.getGame().getCurrentEnvironment().addEntity(e);
    }

    public void place() {
        Entity newEntity = this.holdingEntity.copy();
        if (newEntity.getComponent(StateMachine.class) != null) {
            newEntity.getComponent(StateMachine.class).refreshTextures();
        }
        newEntity.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        newEntity.removeComponent(NonPickable.class);
        Container.getEnv().addEntity(newEntity);
    }

    @Override
    public void editorUpdate(float dT) {
        debounce -= dT;
        PickingTexture pickingTexture = Window.getPickingTexture();
        Environment env = Container.getEnv();

        if (holdingEntity != null) {
            float x = MouseListener.getWorldX();
            float y = MouseListener.getWorldY();
            holdingEntity.transform.position.x = ((int) Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
            holdingEntity.transform.position.y = ((int) Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                float halfWidth = Settings.GRID_WIDTH / 2.0f;
                float halfHeight = Settings.GRID_HEIGHT / 2.0f;
                if (MouseListener.isDragging() &&
                        !tileInCell(holdingEntity.transform.position.x - halfWidth,
                                holdingEntity.transform.position.y - halfHeight)) {
                    place();
                } else if (!MouseListener.isDragging() && !tileInCell(holdingEntity.transform.position.x - halfWidth,
                        holdingEntity.transform.position.y - halfHeight)) {
                    place();
                }
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE) || MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                holdingEntity.destroy();
                holdingEntity = null;
            }
        } else if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int entityId = pickingTexture.readPixel(x, y);
            Entity entity = env.getEntity(entityId);
            if (entity != null && entity.getComponent(NonPickable.class) == null) {
                Window.getImGuiLayer().getPropertiesWindow().setActiveEntity(entity);
            } else if (entity == null && !MouseListener.isDragging()) {
                Window.getImGuiLayer().getPropertiesWindow().clearSelected();
            }
            this.debounce = 0.2f;
        } else if (MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            if (!multiSelect) {
                Window.getImGuiLayer().getPropertiesWindow().clearSelected();
                multiSelectStart = MouseListener.getScreen();
                multiSelect = true;
            }
            multiSelectEnd = MouseListener.getScreen();
            Vector2f multiSelectStartWorld = MouseListener.screenToWorld(multiSelectStart);
            Vector2f multiSelectEndWorld = MouseListener.screenToWorld(multiSelectEnd);
            Vector2f halfSize = (new Vector2f(multiSelectEndWorld).sub(multiSelectStartWorld)).mul(0.5f);
            DebugDraw.addBox((new Vector2f(multiSelectStartWorld)).add(halfSize), new Vector2f(halfSize).mul(2.0f), 0.0f, new Vector3f(0, 1, 0));
        } else if (multiSelect) {
            multiSelect = false;
            int screenStartX = (int) multiSelectStart.x;
            int screenStartY = (int) multiSelectStart.y;
            int screenEndX = (int) multiSelectEnd.x;
            int screenEndY = (int) multiSelectEnd.y;
            multiSelectStart.zero();
            multiSelectEnd.zero();

            if (screenEndX < screenStartX) {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }
            if (screenEndY < screenStartY) {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            float[] entityIDs = pickingTexture.readPixels(new Vector2i(screenStartX, screenStartY), new Vector2i(screenEndX, screenEndY));
            Set<Integer> uniqueEntityIDs = new HashSet<>();
            for (float entityID : entityIDs) {
                uniqueEntityIDs.add((int) entityID);
            }

            for (Integer gameObjectId : uniqueEntityIDs) {
                Entity pickedEntity = env.getEntity(gameObjectId);
                if (pickedEntity != null && pickedEntity.getComponent(NonPickable.class) == null) {
                    Window.getImGuiLayer().getPropertiesWindow().addActiveEntity(pickedEntity);
                }
            }
        }
    }

    private boolean tileInCell(float x, float y) {
        Vector2f start = new Vector2f(x, y);
        Vector2f end = new Vector2f(start).add(new Vector2f(Settings.GRID_WIDTH, Settings.GRID_HEIGHT));
        Vector2f startScreenf = MouseListener.worldToScreen(start);
        Vector2f endScreenf = MouseListener.worldToScreen(end);
        Vector2i startScreen = new Vector2i((int) startScreenf.x + 2, (int) startScreenf.y + 2);
        Vector2i endScreen = new Vector2i((int) endScreenf.x - 2, (int) endScreenf.y - 2);
        float[] entitiesIDs = Window.getPickingTexture().readPixels(startScreen, endScreen);
        // System.out.println("IDsLength: " + entitiesIDs.length + "");

        for (int i = 0; i < entitiesIDs.length; i++) {
            if (entitiesIDs[i] >= 0) {
                Entity entity = Container.getEnv().getEntity((int) entitiesIDs[i]);
                if (entity.getComponent(NonPickable.class) == null) {
                    return true;
                }
            }
        }

        return false;
    }
}
