package org.mystic.game.model.content.skill.slayer;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class Slayer {

	private final Player player;

	private int task = 0;

	private int amount = 0;

	private SlayerDifficulty current = null;

	public Slayer(Player p) {
		this.player = p;
	}

	public static boolean canAttackMob(Player player, Npc mob) {
		int requirement = getRequiredLevel(mob);
		if (requirement > 0) {
			if (player.getSkill().getLevels()[Skills.SLAYER] >= requirement) {
				return true;
			} else {
				player.getClient().queueOutgoingPacket(
						new SendMessage("You need a Slayer level of " + requirement + " to attack this monster."));
				return false;
			}
		}
		return false;
	}

	public static int getRequiredLevel(Npc mob) {
		switch (mob.getId()) {
		// crawling hand
		case 1648:
			return 5;

		// cave crawler
		case 1600:
			return 10;

		// banshee
		case 1612:
			return 15;

		// rockslug
		case 1631:
			return 20;

		// cockatrice
		case 1620:
			return 25;

		// pyrefiend
		case 1633:
			return 30;

		// basilisk
		case 1616:
			return 40;

		// jelly
		case 1637:
			return 52;

		// infernal mage
		case 1643:
			return 45;

		// bloodveld
		case 1618:
			return 50;

		// aberrant spectre
		case 1604:
			return 60;

		// turoth
		case 1626:
			return 55;

		// kurask
		case 1608:
			return 70;

		// cave horror
		case 4353:
			return 58;

		// gargoyle
		case 1610:
			return 75;

		// nechryael
		case 1613:
			return 80;

		// aquanite
		case 9172:
			return 78;

		// abby demon
		case 1615:
			return 85;

		// dark beast
		case 2783:
			return 90;

		// ice stryke
		case 9463:
			return 93;

		default:
			return 1;
		}
	}

	public static int getExperienceForKill(Npc mob) {
		switch (mob.getId()) {
		case 1459:
			return 74;
		// yak
		case 5529:
			return 28;

		// ghost
		case 103:
			return 25;

		// bosses
		case 7133:
		case 6222:
		case 6260:
		case 6247:
		case 6203:
		case 3200:
			return 275;

		case 1183:
		case 1184:
			return 80;

		// chaos dwarf
		case 119:
			return 61;

		// lesser demon
		case 82:
			return 79;

		// giant bat
		case 78:
			return 32;

		// ice warrior
		case 125:
			return 59;

		// ice giant
		case 111:
			return 70;

		// moss giant
		case 112:
			return 60;

		// hill giant
		case 117:
			return 35;

		// hill giant
		case 110:
			return 111;

		// hob goblin
		case 2687:
			return 29;

		// kalphite worker
		case 1153:
			return 40;

		// kalphite soldier
		case 1154:
			return 90;

		// chaos druid
		case 181:
			return 15;

		// green dragon
		case 941:
			return 75;

		// blue dragon
		case 55:
			return 107;

		// red dragon
		case 53:
			return 143;

		// black dragon
		case 54:
			return 199;

		// crawling hand
		case 1648:
			return 14;

		// cave crawler
		case 1600:
			return 22;

		// hell hound
		case 49:
			return 125;

		// rockslug
		case 1631:
			return 27;

		// banshee
		case 1612:
			return 22;

		// infernal mage
		case 1643:
			return 60;

		// bloodveld
		case 1618:
			return 120;

		// aberrant spectre
		case 1604:
			return 90;

		// dagannoth
		case 2455:
			return 85;

		// cockatrice
		case 1620:
			return 37;

		// pyrefiend
		case 1633:
			return 45;

		// basilisk
		case 1616:
			return 75;

		// jelly
		case 1637:
			return 75;

		// turoth
		case 1626:
			return 76;

		// kurask
		case 1608:
			return 97;

		// rockcrabs
		case 1265:
		case 1267:
			return 20;

		// cave horror
		case 4353:
			return 55;

		// waterfiend
		case 5361:
			return 128;

		// mithril dragon
		case 5363:
			return 273;

		// black demon
		case 84:
			return 157;

		// bronze dragon
		case 1590:
			return 125;

		// iron dragon
		case 1591:
			return 173;

		// steel dragon
		case 1592:
			return 220;
		// ice stryke
		case 9463:
			return 250;

		// gargoyle
		case 1610:
			return 105;

		// nechryael
		case 1613:
			return 105;

		// aquanite
		case 9172:
			return 125;

		// abby demon
		case 1615:
			return 185;

		// dark beast
		case 2783:
			return 225;

		default:
			return 10;
		}
	}

	public void assign(SlayerDifficulty diff) {
		switch (diff) {
		case LOW:
			SlayerTasks.Low[] lval = SlayerTasks.Low.values();
			SlayerTasks.Low set = lval[Misc.randomNumber(lval.length)];
			while (player.getMaxLevels()[Skills.SLAYER] < set.getLevel()) {
				set = lval[Misc.randomNumber(lval.length)];
			}
			task = set.getMobId();
			amount = ((12 + Misc.randomNumber(26)));
			current = SlayerDifficulty.LOW;
			break;
		case MEDIUM:
			SlayerTasks.Medium[] mval = SlayerTasks.Medium.values();
			SlayerTasks.Medium set2 = mval[Misc.randomNumber(mval.length)];
			while (player.getMaxLevels()[Skills.SLAYER] < set2.getLevel()) {
				set2 = mval[Misc.randomNumber(mval.length)];
			}
			task = set2.getMobId();
			amount = ((34 + Misc.randomNumber(47)));
			current = SlayerDifficulty.MEDIUM;
			break;
		case HIGH:
			SlayerTasks.High[] hval = SlayerTasks.High.values();
			SlayerTasks.High set3 = hval[Misc.randomNumber(hval.length)];
			while (player.getMaxLevels()[Skills.SLAYER] < set3.getLevel()) {
				set3 = hval[Misc.randomNumber(hval.length)];
			}
			task = set3.getMobId();
			amount = ((53 + Misc.randomNumber(62)));
			current = SlayerDifficulty.HIGH;
			break;
		case BOSS:
			SlayerTasks.Boss[] zval = SlayerTasks.Boss.values();
			SlayerTasks.Boss set4 = zval[Misc.randomNumber(zval.length)];
			while (player.getMaxLevels()[Skills.SLAYER] < set4.getLevel()) {
				set4 = zval[Misc.randomNumber(zval.length)];
			}
			task = set4.getMobId();
			amount = ((1 + Misc.randomNumber(0)));
			current = SlayerDifficulty.BOSS;
			break;
		}
	}

	public void checkForSlayer(Npc mob) {
		if (task == 0) {
			return;
		}
		if (player.getSlayer().getDifficulty() == null) {
			return;
		}
		if (GameDefinitionLoader.getNpcDefinition(mob.getId()).getName().toLowerCase()
				.equals(GameDefinitionLoader.getNpcDefinition(player.getSlayer().getTask()).getName().toLowerCase())) {
			amount = (amount - 1);
			player.getSkill().addExperience(Skills.SLAYER, getExperienceForKill(mob));
			if (amount == 0) {
				player.getAchievements().incr(player, "Complete a Slayer task");
				player.getAchievements().incr(player, "Complete 50 Slayer tasks");
				player.getSkill().addExperience(Skills.SLAYER, getExperienceForKill(mob) * 5);
				player.addSlayerPoints(current == SlayerDifficulty.LOW ? 10 + (player.isGoldMember() ? 1 : 0)
						: current == SlayerDifficulty.MEDIUM ? 20 + (player.isGoldMember() ? 2 : 0)
								: current == SlayerDifficulty.BOSS ? 50 + (player.isGoldMember() ? 4 : 0)
										: current == SlayerDifficulty.HIGH ? 35 + (player.isGoldMember() ? 3 : 0) : 0);
				player.getClient().queueOutgoingPacket(new SendMessage(
						"You have completed your Slayer task; return to your slayer master in edgeville for another."));
				player.getClient().queueOutgoingPacket(new SendMessage(
						"You now have a total amount of " + player.getSlayerPoints() + " slayer points."));
				player.getSlayer().reset();
				QuestTab.update(player);
			} else {
				QuestTab.update(player);
			}
		}
	}

	public int getAmount() {
		return amount;
	}

	public SlayerDifficulty getDifficulty() {
		return current;
	}

	public int getTask() {
		return task;
	}

	public boolean hasTask() {
		return (amount > 0) && (task != 0);
	}

	public void reset() {
		task = 0;
		amount = 0;
		current = null;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setCurrent(SlayerDifficulty current) {
		this.current = current;
	}

	public void setTask(int task) {
		this.task = task;
	}
}
