package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.Entity;
import diamond2DGL.EntityFactory;
import diamond2DGL.editor.PropertiesWindow;
import diamond2DGL.listeners.MouseListener;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class Gizmo extends Component{

    // ATTRIBUTES
    private Vector4f xAxisColor = new Vector4f(1f,0.3f,0.3f,1f);
    private Vector4f xAxisColorHover = new Vector4f(0.33f,0,0,1);
    private Vector4f yAxisColor = new Vector4f(0.3f,1f,0.3f,1);
    private Vector4f yAxisColorHover = new Vector4f(0,0.33f,0,1);
    private Entity xAxisObject;
    private Entity yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    private PropertiesWindow propertiesWindow;
    private Vector2f xAxisOffset = new Vector2f(55f, -4f);
    private Vector2f yAxisOffset = new Vector2f(12f, 55f);
    private int width = 16;
    private int height = 48;
    private boolean using = false;
    protected Entity activeEntity = null;
    protected boolean xAxisActive;
    protected boolean yAxisActive;

    // CONSTRUCTORS
    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = EntityFactory.createSpriteEntity(arrowSprite, 16, 48);
        this.yAxisObject = EntityFactory.createSpriteEntity(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());
        Container.getEnv().addEntity(this.xAxisObject);
        Container.getEnv().addEntity(this.yAxisObject);
    }

    // GETTERS & SETTERS
    public void setUsing() {
        this.using = true;
    }

    public void setNotUsing() {
        this.using = false;
        this.setInactive();
    }

    // METHODS
    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.setNotToSerialize();
        this.yAxisObject.setNotToSerialize();
        this.xAxisObject.transform.zIndex = 100;
        this.yAxisObject.transform.zIndex = 100;
    }

    @Override
    public void update(float dT) {
        if (using) {
            setInactive();
        }
    }

    @Override
    public void editorUpdate(float dT) {
        if (!using) return;

        this.activeEntity = this.propertiesWindow.getActiveEntity();
        if (this.activeEntity != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = false;
            yAxisActive = true;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (this.activeEntity != null) {
            this.xAxisObject.transform.position.set(this.activeEntity.transform.position);
            this.yAxisObject.transform.position.set(this.activeEntity.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeEntity = null;
        this.xAxisSprite.setColor(new Vector4f(0,0,0,0));
        this.yAxisSprite.setColor(new Vector4f(0,0,0,0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= xAxisObject.transform.position.x &&
                mousePos.x >= xAxisObject.transform.position.x - height &&
                mousePos.y >= xAxisObject.transform.position.y &&
                mousePos.y <= xAxisObject.transform.position.y + width ) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }
        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= yAxisObject.transform.position.x &&
                mousePos.x >= yAxisObject.transform.position.x - width &&
                mousePos.y <= yAxisObject.transform.position.y &&
                mousePos.y >= yAxisObject.transform.position.y - height ) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }
        yAxisSprite.setColor(yAxisColor);
        return false;
    }
}
