package org.mystic.game.model.entity;

public final class Graphic {

	/**
	 * Creates a new high graphic
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @return
	 */
	public static Graphic highGraphic(int id, int delay) {
		return new Graphic(id, delay, 100);
	}

	/**
	 * Creates a new low graphic
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @return
	 */
	public static Graphic lowGraphic(int id, int delay) {
		return new Graphic(id, delay, 0);
	}

	/**
	 * The id of the graphic
	 */
	private final int id;

	/**
	 * The height of the graphic
	 */
	private final int height;

	/**
	 * The delay of the graphic
	 */
	private final int delay;

	/**
	 * Constructs a new graphic based on another graphic
	 * 
	 * @param other
	 *            The other graphic
	 */
	public Graphic(Graphic other) {
		this.id = other.id;
		this.delay = other.delay;
		this.height = other.height;
	}

	/**
	 * Constructs a new graphic with a an id
	 * 
	 * @param id
	 *            The id of the graphic
	 */
	public Graphic(int id) {
		this(id, 0, false);
	}

	/**
	 * Constructs a new graphic with an id and delay, should the delay be high?
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param high
	 *            Should the graphic be high?
	 */
	public Graphic(int id, int delay, boolean high) {
		this.id = id;
		this.delay = delay;
		if (high) {
			this.height = 100;
		} else {
			this.height = 0;
		}
	}

	/**
	 * Constructs a new graphic with an id, delay, and height
	 * 
	 * @param id
	 *            The id of the graphic
	 * @param delay
	 *            The delay of the graphic
	 * @param height
	 *            The height of the graphic
	 */
	public Graphic(int id, int delay, int height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
	}

	/**
	 * Gets the delay of the graphic
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the height of the graphic
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the id of the graphic
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the delay and height in one variable to send to the client
	 * 
	 * @return
	 */
	public int getValue2() {
		return delay | height << 16;
	}

}