package diamond2DGL.physics;

import diamond2DGL.Entity;
import diamond2DGL.engComponents.Component;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class DiaContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Entity entityA = (Entity) contact.getFixtureA().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : entityA.getComponents()) {
            c.beginCollision(entityB, contact, aNormal);
        }
        for (Component c : entityB.getComponents()) {
            c.beginCollision(entityA, contact, bNormal);
        }
    }

    @Override
    public void endContact(Contact contact) {
        Entity entityA = (Entity) contact.getFixtureA().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : entityA.getComponents()) {
            c.endCollision(entityB, contact, aNormal);
        }
        for (Component c : entityB.getComponents()) {
            c.endCollision(entityA, contact, bNormal);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        Entity entityA = (Entity) contact.getFixtureA().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : entityA.getComponents()) {
            c.preSolve(entityB, contact, aNormal);
        }
        for (Component c : entityB.getComponents()) {
            c.preSolve(entityA, contact, bNormal);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        Entity entityA = (Entity) contact.getFixtureA().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getUserData();
        WorldManifold worldManifold = new WorldManifold();
        contact.getWorldManifold(worldManifold);
        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : entityA.getComponents()) {
            c.postSolve(entityB, contact, aNormal);
        }
        for (Component c : entityB.getComponents()) {
            c.postSolve(entityA, contact, bNormal);
        }
    }
}
