package diamond2DGL.utils;

import com.google.gson.*;
import diamond2DGL.Entity;
import diamond2DGL.GSONDeserializers.ComponentDeserializer;
import diamond2DGL.GSONDeserializers.EntityDeserializer;
import diamond2DGL.engComponents.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings implements JsonSerializer<Component>, JsonDeserializer<Component> {

    public static String projectDirectory;
    public static float GRID_WIDTH = 0.25f;
    public static float GRID_HEIGHT = 0.25f;

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter("diaSettings.json");

           // writer.write(gson.toJson(toSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(Entity.class, new EntityDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("config.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {

        }
    }

    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}
