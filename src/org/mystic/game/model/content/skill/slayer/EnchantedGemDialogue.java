package org.mystic.game.model.content.skill.slayer;

import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class EnchantedGemDialogue extends Dialogue {

	public EnchantedGemDialogue(Player player) {
		this.player = player;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9167:
			if (player.getSlayer().hasTask()) {
				DialogueManager.sendStatement(player,
						new String[] { "You have been assigned to kill another ",
								player.getSlayer().getAmount() + " x " + Misc.formatPlayerName(
										GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName())
										+ "." });
			} else {
				DialogueManager.sendStatement(player, new String[] { "You do not have a slayer task." });
			}
			end();
			return true;
		case 9168:
			String[] text = new String[] { "You do not have a slayer task." };
			if (player.getSlayer().getDifficulty() != null) {
				switch (player.getSlayer().getDifficulty()) {
				case LOW:
					SlayerTasks.Low data = SlayerTasks.forLow(player.getSlayer().getTask());
					if (data == null) {
						break;
					}
					text = data.getLocation();
					break;
				case MEDIUM:
					SlayerTasks.Medium data2 = SlayerTasks.forMedium(player.getSlayer().getTask());
					if (data2 == null) {
						break;
					}
					text = data2.getLocation();
					break;
				case HIGH:
					SlayerTasks.High data3 = SlayerTasks.forHigh(player.getSlayer().getTask());
					if (data3 == null) {
						break;
					}
					text = data3.getLocation();
					break;
				case BOSS:
					SlayerTasks.High data4 = SlayerTasks.forHigh(player.getSlayer().getTask());
					if (data4 == null) {
						break;
					}
					text = data4.getLocation();
					break;

				}
			}
			DialogueManager.sendStatement(player, text);
			end();
			return true;
		case 9169:
			end();
			player.send(new SendRemoveInterfaces());
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		DialogueManager.sendOption(player, new String[] { "Kills Left", "Task Location", "Nevermind" });
	}
}
