package diamond2DGL.utils;

import diamond2DGL.renderer.Shader;
import diamond2DGL.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

    public static Shader getShader(String path) {
        File file = new File(path);
        if (ResourceManager.shaders.containsKey(file.getAbsolutePath())) {
            return ResourceManager.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(path);
            shader.compile();
            ResourceManager.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String path) {
        File file = new File(path);
        if (ResourceManager.textures.containsKey(file.getAbsolutePath())) {
            return ResourceManager.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(path);
            ResourceManager.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }
}
