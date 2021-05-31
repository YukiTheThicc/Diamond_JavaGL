package diamond2DGL.observers;

import diamond2DGL.Entity;
import diamond2DGL.observers.events.Event;

public interface Observer {
    void onNotify(Entity entity, Event event);
}
