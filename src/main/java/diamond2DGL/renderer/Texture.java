package diamond2DGL.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {

    private String path;
    private int ID;

    public Texture(String path) {
        this.path = path;
        this.ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);
        // Texture Parameters
        // Repeat texture on both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(path, x, y, channels, 0);

        if (image != null) {
            if (channels.get(0) == 4) {
                // RGBA
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, x.get(0), y.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 3) {
                // RGB
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, x.get(0), y.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "ERROR: (Texture) Unexpected numbre of channels " + channels.get(0);
            }
        } else {
            assert false : "ERROR: Failed to load texture: " + path;
        }
        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, ID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
