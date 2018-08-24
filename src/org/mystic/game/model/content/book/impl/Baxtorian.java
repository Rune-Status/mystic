package org.mystic.game.model.content.book.impl;

import org.mystic.game.model.content.book.Book;
import org.mystic.game.model.content.book.Page;
import org.mystic.game.model.content.book.ReadingSession;
import org.mystic.game.model.entity.player.Player;

/**
 * A book on baxtorian.
 * 
 * @author Joshua Barry
 * 
 */
public class Baxtorian extends ReadingSession<Player> {

	public Baxtorian(Player reader) {
		super(reader);
	}

	@Override
	public Book evaluate() {
		Page firstPage = new Page("The missing relics.", "", "Many artifacts of", "elven history were lost",
				"after the fourth age. The", "greatest loss to our", "collections of elf history",
				"were the hidden tratures", "of Baxtorian.\\n    Some believe these", "", "treasures are still",
				"unclaimed, but it is more", "commonly believed that", "dward miners recovered", "the treasure at the",
				"beginning of the third", "age. Another great loss", "was Glarial's pebble, a key",
				"which allowed her family", "to visit her her tomb.\\n    The stone was taken", "",
				"by a gnome family over a");

		Page secondPage = new Page("century ago. It is", "believed that the gnomes'", "descendent Golrie still has",
				"the stone hidden in the", "caves under the gnome", "tree village.", "", "The sonnet of Baxtorian", "",
				"The love between", "Baxtorian and Glarial was", "said to have lasted over a", "century. They lived a",
				"peaceful life learning and", "teaching the laws of", "nature. When Baxtorian's",
				"kingdom was invaded by", "the dark forces he left on", "a five year campaign. He",
				"returned to find his", "people slaughtered and his", "wife taken by the enemy.");

		Page thirdPage = new Page("    After years of", "searching for his love he", "finally gave up and",
				"returned to the home he", "made for Glarial under", "the Baxtorian Waterfall.", "Once he entered he",
				"never returned. Only", "Glarial had the power to", "also enter the waterfall.", "    Since Baxtorian",
				"entered no one but her", "can follow him in, it's as if", "the powers of nature still",
				"work to protect him.", "", "The power of nature", "", "    Glarial and Baxtorian",
				"were masters of nature.", "Trees would grow, hills", "form and rivers flood on");

		Page fourthPage = new Page("their command. Baxtorian", "in particular had", "perfected rune lore. It",
				"was said that he could", "use the stones to control", "water, earth and air.", "", "Ode to eternity",
				"", "A short piece written by", "Baxtorian himself.", "", "What care I for this", "mortal coil,",
				"where treasures are yet", "so frail,", "for it is you that is my", "life blood,",
				"the wine to my holy grail", "and if i see the", "judgement day,", "when the gods fill the air");

		Page lastPage = new Page("with dust,", "I'll happily choke on your", "memory,", "as my kingdom turns to",
				"rust");

		Book book = new Book("Book on Baxtorian", firstPage);
		book.add(secondPage);
		book.add(thirdPage);
		book.add(fourthPage);
		book.add(lastPage);
		return book;
	}

}
