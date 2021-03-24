package diamond2DGL;

public abstract class Game {

	/** Abstract class Game
	 *
	 * This class as instanced will represent the game itself. It contains the game container, which will be the same
	 * for any game ran in the engine; it (Container class) keeps track of the delta time, and keeps running the game
	 * loop until the function stop() is called. the container will also cover the opening of the window, closing and
	 * memory liberation, leaving only the game mechanics and rendering left for the developer.
	 *
	 * This class is NOT a singleton. Execute Game.start() to run the game.
	 *
	 * selectEnvironment is for changing the environment of the game.
	 *
	 * update holds all the actions to do on game update, depending on the delta time
	 *
	 * render will render the game using the environments own renderer
	 */
	private final Container container;
	private Environment currentEnvironment;

	public Game () {
		this.container = new Container(this);
		this.selectEnvironment(0);
	}

	public Environment getCurrentEnvironment() {
		return currentEnvironment;
	}

	public void setCurrentEnvironment(Environment currentEnvironment) {
		this.currentEnvironment = currentEnvironment;
	}

	public void start() {
		this.container.run();
	}

	public abstract void selectEnvironment(int envCode);

	public void update(float dT) {
		this.currentEnvironment.update(dT);
	}

	public void render() {
		this.currentEnvironment.render();
	}
}