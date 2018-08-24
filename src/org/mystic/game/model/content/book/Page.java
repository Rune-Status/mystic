package org.mystic.game.model.content.book;

/**
 * Represents the page in a book.
 * 
 * @author Joshua Barry
 * 
 */
public class Page {

	private String[] context;

	/**
	 * 
	 * @param number
	 * @param context
	 */
	public Page(String... context) {
		this.context = context;
	}

	/**
	 * The page context.
	 * 
	 * @return
	 */
	public String[] getContext() {
		return context;
	}

	/**
	 * Convenient access to the page length.
	 * 
	 * @return
	 */
	public int length() {
		return context.length;
	}

}
