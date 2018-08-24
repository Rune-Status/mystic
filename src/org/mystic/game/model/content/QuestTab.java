package org.mystic.game.model.content;

import java.util.Objects;

import org.mystic.game.model.content.quest.QuestConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class QuestTab {

	public static void sendTab(Player player) {
		if (player.getQuesting().isQuestCompleted(QuestConstants.RECIPE_FOR_DISASTER)) {
			player.send(new SendString("@gre@Recipe for Disaster", 29162));
		} else if (player.getQuesting().getQuestStage(QuestConstants.RECIPE_FOR_DISASTER) > 0) {
			player.send(new SendString("@yel@Recipe for Disaster", 29162));
		} else {
			player.send(new SendString("@red@Recipe for Disaster", 29162));
		}
		player.send(new SendString("Coming soon", 29163));
		player.send(new SendString("Coming soon", 29164));
		player.send(new SendString("Coming soon", 29165));
		player.send(new SendString("Coming soon", 29166));
		String string = player.isGoldMember() ? "@gre@Yes" : "@red@No";
		player.send(new SendString("@or1@Gold Member: " + string, 29167));
		String string2 = player.getBankPinAttributes().hasBankPin() ? "@gre@Yes" : "@red@No";
		if (player.getSlayer().getTask() == 0) {
			player.send(new SendString("@or1@Task: @red@None.", 29168));
		} else {
			player.send(
					new SendString("@or1@Task: @gre@"
							+ Misc.formatPlayerName(
									GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName())
							+ "@or1@", 29168));
		}
		player.send(new SendString("@or1@Bank Pin: " + string2, 29169));
		if (player.getEarningPotential().getPkp() == 0) {
			player.send(new SendString("@or1@PK Points: @red@" + player.getEarningPotential().getPkp() + "", 29170));
		} else {
			player.send(new SendString("@or1@PK Points: @gre@" + player.getEarningPotential().getPkp() + "", 29170));
		}
		if (player.getKills() == 0) {
			player.send(new SendString("@or1@Kills: @red@" + player.getKills() + "", 29171));
		} else {
			player.send(new SendString("@or1@Kills: @gre@" + player.getKills() + "", 29171));
		}
		if (player.getDeaths() == 0) {
			player.send(new SendString("@or1@Deaths: @red@" + player.getDeaths() + "", 29172));
		} else {
			player.send(new SendString("@or1@Deaths: @gre@" + player.getDeaths() + "", 29172));
		}
		if (player.getKillstreak() == 0) {
			player.send(new SendString("@or1@Killstreak: @red@" + player.getKillstreak() + "", 29173));
		} else {
			player.send(new SendString("@or1@Killstreak: @gre@" + player.getKillstreak() + "", 29173));
		}
		if (player.getSlayerPoints() == 0) {
			player.send(new SendString("@or1@Slayer Points: @red@" + player.getSlayerPoints(), 29174));
		} else {
			player.send(new SendString("@or1@Slayer Points: @gre@" + player.getSlayerPoints(), 29174));
		}
		if (player.getVotePoints() == 0) {
			player.send(new SendString("@or1@Voting Points: @red@" + player.getVotePoints(), 29180));
		} else {
			player.send(new SendString("@or1@Voting Points: @gre@" + player.getVotePoints(), 29180));
		}
		if (player.getAchievementPoints() > 0) {
			player.send(new SendString("@or1@Achievement Points: @gre@" + player.getAchievementPoints(), 29176));
		} else {
			player.send(new SendString("@or1@Achievement Points: @red@" + player.getAchievementPoints(), 29176));
		}
		if (player.getRunecraftingPoints() > 0) {
			player.send(new SendString("@or1@Runecrafting Points: @gre@" + player.getRunecraftingPoints(), 29177));
		} else {
			player.send(new SendString("@or1@Runecrafting Points: @red@" + player.getRunecraftingPoints(), 29177));
		}
		if (player.getDuelWins() > 0) {
			player.send(new SendString("@or1@Duel Arena Wins: @gre@" + player.getDuelWins(), 29178));
		} else {
			player.send(new SendString("@or1@Duel Arena Wins: @red@" + player.getDuelWins(), 29178));
		}
		if (player.getDuelLosses() > 0) {
			player.send(new SendString("@or1@Duel Arena Losses: @gre@" + player.getDuelLosses(), 29179));
		} else {
			player.send(new SendString("@or1@Duel Arena Losses: @red@" + player.getDuelLosses(), 29179));
		}
		if (player.getDonatorPoints() > 0) {
			player.send(new SendString("@or1@Member Points: @gre@" + player.getDonatorPoints(), 29175));
		} else {
			player.send(new SendString("@or1@Member Points: @red@" + player.getDonatorPoints(), 29175));
		}
	}

	public static void update(Player player) {
		if (Objects.nonNull(player)) {
			sendTab(player);
		}
	}

}