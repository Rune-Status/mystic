package org.mystic.game.model.content;

import org.mystic.game.World;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendString;

public class WelcomeScreen {

	public static void send(Player player) {
		if (!player.inWilderness()) {
			player.send(new SendInterface(15244));
			player.send(new SendString("\\n There are currently @gre@" + World.getActivePlayers()
					+ " @yel@players playing! \\n \\n You have played for a total of 0 days.", 15259));
			player.send(new SendString("You have @gre@" + player.getVotePoints()
					+ " @yel@Vote Points. \\n \\nMake sure to vote at Runelocus \\nand Moparscape. \\n \\n www.mystic-ps.com/vote",
					15260));

			player.send(new SendString("   Welcome to Mystic", 15257));
			player.send(new SendString("   Please make sure to visit: www.Mystic-PS.com", 15258));

			player.send(new SendString("", 15261));
			if (player.getPlayer().isGoldMember()) {
				player.send(new SendString("\\n \\nYou are a Gold Member.", 15262));
			} else {
				player.send(new SendString(
						"\\n \\nYou are not a Gold Member. \\n Become one @ www.mystic-ps.com/store/", 15262));
			}
			player.send(new SendString("CLICK HERE TO PLAY", 15263));
			player.send(new SendString("Vote to help Mystic grow, every vote counts! \\nEvery vote counts.", 15265));
			player.send(new SendString("Purchase Gold Membership to gain Gold Member benefits.", 15266));
			if (player.getBankPinAttributes().hasBankPin()) {
				player.send(new SendString("\\n@gre@Your bank is PIN protected.", 15270));
			} else {
				player.send(new SendString("\\nYou do have a Bank PIN set.", 15270));
			}
		}
	}

}
