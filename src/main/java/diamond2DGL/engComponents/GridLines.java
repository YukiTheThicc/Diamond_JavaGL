package diamond2DGL.engComponents;

import diamond2DGL.Camera;
import diamond2DGL.Container;
import diamond2DGL.renderer.DebugDraw;
import diamond2DGL.utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component {

    @Override
    public void editorUpdate(float dt) {
        Camera camera = Container.getCamera();
        Vector2f cameraPos = camera.pos;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        float firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numHLines = (int)((projectionSize.y * camera.getZoom()) / Settings.GRID_HEIGHT) + 2;
        int numVLines = (int)((projectionSize.x * camera.getZoom()) / Settings.GRID_WIDTH) + 2;

        float width = (int)(projectionSize.x * camera.getZoom())+ Settings.GRID_WIDTH * 2;
        float height = (int)(projectionSize.y * camera.getZoom())+ Settings.GRID_HEIGHT * 2;

        Vector3f color = new Vector3f(0.66f, 0.66f, 0.66f);

        int maxLines = Math.max(numVLines, numHLines);

        float x = 0;
        float y = 0;
        int i = 0;
        for (i = 0; i < maxLines; i++) {
            x = firstX + (Settings.GRID_WIDTH * i);
            y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numHLines) {
                DebugDraw.addLine(new Vector2f(firstX, y), new Vector2f(width, y), color);
            }

            if (i < numVLines) {
                DebugDraw.addLine(new Vector2f(x, firstY), new Vector2f(x, height), color);
            }
        }
        //System.out.println("firstX: " + firstX + ", firstY: " + firstY);
    }
}
