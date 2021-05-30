package diamond2DGL.renderer;

import diamond2DGL.Camera;
import diamond2DGL.engComponents.SpriteRenderer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{

    /*
    ---DATA STORED PER VERTEX---
    ==================================================================================
    POS                 COLOR                               TEX_COORDS          TEX_ID
    float, float,       float, float, float, float,         float, float,       float
    ==================================================================================
    */
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    // Attribute offsets in BYTES
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXT_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEXT_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE + TEX_COORDS_SIZE + TEX_ID_SIZE + ENTITY_ID_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    // Batch attributes
    private List<Texture> textures;
    private SpriteRenderer[] sprites;
    private float[] vertices;
    private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private int numSprites;
    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;
    private boolean spriteFull;

    //  CONSTRUCTORS
    public RenderBatch(int maxBatchSize, int zIndex) {
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;

        // 4 vertices per quad
        this.vertices = new float[maxBatchSize * 4 * this.VERTEX_SIZE];

        this.numSprites = 0;
        this.spriteFull = true;
        this.textures = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public int getzIndex() {
        return zIndex;
    }

    //  PRIVATE METHODS
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
        Vector2f[] texCoords = sprite.getTexCoords();

        // Identify the texture to load into the vertex
        int texId = 0;
        if (sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i).equals(sprite.getTexture())) {
                    texId = i + 1;
                    break;
                }
            }
        }

        boolean isRotated = sprite.parent.transform.rotation != 0.0f;
        Matrix4f transfromMatrix = new Matrix4f().identity();
        if (isRotated) {
            transfromMatrix.translate(sprite.parent.transform.position.x, sprite.parent.transform.position.y, 0);
            transfromMatrix.rotate((float)Math.toRadians(sprite.parent.transform.rotation), 0, 0, 1);
            transfromMatrix.scale(sprite.parent.transform.scale.x, sprite.parent.transform.scale.y, 1);
        }

        // Add vertices with the appropiate properties (?) --REVISE
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {

            // Set the values of each vertex coords as its supposed to be
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            Vector4f currentPos = new Vector4f(sprite.parent.transform.position.x + (xAdd * sprite.parent.transform.scale.x),
                    sprite.parent.transform.position.y + (yAdd * sprite.parent.transform.scale.y), 0, 1);
            if (isRotated) {
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transfromMatrix);
            }

            // Load position
            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            //  Load Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load Texture Coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            // Load Texture ID
            vertices[offset + 8] = texId;

            // Load Entity ID
            vertices[offset + 9] = sprite.parent.getUid() + 1;

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
                3   2   ?
                0   1   ?

                3,2,0,0,2,1     7,6,4,4,6,5
        */
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;
        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    //  PUBLIC METHODS
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

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXT_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer spr) {
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if (spr.getTexture() != null) {
            if (!textures.contains(spr.getTexture())) {
                textures.add(spr.getTexture());
            }
        }

        //Add properties
        loadVertexProperties(index);
        if (numSprites >= this.maxBatchSize) {
            this.spriteFull = false;
        }
    }

    public boolean isSpriteFull() {
        return spriteFull;
    }

    public boolean isTextureFull() {
        return this.textures.size() > 8;
    }

    public boolean hasTexture(Texture texture) {
        return this.textures.contains(texture);
    }

    public void render(Camera camera) {
        boolean rebuffer = false;
        for (int i = 0; i < this.numSprites; i++) {
            SpriteRenderer spr = sprites[i];
            if (spr.hasChanged()) {
                loadVertexProperties(i);
                spr.wasChanged();
                rebuffer = true;
            }
        }
        if (rebuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        Shader shader = Renderer.getBoundShader();
        shader.use();
        shader.uploadMat4f("uProjection", camera.getpMatrix());
        shader.uploadMat4f("uView", camera.getvMatrix());
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for (int i = 0; i < textures.size(); i++) {
            textures.get(i).unbind();
        }
        shader.detach();
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.getzIndex());
    }
}
