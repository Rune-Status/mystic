package org.mystic.game.model.content;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendString;

public class MemberInformation {

	public static final int INTERFACE_ID = 8134;

	public static void clear(Player player) {
		for (int i = 8145; i < 8247; i++) {
			player.send(new SendString("", i));
		}
	}

	public static void display(Player player) {
		clear(player);
		player.send(new SendInterface(INTERFACE_ID));
		player.send(new SendString("@dre@Membership Perks - www.Mystic-PS.com/store/", 8144));
		player.send(new SendString(strings[0], 8147));
		player.send(new SendString(strings[1], 8148));
	}

	public static final String[] strings = { "@or1@Regular Gold Member",
			"Familiar special attacks do not require scrolls." };

}
