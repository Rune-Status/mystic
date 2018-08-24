package org.mystic.game.model.content.book;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.utility.TraversableList;

/**
 * 
 * @author Joshua Barry
 * 
 */
public class Book {

	/**
	 * The title of the book, displayed at the header of the interface.
	 */
	protected String title;

	/**
	 * A list of pages.
	 */
	protected TraversableList<Page> pages = new TraversableList<Page>();

	/**
	 * A readable book.
	 * 
	 * @param title
	 *            The title of the book
	 * @param pages
	 *            The page contents.
	 */
	public Book(String title, Page... pages) {
		this.title = title;
		for (Page page : pages) {
			this.pages.add(page);
		}
	}

	/**
	 * 
	 * @param player
	 *            The player reading the book.
	 */
	public void open(Player player) {
		Page page = pages.current();
		player.send(new SendInterface(12624));
		player.send(new SendString(title, 12666));
		update(player, page);
	}

	/**
	 * Updates the book's context.
	 * 
	 * @param player
	 *            The player who's interface need's an update.
	 * @param page
	 *            The new page to update the book with.
	 */
	public void update(Player player, Page page) {
		int childId = 12715;
		for (int i = 0; i < 22; i++) {
			if (i + 1 > page.length()) {
				player.send(new SendString("", childId));
			} else if (page.getContext()[i] == null) {
				player.send(new SendString("", childId));
			} else {
				player.send(new SendString(page.getContext()[i], childId));
			}
			childId += 1;
		}
	}

	/**
	 * Adds a new page to the book.
	 * 
	 * @param page
	 *            The page to add.
	 */
	public void add(Page page) {
		pages.add(page);
	}

	/**
	 * Adds a newly constructed page to the book.
	 * 
	 * @param strings
	 *            The page context.
	 */
	public void add(String... strings) {
		pages.add(new Page(strings));
	}

	/**
	 * Gets the mapping of pages.
	 * 
	 * @return pages
	 */
	public TraversableList<Page> getPages() {
		return pages;
	}

}
