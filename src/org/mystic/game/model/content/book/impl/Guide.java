package org.mystic.game.model.content.book.impl;

import org.mystic.game.model.content.book.Book;
import org.mystic.game.model.content.book.Page;
import org.mystic.game.model.content.book.ReadingSession;
import org.mystic.game.model.entity.player.Player;

public final class Guide extends ReadingSession<Player> {

	public Guide(Player reader) {
		super(reader);
	}

	@Override
	public Book evaluate() {

		final Page first = new Page("@dre@Part 1: Getting started", "", "Before you jump straight into",
				"the action you might need", "some more cash.", "", "To make some starting gp you",
				"may thieve the stalls north", "of the edgeville bank.", "", "", "@dre@Part 2: The Market", "",
				"Now that you have money", "you'll need somewhere to", "spend it.", "", "You may teleport to the",
				"market by using the", "home teleportation spell and", "selecting the 'Market'",
				"teleportation option.");

		final Page second = new Page("@dre@Part 3: Trading", "", "As a regular player you", "may want to exchange your",
				"items with other players", "You may do this by either a", "simple trade offer with a player",
				"or by visiting the trading post", "located at the Grand Exchange.", "", "", "@dre@Part 4: Navigation",
				"", "There are many ways to", "navigate Mystic. The", "main method of navigation is",
				"via the Teleportation Wizard.", "The Wizard can be located", "north of Edgeville bank. Or",
				"even the at the-", "Grand Exchange.");

		final Book book = new Book("Guide Book", first);
		book.add(second);
		return book;
	}
}
