package org.mystic.game.model.content.book;

/**
 * 
 * @author Joshua Barry
 * 
 */
public abstract class ReadingSession<T> {

	/**
	 * The main reader.
	 */
	private T reader;

	/**
	 * 
	 * @param player
	 *            The palyer who will read the book.
	 */
	public ReadingSession(T reader) {
		this.reader = reader;
	}

	/**
	 * Set up a new book.
	 * 
	 * @return
	 */
	public abstract Book evaluate();

	public T getReader() {
		return reader;
	}

}
