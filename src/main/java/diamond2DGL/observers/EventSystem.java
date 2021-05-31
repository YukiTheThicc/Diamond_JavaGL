package diamond2DGL.observers;

import diamond2DGL.Entity;
import diamond2DGL.observers.events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem {

    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notify(Entity entity, Event event) {
        for (Observer observer : observers) {
            observer.onNotify(entity, event);
        }
    }
}
