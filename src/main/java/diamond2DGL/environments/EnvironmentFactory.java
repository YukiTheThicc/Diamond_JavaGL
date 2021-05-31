package diamond2DGL.environments;

public abstract class EnvironmentFactory {

    public abstract void build(Environment env);
    public abstract void loadResources(Environment env);
    public abstract void imgui();

}
