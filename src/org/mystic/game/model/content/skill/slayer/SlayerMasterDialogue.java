package org.mystic.game.model.content.skill.slayer;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.Dialogue;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.content.dialogue.Emotion;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class SlayerMasterDialogue extends Dialogue {

	private final int masterId;

	public SlayerMasterDialogue(Player player, int masterId) {
		this.player = player;
		this.masterId = masterId;
	}

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		case 9157:
			next = 8;
			execute();
			return true;
		case 9158:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			return true;
		case 9178:
			if (option == 2) {
				if (player.getSlayer().hasTask()) {
					DialogueManager
							.sendNpcChat(player, masterId, Emotion.CALM,
									new String[] {
											"You're still hunting "
													+ Misc.formatPlayerName(GameDefinitionLoader
															.getNpcDefinition(player.getSlayer().getTask()).getName())
													+ "'s.",
											"You may pay me to reset your task or come back",
											"when you are finished." });
					end();
				}
			} else {
				next = 2;
				execute();
			}
			return true;
		case 9179:
			next = 3;
			execute();
			return true;
		case 9180:
			next = 4;
			execute();
			return true;
		case 9181:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
			return true;
		}
		return false;
	}

	private void assign(Player player) {
		if (masterId == 8275) {
			player.getSlayer().assign(SlayerDifficulty.HIGH);
		} else if (masterId == 8274) {
			player.getSlayer().assign(SlayerDifficulty.BOSS);
		} else if (masterId == 1597) {
			player.getSlayer().assign(SlayerDifficulty.MEDIUM);
		} else if (masterId == 8273) {
			player.getSlayer().assign(SlayerDifficulty.LOW);
		}
		QuestTab.update(player);
		DialogueManager.sendNpcChat(player, masterId, Emotion.CALM,
				new String[] { "Excellent, you're doing great. Your new task is to kill ",
						player.getSlayer().getAmount() + " x " + Misc.formatPlayerName(
								GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName()) });
		end();
	}

	@Override
	public void execute() {
		switch (next) {
		case 0:
			DialogueManager.sendNpcChat(player, masterId, Emotion.CALM,
					new String[] { "'Ello, and what are you after then?" });
			next += 1;
			break;
		case 1:
			DialogueManager.sendOption(player, new String[] { "I need another assignment.", "Where is my task located?",
					"Can you reset my Slayer task?", "Er... Nothing..." });
			option = 1;
			break;
		case 2:
			if (player.getSlayer().hasTask()) {
				DialogueManager
						.sendNpcChat(player, masterId, Emotion.CALM,
								new String[] {
										"You're still hunting "
												+ Misc.formatPlayerName(GameDefinitionLoader
														.getNpcDefinition(player.getSlayer().getTask()).getName())
												+ "'s.",
										"You may pay me to reset your task or come back", "when you are finished." });
				end();
			} else {
				assign(player);
			}
			end();
			break;
		case 3:
			String[] text = new String[] { "You do not have a slayer task." };
			if (player.getSlayer().getDifficulty() != null) {
				switch (player.getSlayer().getDifficulty()) {
				case LOW:
					SlayerTasks.Low data = SlayerTasks.forLow(player.getSlayer().getTask());
					if (data == null) {
						return;
					}
					text = data.getLocation();
					break;
				case MEDIUM:
					SlayerTasks.Medium data2 = SlayerTasks.forMedium(player.getSlayer().getTask());
					if (data2 == null) {
						return;
					}
					text = data2.getLocation();
					break;
				case HIGH:
					SlayerTasks.High data3 = SlayerTasks.forHigh(player.getSlayer().getTask());
					if (data3 == null) {
						return;
					}
					text = data3.getLocation();
					break;
				case BOSS:
					SlayerTasks.Boss data4 = SlayerTasks.forBoss(player.getSlayer().getTask());
					if (data4 == null) {
						return;
					}
					text = data4.getLocation();
					break;

				}
			}
			DialogueManager.sendNpcChat(player, masterId, Emotion.CALM, text);
			next = 6;
			break;
		case 4:
			DialogueManager
					.sendNpcChat(player, masterId, Emotion.CALM,
							new String[] { "It will cost you "
									+ (player.isGoldMember() ? "10 Slayer Points" : "15 Slayer Points")
									+ " to reset your task.", "This can't be undone, are you sure?" });
			next = 7;
			break;
		case 5:
			String task = Misc
					.formatPlayerName(GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName());
			int am = player.getSlayer().getAmount();
			DialogueManager.sendNpcChat(player, masterId, Emotion.CALM,
					new String[] { "Your new task is to kill " + am + " " + task + "s." });
			QuestTab.update(player);
			end();
			break;
		case 6:
			player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			end();
			break;
		case 7:
			DialogueManager.sendOption(player, new String[] { "Yes.", "No." });
			break;
		case 8:
			int amount = player.isGoldMember() ? 10 : 15;
			if (!player.getSlayer().hasTask()) {
				DialogueManager.sendStatement(player, new String[] { "You do not have an active task to reset.",
						"You can get a slayer task by speaking to any slayer master." });
				end();
			} else if (player.getSlayerPoints() < amount) {
				DialogueManager.sendStatement(player,
						new String[] { "You do not have the Slayer points needed to reset your task." });
				end();
			} else {
				player.getSlayer().reset();
				QuestTab.update(player);
				DialogueManager.sendStatement(player, new String[] { "Your slayer task has been reset.",
						"Visit your nearest slayer master for a new one." });
				player.setSlayerPoints(player.getSlayerPoints() - amount);
				end();
			}
			break;
		}
	}
}
