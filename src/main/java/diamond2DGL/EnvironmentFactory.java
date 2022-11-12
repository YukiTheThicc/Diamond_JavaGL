package diamond2DGL;

import diamond2DGL.Environment;

public abstract class EnvironmentFactory {

    public abstract void build(Environment env);
    public abstract void loadResources(Environment env);

    /**
     *
     */
    public abstract void imgui();

}
