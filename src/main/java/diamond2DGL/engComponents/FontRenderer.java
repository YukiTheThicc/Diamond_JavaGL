package diamond2DGL.engComponents;

import diamond2DGL.Component;
import diamond2DGL.Entity;

import java.sql.SQLOutput;

public class FontRenderer extends Component {

    public FontRenderer() {

    }

    @Override
    public void start() {
        if (parent.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found SpriteRenderer in FontRenderer");
        }
    }

    @Override
    public void update(float dT) {

    }

}
