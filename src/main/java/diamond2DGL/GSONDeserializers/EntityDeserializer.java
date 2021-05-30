package diamond2DGL.GSONDeserializers;

import com.google.gson.*;
import diamond2DGL.engComponents.Component;
import diamond2DGL.Entity;
import diamond2DGL.engComponents.Transform;

import java.lang.reflect.Type;

public class EntityDeserializer implements JsonDeserializer<Entity> {
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");

        Entity entity = new Entity(name);
        for (JsonElement e: components) {
            Component c = context.deserialize(e, Component.class);
            entity.addComponent(c);
        }
        entity.transform = entity.getComponent(Transform.class);

        return entity;
    }
}
