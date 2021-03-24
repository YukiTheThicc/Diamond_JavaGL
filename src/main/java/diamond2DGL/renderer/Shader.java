package diamond2DGL.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String path;

   /*
   * TODO -> Add new constructor for loading vertex and fragment shaders from two
   * */

    public Shader(String filePath) {
        this.path = filePath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] sources = source.split("(#type)( ) + ([a-zA-Z])+");

            // Find the first pattern after #type
            int index = source.indexOf("#type" + 6);
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();
            // Find the second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = sources[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = sources[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = sources[1];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = sources[1];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Failed shader file opening for: '" + filePath + "'";
        }
    }

    public void compile() {

    }

    public void use() {

    }

    public void detach() {

    }
}
