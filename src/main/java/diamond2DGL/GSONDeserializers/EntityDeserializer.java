package diamond2DGL.GSONDeserializers;

import com.google.gson.*;
import diamond2DGL.engComponents.Component;
import diamond2DGL.Entity;
import diamond2DGL.Transform;

import java.lang.reflect.Type;

public class EntityDeserializer implements JsonDeserializer<Entity> {
    @Override
    public Entity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

        Entity entity = new Entity(name, transform, zIndex);
        for (JsonElement e: components) {
            Component c = context.deserialize(e, Component.class);
            entity.addComponent(c);
        }

        return entity;
    }
}
