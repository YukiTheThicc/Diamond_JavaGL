package diamond2DGL.utils;

import diamond2DGL.audio.Sound;
import diamond2DGL.engComponents.SpriteSheet;
import diamond2DGL.renderer.Shader;
import diamond2DGL.renderer.Texture;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, SpriteSheet> spriteSheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String path) {
        File file = new File(path);
        if (AssetManager.shaders.containsKey(file.getAbsolutePath())) {
            return AssetManager.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(path);
            shader.compile();
            AssetManager.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String path) {
        File file = new File(path);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(path);
            textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String path,  SpriteSheet spriteSheet) {
        File file = new File(path);
        if (!spriteSheets.containsKey(file.getAbsolutePath())) {
            spriteSheets.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet (String path) {
        File file = new File(path);
        if (!spriteSheets.containsKey(file.getAbsolutePath())) {
            assert false: "ERROR: Tried to access spriteSheet in '" + path + "', and it has not been added to the resource manager";
        }
        return spriteSheets.getOrDefault(file.getAbsolutePath(), null);
    }

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }

    public static Sound getSound(String path) {
        File file = new File(path);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            assert false: "Sound file not added'" + path + "'";
        }
        return null;
    }

    public static Sound addSound(String path, boolean loops) {
        File file = new File(path);
        if (sounds.containsKey(file.getAbsolutePath())) {
            return sounds.get(file.getAbsolutePath());
        } else {
            Sound sound = new Sound(file.getAbsolutePath(), loops);
            sounds.put(file.getAbsolutePath(), sound);
            return sound;
        }
    }
}
