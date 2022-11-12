package diamond2DGL.engComponents;

import diamond2DGL.imgui.PropertiesWindow;
import diamond2DGL.listeners.MouseListener;
import diamond2DGL.renderer.Sprite;

public class TranslateGizmo extends Gizmo {

    // CONSTRUCTORS
    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void editorUpdate(float dT) {
        if (activeEntity != null) {
            if (xAxisActive && !yAxisActive) {
                activeEntity.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeEntity.transform.position.y -= MouseListener.getWorldDy();
            }
        }
        super.editorUpdate(dT);
    }
}
