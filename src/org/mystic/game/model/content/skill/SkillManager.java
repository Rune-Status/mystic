package org.mystic.game.model.content.skill;

import static org.mystic.game.model.content.skill.Skills.DUNGEONEERING;
import static org.mystic.game.model.content.skill.Skills.PRAYER;

import org.mystic.game.GameConstants;
import org.mystic.game.World;
import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendChatInterface;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendSkill;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendUpdateExperience;
import org.mystic.utility.Misc;

/**
 * Handles all skill functionality
 * 
 * @author Michael Sasse
 * @author Valiant - http://www.rune-server.org/members/Valiant
 * 
 */
public class SkillManager {

	public static final int[] EXP_FOR_LEVEL = new int[125];

	/**
	 * Declares a table to get the experience for a level
	 */
	public static final void declare() {
		int i = 0;
		for (int j = 0; j < EXP_FOR_LEVEL.length; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300.0D * Math.pow(2.0D, l / 7.0D));
			i += i1;
			EXP_FOR_LEVEL[j] = (i / 4);
		}
	}

	private double[] experience = new double[25];

	private int combatLevel = 0;

	private int totalLevel = 0;

	private final Player player;

	private boolean expLock = false;

	private long lock = 0L;

	/**
	 * Constructs a new skill instance
	 * 
	 * @param player
	 *            The player creating the skill instance
	 */
	public SkillManager(Player player) {
		this.player = player;
		for (int i = 0; i < 25; i++) {
			if (i == Skills.HITPOINTS) {
				getLevels()[i] = 10;
				experience[i] = 1154.0D;
			} else if (i == Skills.HERBLORE) {
				getLevels()[i] = 3;
				experience[i] = 174.0D;
			} else {
				getLevels()[i] = 1;
				experience[i] = 0.0D;
			}
		}
	}

	/**
	 * Increments this level by {@code amount} to a maximum of
	 * {@code realLevel + amount}.
	 *
	 * @param amount
	 *            the amount to increase this level by.
	 */
	public void increaseCurrentLevelMax(int skill, int amount) {
		increaseCurrentLevel(skill, amount, player.getMaxLevels()[skill] + amount);
	}

	/**
	 * Increases the current level
	 * 
	 * @param skill
	 * @param boostLevel
	 * @param cap
	 */
	public void increaseCurrentLevel(int skill, int amount, int max) {
		final int curr = getLevels()[skill];
		if ((curr + amount) > max) {
			setLevel(skill, max);
			return;
		}
		setLevel(skill, curr + amount);
	}

	/**
	 * Decrements this level by {@code amount} to {@code minimum}.
	 *
	 * @param amount
	 *            the amount to decrease this level by.
	 */
	public void decreaseCurrentLevel(int skill, int amount, int minimum) {
		final int curr = getLevels()[skill];
		if ((curr - amount) < minimum) {
			setLevel(skill, minimum);
			return;
		}
		setLevel(skill, curr - amount);
	}

	/**
	 * Adds combat experience after dealing damage
	 * 
	 * @param type
	 *            The type of combat the player dealt damage with
	 * @param hit
	 *            The amount of damage dealt
	 */
	public void addCombatExperience(CombatTypes type, int hit) {
		if ((expLock) || (player.getMagic().isDFireShieldEffect())) {
			return;
		}
		double base = player.getMagic().getSpellCasting().getBaseExperience();
		if (0 > hit && type == CombatTypes.MAGIC) {
			addExperience(6, base);
			return;
		}
		double exp;
		if (player.inWilderness() && player.getWildernessLevel() > 0
				|| player.getController().equals(ControllerManager.SAFE_WARS)) {
			if (player.getCombat().getAttacking() != null && player.getCombat().getAttacking().isNpc()) {
				exp = hit * GameConstants.EXPERIENCE_RATE;
			} else {
				exp = hit * 4.0;
			}
		} else {
			exp = hit * GameConstants.EXPERIENCE_RATE;
		}
		switch (type) {
		case MELEE:
			switch (player.getEquipment().getAttackStyle()) {
			case ACCURATE:
				addExperience(0, exp);
				break;
			case AGGRESSIVE:
				addExperience(2, exp);
				break;
			case CONTROLLED:
				addExperience(0, exp / 3.0D);
				addExperience(2, exp / 3.0D);
				addExperience(1, exp / 3.0D);
				break;
			case DEFENSIVE:
				addExperience(1, exp);
				break;
			}
			break;
		case MAGIC:
			addExperience(6, exp + base);
			break;
		case RANGED:
			switch (player.getEquipment().getAttackStyle()) {
			case ACCURATE:
				addExperience(4, exp);
				break;
			case AGGRESSIVE:
				addExperience(4, exp);
				break;
			case CONTROLLED:
				addExperience(4, exp);
				break;
			case DEFENSIVE:
				addExperience(4, exp / 2.0D);
				addExperience(1, exp / 2.0D);
				break;
			}
			break;
		}
		addExperience(3, exp / 3);
	}

	/**
	 * Adds experience to the experience array
	 * 
	 * @param id
	 *            The id of the skill to add experience too
	 * @param experience
	 *            The amount of experience too add
	 * @return
	 */
	public double addExperience(int id, double experience) {
		if ((expLock) && (id <= 6)) {
			return 0;
		}
		if (id != Skills.ATTACK && id != Skills.STRENGTH && id != Skills.DEFENCE && id != Skills.HITPOINTS
				&& id != Skills.RANGED && id != Skills.MAGIC) {
			experience *= GameConstants.SKILL_RATE;
		}
		if (IronMan.isIronMan(player)) {
			experience = experience / 2;
		}
		this.experience[id] += experience;
		if (player.getMaxLevels()[id] == 99 && id != DUNGEONEERING
				|| player.getMaxLevels()[id] == 120 && id == DUNGEONEERING) {
			if (this.experience[id] >= 200000000) {
				this.experience[id] = 200000000;
			} else {
				player.getClient().queueOutgoingPacket(new SendUpdateExperience(id, (int) experience));
			}
			update(id);
			return experience;
		}
		player.getClient().queueOutgoingPacket(new SendUpdateExperience(id, (int) experience));
		int newLevel = getLevelForExperience(id, this.experience[id]);
		if (newLevel > 99 && id != DUNGEONEERING) {
			newLevel = 99;
		}
		if (newLevel > 120 && id == DUNGEONEERING) {
			newLevel = 120;
		}
		if (player.getMaxLevels()[id] < newLevel) {
			getLevels()[id] = ((short) (newLevel - (player.getMaxLevels()[id] - getLevels()[id])));
			player.getMaxLevels()[id] = ((short) (newLevel));
			updateTotalLevel();
			if ((id == 0) || (id == 2) || (id == 1) || (id == 3) || (id == 4) || (id == 6) || (id == 5)
					|| id == Skills.SUMMONING) {
				updateCombatLevel();
			}
			onLevelup(newLevel, id);
			if (id == DUNGEONEERING ? newLevel == 120 : newLevel == 99) {
				World.sendGlobalMessage(player.getUsername() + " has achieved the level of " + newLevel + " in "
						+ Skills.SKILL_NAMES[id] + ".", true);
			}
		}
		if (this.experience[id] >= 200000000) {
			this.experience[id] = 200000000;
		}
		update(id);
		return experience;
	}

	/**
	 * Deducts an amount from a skill
	 * 
	 * @param id
	 *            The id of the skill
	 * @param amount
	 *            The amount to remove from the skill
	 */
	public void deductFromLevel(int id, int amount) {
		getLevels()[id] = ((short) (getLevels()[id] - amount));
		if (getLevels()[id] < 0) {
			getLevels()[id] = 0;
		}
		update(id);
	}

	/**
	 * Gets the players combat levels
	 * 
	 * @return
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets the players current experience
	 * 
	 * @return
	 */
	public double[] getExperience() {
		return experience;
	}

	/**
	 * Gets a level based on the amount of experience provided
	 * 
	 * @param id
	 *            The skill to check the level for
	 * @param experience
	 *            The amount of experience to check for a level
	 * @return The level based on the provided experience
	 */
	public byte getLevelForExperience(final int id, double experience) {
		byte lvl = 0;
		int i = 0;
		for (int j = 0; j <= 121; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300.0D * Math.pow(2.0D, l / 7.0D));
			i += i1;
			int xp = i / 4;
			if (xp <= experience) {
				lvl = (byte) (j + 2);
			}
		}
		return (id != DUNGEONEERING ? lvl > 99 : lvl > 120) ? (byte) (id != DUNGEONEERING ? 99 : 120)
				: lvl == 0 ? 1 : lvl;
	}

	/**
	 * Gets the players levels
	 * 
	 * @return
	 */
	public short[] getLevels() {
		return player.getLevels();
	}

	public double getSummoningAddition() {
		double value = player.getMaxLevels()[Skills.SUMMONING] / 2 * 0.25;
		return value;
	}

	public double getSummoningAddition2() {
		double value = player.getMaxLevels()[Skills.SUMMONING] / 2 * 0.25;
		if (!player.inWilderness()) {
			value = 0;
		}
		return value;
	}

	/**
	 * Gets the total amount of combat experience
	 * 
	 * @return The total amount of combat experience
	 */
	public long getTotalCombatExperience() {
		long total = 0L;
		for (int i = 0; i <= 6; i++) {
			total = (long) (total + experience[i]);
		}
		return total;
	}

	/**
	 * Gets the players total experience
	 * 
	 * @return The players total experience
	 */
	public long getTotalExperience() {
		long total = 0L;
		for (double i : experience) {
			total = (long) (total + (i > 200000000.0D ? 200000000.0D : i));
		}
		return total;
	}

	/**
	 * Gets the players total level
	 * 
	 * @return
	 */
	public int getTotalLevel() {
		return totalLevel;
	}

	/**
	 * Gets the amount of experience for a level
	 * 
	 * @param skillId
	 *            The id of the skill
	 * @param level
	 *            The level the player is getting the experience for
	 * @return The amount of experience for a level
	 */
	public int getXPForLevel(int skillId, int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points = (int) (points + Math.floor(lvl + 300.0D * Math.pow(2.0D, lvl / 7.0D)));
			if ((lvl >= level) || (lvl == 99) && skillId != DUNGEONEERING || (lvl == 120) && skillId == DUNGEONEERING)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Gets if the player has a combat levels
	 * 
	 * @return
	 */
	public boolean hasCombatLevels() {
		for (int i = 0; i <= 6; i++) {
			if ((i == 3) && (player.getMaxLevels()[i] > 10)) {
				return true;
			}
			if (player.getMaxLevels()[i] > 1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets if the player has at least 2 99 skills
	 * 
	 * @return The player has at least 2 99's
	 */
	public boolean hasTwo99s() {
		byte c = 0;
		int index = 0;
		for (int i : player.getMaxLevels()) {
			if ((i == 99) && index != Skills.DUNGEONEERING || i == 120 && index == Skills.DUNGEONEERING) {
				if ((c = (byte) (c + 1)) == 2) {
					return true;
				}
			}
			index++;
		}
		return false;
	}

	/**
	 * Gets if the experience is locked
	 * 
	 * @return The experience is locked
	 */
	public boolean isExpLocked() {
		return expLock;
	}

	/**
	 * Locks the players skill from performing another skill for a certain delay
	 * 
	 * @param delay
	 *            The delay to lock the skills
	 */
	public void lock(int delay) {
		lock = (World.getCycles() + delay);
	}

	/**
	 * Gets if the players skill is locked
	 * 
	 * @return The players skills are locked
	 */
	public boolean locked() {
		return lock > World.getCycles();
	}

	/**
	 * Actions that take place on leveling up
	 * 
	 * @param lvl
	 *            The level the player has just achieved
	 * @param skill
	 *            The skill the player achieved the level in
	 */
	public void onLevelup(int lvl, int skill) {
		player.getUpdateFlags().sendGraphic(Skills.LEVELUP_GRAPHIC);
		String line1 = "Congratulations! You've just advanced " + Misc.getAOrAn(Skills.SKILL_NAMES[skill]) + " "
				+ Skills.SKILL_NAMES[skill] + " level!";
		String line2 = "You have now reached level " + lvl + "!";
		String line3 = "Congratulations, You've just advanced " + Misc.getAOrAn(Skills.SKILL_NAMES[skill]) + " "
				+ Skills.SKILL_NAMES[skill] + " level.";
		player.getClient().queueOutgoingPacket(new SendMessage(line3));
		player.getClient().queueOutgoingPacket(new SendChatInterface(Skills.CHAT_INTERFACES[skill][1]));
		player.getClient().queueOutgoingPacket(new SendString(line1, 4268));
		player.getClient().queueOutgoingPacket(new SendString(line2, 4269));
	}

	/**
	 * Updates the skills on login
	 */
	public void onLogin() {
		updateLevelsForExperience();
		updateCombatLevel();
		updateTotalLevel();
		for (int i = 0; i < 25; i++) {
			update(i);
		}
		player.getAchievements().set(player, "Reach 1,500 Total level", totalLevel, true);
		player.getAchievements().set(player, "Reach 2,000 Total level", totalLevel, true);
	}

	/**
	 * Resets the players skill back to default
	 * 
	 * @param id
	 *            The id of the skill to reset
	 */
	public void reset(int id) {
		if (id == 3) {
			getLevels()[id] = 10;
			experience[id] = 1154.0D;
			player.getMaxLevels()[id] = 10;
		} else {
			getLevels()[id] = 1;
			player.getMaxLevels()[id] = 1;
			experience[id] = 0.0D;
		}
		update(id);
		updateCombatLevel();
	}

	/**
	 * Resets the players combat stats, attack through magic
	 */
	public void resetCombatStats() {
		for (int i = 0; i <= 7; i++) {
			getLevels()[i] = player.getMaxLevels()[i];
			update(i);
		}
	}

	/**
	 * Restores the players levels back to normal
	 */
	public void restore() {
		for (int i = 0; i < 25; i++) {
			getLevels()[i] = player.getMaxLevels()[i];
			update(i);
		}
	}

	/**
	 * Sets the players current experience
	 * 
	 * @param experience
	 */
	public void setExperience(double[] experience) {
		this.experience = experience;
	}

	/**
	 * Sets the experience locked or unlocked
	 * 
	 * @param locked
	 *            If he experience is locked or unlocked
	 */
	public void setExpLock(boolean locked) {
		expLock = locked;
	}

	/**
	 * Sets a level by the id
	 * 
	 * @param id
	 *            The id of the skill
	 * @param level
	 *            The level to set the skill too
	 */
	public void setLevel(int id, int level) {
		getLevels()[id] = ((byte) level);
		update(id);
	}

	/**
	 * Updates all of the players skills
	 */
	public void update() {
		for (int i = 0; i < 25; i++) {
			update(i);
		}
	}

	/**
	 * Updates a skill by the id
	 * 
	 * @param id
	 *            The id of the skill being updated
	 */
	public void update(int id) {
		if (id == PRAYER) {
			player.send(new SendString("" + player.getLevels()[PRAYER] + "/" + player.getMaxLevels()[PRAYER], 687));
		}
		player.getClient().queueOutgoingPacket(new SendString("" + player.getLevels()[3] + "", 4016));
		player.getClient().queueOutgoingPacket(new SendString("" + player.getLevels()[5] + "", 4012));
		player.getClient().queueOutgoingPacket(new SendString("" + player.getMaxLevels()[3] + "", 4017));
		player.getClient().queueOutgoingPacket(new SendString("" + player.getMaxLevels()[5] + "", 4013));
		player.getClient().queueOutgoingPacket(new SendSkill(id, getLevels()[id], (int) experience[id]));
	}

	/**
	 * Updates the players combat level
	 */
	public void updateCombatLevel() {
		int s = this.combatLevel;
		double combatLevel = (player.getMaxLevels()[1] + player.getMaxLevels()[3]
				+ Math.floor(player.getMaxLevels()[5] / 2)) * 0.25D;
		double warrior = (player.getMaxLevels()[0] + player.getMaxLevels()[2]) * 0.325D;
		double ranger = player.getMaxLevels()[4] * 0.4875D;
		double mage = player.getMaxLevels()[6] * 0.4875D;
		if (player.inWilderness()) {
			this.combatLevel = ((int) (combatLevel + Math.max(warrior, Math.max(ranger, mage))));
		} else {
			this.combatLevel = ((int) (combatLevel + Math.max(warrior, Math.max(ranger, mage))
					+ player.getSkill().getSummoningAddition()));
		}
		if (s != combatLevel) {
			player.setAppearanceUpdateRequired(true);
			player.getClient()
					.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 3983));
			if (player.inWilderness()) {
				if (player.getSkill().getSummoningAddition() > 0) {
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 2280));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 331));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 12294));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 1768));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 429));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 1702));
					player.getClient()
							.queueOutgoingPacket(new SendString("Combat Level: " + player.getSkill().getCombatLevel()
									+ " + " + (int) Math.floor(player.getSkill().getSummoningAddition()), 4450));
				}
			} else {
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 2280));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 331));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 12294));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 1768));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 429));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 1702));
				player.getClient().queueOutgoingPacket(
						new SendString("Combat Level: " + player.getSkill().getCombatLevel(), 4450));
			}
		}
	}

	/**
	 * Updates all of the levels for the players experience
	 */
	public void updateLevelsForExperience() {
		for (int i = 0; i < 25; i++) {
			player.getMaxLevels()[i] = getLevelForExperience(i, experience[i]);
		}
	}

	/**
	 * Updates the total level
	 */
	public void updateTotalLevel() {
		totalLevel = 0;
		for (int i = 0; i < 25; i++) {
			totalLevel += player.getMaxLevels()[i];
		}
		player.getAchievements().set(player, "Reach 1,500 Total level", totalLevel, true);
		player.getAchievements().set(player, "Reach 2,000 Total level", totalLevel, true);
	}

}