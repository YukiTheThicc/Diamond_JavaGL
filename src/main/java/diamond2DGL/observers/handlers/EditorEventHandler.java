package diamond2DGL.observers.handlers;

import diamond2DGL.Container;
import diamond2DGL.Display;
import diamond2DGL.Entity;
import diamond2DGL.environments.EditorEnvFactory;
import diamond2DGL.observers.Observer;
import diamond2DGL.observers.events.Event;

public class EditorEventHandler implements Observer {
    @Override
    public void onNotify(Entity entity, Event event) {
        switch (event.type) {
            case Play:
                Container.playEditor();
                Container.getEnv().save();
                Container.getGame().selectEnvironment(new EditorEnvFactory());
                break;
            case Stop:
                Container.stopEditor();
                Container.getGame().selectEnvironment(new EditorEnvFactory());
                break;
            case SaveLevel:
                Container.getEnv().save();
                break;
            case LoadLevel:
                Display.getImGuiLayer().getPropertiesWindow().setActiveEntity(null);
                Container.getGame().selectEnvironment(new EditorEnvFactory());
                break;
        }
    }
}
