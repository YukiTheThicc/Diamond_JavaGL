package Diamond2DGL;

public interface IGame {

	public abstract void start();
	public abstract void update(Container c, float dt);
	public abstract void render(Container c, Renderer r);
	
}
