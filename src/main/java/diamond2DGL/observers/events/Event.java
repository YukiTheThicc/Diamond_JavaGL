package diamond2DGL.observers.events;

public class Event {

    // ATTRIBUTES
    public EventType type;

    // CONSTRUCTORS
    public Event(EventType type) {
        this.type = type;
    }

    public Event() {
        this.type = EventType.UserEvent;
    }

    // GETTERS & SETTERS

    //METHODS
}
