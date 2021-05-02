package diamond2DGL.engComponents;

import diamond2DGL.Camera;
import diamond2DGL.renderer.Shader;
import diamond2DGL.utils.ResourceManager;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasSpace;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = ResourceManager.getShader("src/main/resources/shaders/default.glsl");
        this.shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices per quad
        this.vertices = new float[maxBatchSize * 4 * this.VERTEX_SIZE];

        this.numSprites = 0;
        this.hasSpace = true;
    }

    private int[] generateIndices() {
        // 6 indices per rect (3 per triangle)
        int[] elements = new int[maxBatchSize * 6];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        //Find offset
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        /*
         *     (0,0)   (1,0)
         *       x       x
         *     (0,1)   (1,1)
         *       x       x
         *  Add vertices with the appropriate properties
         */

        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 0.0f;
            }
            vertices[offset] = sprite.parent.transform.pos.x + (xAdd * sprite.parent.transform.scale.x);
            vertices[offset + 1] = sprite.parent.transform.pos.y + (yAdd * sprite.parent.transform.scale.y);

            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;
            offset += VERTEX_SIZE;
        }
    }

    /**
     * Loads a rect vertex indices in proper order onto the element array (ebo)
     *
     * @param elements:
     * @param index:
     */
    private void loadElementIndices(int[] elements, int index) {
        // 6 for the total size of the rect
        // 4 for the vertex index value
        /*
                0   1   ?
                3   2   ?

                3,2,0,0,2,1     7,6,4,4,6,5
        */
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;
        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public void start() {
        // Generate and bind Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // ALLOCATE space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer spr) {
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        //Add properties
        loadVertexProperties(index);
        if (numSprites >= this.maxBatchSize) {
            this.hasSpace = false;
        }
    }

    public boolean hasSpace() {
        return hasSpace;
    }

    public void render(Camera camera) {
        // For now we will rebuffer all data every frame (to be changed)
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        shader.use();
        shader.uploadMat4f("uProjection", camera.getpMatrix());
        shader.uploadMat4f("uView", camera.getvMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }
}
