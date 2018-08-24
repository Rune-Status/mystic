package org.mystic.game.model.content.minigames.godwars;

import org.mystic.game.model.content.minigames.godwars.GodWarsData.Allegiance;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.DefaultController;
import org.mystic.game.model.networking.outgoing.SendWalkableInterface;

public class GodWarsController extends DefaultController {

	@Override
	public void onControllerInit(Player player) {
		player.send(new SendWalkableInterface(16210));
		for (Allegiance allegiance : Allegiance.values()) {
			player.getMinigames().updateGWKC(allegiance);
		}
	}
}