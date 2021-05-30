package diamond2DGL.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private String path;
    private transient int ID;
    private int width;
    private int height;

    // CONSTRUCTORS
    public Texture() {
        ID = -1;
        width = -1;
        height = -1;
        path = "";
    }

    public Texture(int width, int height) {
        this.path = "GENERATED";
        this.ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

    }

    // GETTERS & SETTERS
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getID() {
        return ID;
    }

    public String getPath() {
        return path;
    }

    // METHODS
    public void init(String path) {
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
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer image = stbi_load(path, x, y, channels, 0);

        if (image != null) {
            this.width = x.get(0);
            this.height = y.get(0);
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Texture)) return false;
        Texture tex = (Texture)o;
        return tex.getWidth() == this.width && tex.getHeight() == this.height && tex.getID() == this.ID &&
                tex.getPath().equals(this.path);
    }
}
