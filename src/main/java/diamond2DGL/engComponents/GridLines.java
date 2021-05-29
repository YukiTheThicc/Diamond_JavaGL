package diamond2DGL.engComponents;

import diamond2DGL.Container;
import diamond2DGL.renderer.DebugDraw;
import diamond2DGL.utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component {

    @Override
    public void update(float dt) {
        Vector2f cameraPos = Container.getCamera().pos;
        Vector2f projectionSize = Container.getCamera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int) (cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numHLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numVLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        int width = (int)projectionSize.x + Settings.GRID_WIDTH * 2;
        int height = (int)projectionSize.y + Settings.GRID_HEIGHT * 2;

        Vector3f color = new Vector3f(0, 1, 0);

        int maxLines = Math.max(numVLines, numHLines);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numHLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(x + width, y), color);
            }

            if (i < numVLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, y + height), color);
            }
        }
    }
}
