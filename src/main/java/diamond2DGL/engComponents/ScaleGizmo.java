package diamond2DGL.engComponents;

import diamond2DGL.editor.PropertiesWindow;
import diamond2DGL.listeners.MouseListener;

public class ScaleGizmo extends Gizmo{

    // CONSTRUCTORS
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    // GETTERS & SETTERS

    // METHODS
    @Override
    public void editorUpdate(float dT) {
        if (activeEntity != null) {
            if (xAxisActive && !yAxisActive) {
                activeEntity.transform.scale.x -= MouseListener.getWorldDx();
            } else if (yAxisActive) {
                activeEntity.transform.scale.y -= MouseListener.getWorldDy();
            }
        }
        super.editorUpdate(dT);
    }
}
