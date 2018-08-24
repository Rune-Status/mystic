package org.mystic.game.model.content.quest;

import java.util.ArrayList;
import java.util.List;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.utility.Misc;

public class Questing {

	private final Player player;
	private final byte[] questStages = new byte[QuestConstants.TOTAL_QUESTS];
	private final List<Quest> activeQuests = new ArrayList<Quest>();

	public Questing(Player player) {
		this.player = player;
	}

	public void onLogin() {

	}

	public boolean clickQuestButton(int id) {
		for (int i = 0; i < QuestConstants.QUEST_BUTTONS.length; i++) {
			if (id == QuestConstants.QUEST_BUTTONS[i]) {
				Misc.openBlankQuestDialogue(player);
				String[] lines = QuestConstants.QUESTS_BY_ID[i].getLinesForStage(questStages[i]);
				player.getClient().queueOutgoingPacket(new SendString(QuestConstants.QUESTS_BY_ID[i].getName(), 8144));
				if (lines == null) {
					return true;
				}
				for (int k = 0; k < lines.length; k++) {
					player.getClient().queueOutgoingPacket(new SendString(lines[k], 8149 + k));
				}
				return true;
			}
		}
		return false;
	}

	public void openQuestPage(int id) {
		Misc.openBlankQuestDialogue(player);
		String[] lines = QuestConstants.QUESTS_BY_ID[id].getLinesForStage(questStages[id]);
		player.getClient().queueOutgoingPacket(new SendString(QuestConstants.QUESTS_BY_ID[id].getName(), 8144));
		if (lines == null) {
			return;
		}
		for (int k = 0; k < lines.length; k++) {
			player.getClient().queueOutgoingPacket(new SendString(lines[k], 8149 + k));
		}
	}

	public void reset(int id) {
		activeQuests.remove(QuestConstants.QUESTS_BY_ID[id]);
		questStages[id] = 0;
	}

	public int getQuestPoints() {
		int points = 0;
		for (int i = 0; i < QuestConstants.TOTAL_QUESTS; i++) {
			if (isQuestCompleted(QuestConstants.QUESTS_BY_ID[i])) {
				points += QuestConstants.QUESTS_BY_ID[i].getPoints();
			}
		}
		return points;
	}

	public byte getQuestStage(Quest quest) {
		return questStages[quest.getId()];
	}

	public void setQuestStage(Quest quest, int stage) {
		if (stage == 0) {
			activeQuests.remove(QuestConstants.QUESTS_BY_ID[quest.getId()]);
		}
		questStages[quest.getId()] = ((byte) stage);
		if (questStages[quest.getId()] == QuestConstants.QUESTS_BY_ID[quest.getId()].getFinalStage()) {
			player.getClient().queueOutgoingPacket(new SendMessage("Congratulations, you have completed the quest: "
					+ QuestConstants.QUESTS_BY_ID[quest.getId()].getName() + "!"));
			if (activeQuests.size() > 0) {
				activeQuests.remove(quest.getId());
			}
			player.getClient().queueOutgoingPacket(
					new SendString(QuestConstants.QUESTS_BY_ID[quest.getId()].getPoints() + "", 4444));
			player.getClient().queueOutgoingPacket(new SendString(
					"You've completed " + QuestConstants.QUESTS_BY_ID[quest.getId()].getName() + "!", 301));
			player.getClient().queueOutgoingPacket(new SendString(getQuestPoints() + "", 304));
			player.getClient().queueOutgoingPacket(new SendInterface(297));
		} else {
			if (!activeQuests.contains(QuestConstants.QUESTS_BY_ID[quest.getId()])) {
				activeQuests.add(QuestConstants.QUESTS_BY_ID[quest.getId()]);
			}
		}
	}

	public void incrQuestStage(Quest quest) {
		questStages[quest.getId()]++;
		if (questStages[quest.getId()] > quest.getFinalStage()) {
			Exception e = new Exception("Quest attempt to increment a completed quest!");
			e.printStackTrace();
			questStages[quest.getId()] = quest.getFinalStage();
			return;
		}
	}

	public boolean isQuestCompleted(Quest quest) {
		return questStages[quest.getId()] == quest.getFinalStage();
	}

	public void setCompleted(Quest quest) {
		questStages[quest.getId()] = quest.getFinalStage();
		if (questStages[quest.getId()] == quest.getFinalStage()) {
			player.getClient().queueOutgoingPacket(
					new SendMessage("Congratulations, you have completed the quest: " + quest.getName() + "!"));
			QuestTab.sendTab(player);
			activeQuests.remove(quest);
			player.getClient().queueOutgoingPacket(new SendString(quest.getPoints() + "", 4444));
			player.getClient().queueOutgoingPacket(new SendString("You've completed " + quest.getName() + "!", 301));
			player.getClient().queueOutgoingPacket(new SendString(getQuestPoints() + "", 304));
			player.getClient().queueOutgoingPacket(new SendInterface(297));
		}
	}

	public boolean isQuestActive(Quest quest) {
		return activeQuests.contains(quest);
	}

	public void setQuestActive(Quest quest, boolean active) {
		if (active) {
			if (!activeQuests.contains(quest))
				activeQuests.add(quest);
		} else {
			activeQuests.remove(quest);
		}
	}

	public boolean isClickQuestObject(int option, int id) {
		for (int i = 0; i < activeQuests.size(); i++) {
			Quest k = activeQuests.get(i);
			if (k.clickObject(player, option, id)) {
				return true;
			}
		}
		return false;
	}

	public boolean isUseItemOnQuestObject(int item, int object) {
		for (int i = 0; i < activeQuests.size(); i++) {
			Quest k = activeQuests.get(i);
			if (k.useItemOnObject(player, item, object)) {
				return true;
			}
		}
		return false;
	}

	public byte[] getQuestStages() {
		return questStages;
	}

	public List<Quest> getActiveQuests() {
		return activeQuests;
	}

}