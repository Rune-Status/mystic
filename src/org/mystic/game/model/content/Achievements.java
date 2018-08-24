package org.mystic.game.model.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;

public class Achievements {

	public static final int START_INDEX = 15007;

	private static final String[] ACHIEVEMENTS = { "TYPE:Easy", "Learning the ropes", "Kill 10 Rock crabs",
			"Complete a Slayer task", "Win a duel", "Get tele-blocked", "Get skulled", "Have 25 friends",
			"Use a Special attack", "Finish Gnome agility course", "TYPE:Medium", "Repair 25 equipment",
			"Defeat 500 monsters", "Kill 25 players in Wild", "Obtain 50 rare drops", "Vote 50 times", "Bury 500 bones",
			"Achieve 138 combat", "Die 10 times", "Use 1,000 consumables", "Mine 500 ores", "Harvest 500 crops",
			"Create 500 potions", "Fletch 1,000 logs", "Complete 50 Slayer tasks", "Spend 10,000,000 in Shops",
			"Fish 1,000 items", "Cook 1,000 items", "Burn 1,000 logs", "Cut 1,000 trees", "Cast 1,000 spells",
			"Alch 10,000,000gp", "Inflict 50 damage in one hit", "Reach 1,500 Total level", "Kill the Corporeal Beast",
			"Defeat TzTok-Jad", "Finish Wilderness course", "TYPE:Hard", "Die 50 times", "Spend 50,000,000 in Shops",
			"Repair 75 equipment", "Kill 50 Corporeal beasts", "Kill 50 King Black Dragons", "Use 10,000 consumables",
			"Achieve 1,000 total GWD KC", "Win 100 Duels", "Achieve 500 total PKP", "Bury 5,000 bones",
			"Cast 10,000 spells", "Alch 50,000,000gp", "Obtain 250 rare drops", "Alch an Abyssal whip",
			"Use 1,000 Special attacks", "Inflict 75 damage in one hit", "Reach 2,000 Total level",
			"Equip the Max cape" };

	public static final int[] COMPLETION_DATA = new int[ACHIEVEMENTS.length];

	private static final Map<String, Integer> completionAmountForName = new HashMap<String, Integer>();

	public static void declare() {
		for (int i = 0; i < ACHIEVEMENTS.length; i++) {
			String check = ACHIEVEMENTS[i];
			if (check.contains(",")) {
				check = check.replaceAll(",", "");
			}
			if ((check.contains("30-30")) || ((check.contains("Inflict")) && (check.contains("damage")))) {
				COMPLETION_DATA[i] = 1;
				completionAmountForName.put(ACHIEVEMENTS[i], 1);
			} else {
				int start = -1;
				for (int k = 0; k < 10; k++) {
					int indexOfNumber = check.indexOf("" + k);

					if ((indexOfNumber != -1 && start > indexOfNumber) || (indexOfNumber != -1 && start == -1)) {
						start = indexOfNumber;
					}
				}
				if (start == -1) {
					COMPLETION_DATA[i] = 1;
					completionAmountForName.put(ACHIEVEMENTS[i], 1);
				} else {
					int end = -1;
					for (int k = 0; k < 10; k++) {
						int indexOfNumber = check.lastIndexOf("" + k);

						if ((indexOfNumber != -1 && end < indexOfNumber + 1) || (indexOfNumber != -1 && end == -1)) {
							end = indexOfNumber + 1;
						}
					}
					int amount = Integer.parseInt(check.substring(start, end));
					COMPLETION_DATA[i] = amount;
					completionAmountForName.put(ACHIEVEMENTS[i], amount);
				}
			}
		}
	}

	public static int getAchievementAmount() {
		return completionAmountForName.size();
	}

	public static int getIndex(String name) {
		for (int i = 0; i < ACHIEVEMENTS.length; i++) {
			if (ACHIEVEMENTS[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isHeader(int slot) {
		return ACHIEVEMENTS[(slot - 15007)].contains("TY");
	}

	private Map<String, Integer> data = new HashMap<String, Integer>();

	public void complete(Player player, int index) {
		player.achievementpoints++;
		player.getClient().queueOutgoingPacket(
				new SendMessage("You have completed the achievement: @blu@" + ACHIEVEMENTS[index] + "@bla@."));
		player.getClient().queueOutgoingPacket(new SendMessage(
				"You now have a total of @blu@" + player.getAchievementPoints() + " Achievement Points."));
		QuestTab.update(player);
	}

	public int getCompletedAchievmentAmount() {
		int completed = 0;
		for (Iterator<Entry<String, Integer>> i = completionAmountForName.entrySet().iterator(); i.hasNext();) {
			Entry<String, Integer> entry = i.next();
			String name = entry.getKey();
			if (!completionAmountForName.containsKey(name)) {
				Exception e = new Exception("No completion amount for achievments on map: " + name);
				e.printStackTrace();
				continue;
			}
			if (isCompleted(name)) {
				completed++;
			}
		}
		return completed;
	}

	public String[][] getData() {
		String[][] o = new String[data.size()][2];
		int k = 0;
		for (int i = 0; i < ACHIEVEMENTS.length; i++) {
			if (data.containsKey(ACHIEVEMENTS[i])) {
				o[k] = new String[] { ACHIEVEMENTS[i], data.get(ACHIEVEMENTS[i]).toString() };
				k++;
			}
		}
		return o;
	}

	public void incr(Player player, String name) {
		incr(player, name, 1);
	}

	public void incr(Player player, String name, int amount) {
		int index = getIndex(name);
		if (index == -1) {
			Exception e = new Exception("Achievement not found: " + name);
			e.printStackTrace();
			return;
		}
		if (data.get(name) == null) {
			data.put(name, Integer.valueOf(1));
			if (1 >= COMPLETION_DATA[index])
				complete(player, index);
		} else {
			int i = data.get(name).intValue();
			if (i >= COMPLETION_DATA[index]) {
				return;
			}
			data.remove(name);
			if (i + amount >= COMPLETION_DATA[index]) {
				amount = 0;
				i = COMPLETION_DATA[index];
				complete(player, index);
			}
			data.put(name, Integer.valueOf(i + amount));
		}
		update(player, index);
	}

	public boolean isComplete(int index) {
		return (data.get(ACHIEVEMENTS[index]) != null)
				&& (data.get(ACHIEVEMENTS[index]).intValue() == COMPLETION_DATA[index]);
	}

	public boolean isCompleted(String name) {
		if (!completionAmountForName.containsKey(name)) {
			Exception e = new Exception("Does not exist in achievement completion data: " + name);
			e.printStackTrace();
			return false;
		}
		return data.containsKey(name) && data.get(name) >= completionAmountForName.get(name);
	}

	public void onKillMob(Player p, Npc mob) {
		incr(p, "Defeat 500 monsters");
		switch (mob.getId()) {
		case 1265:
		case 1267:
			incr(p, "Kill 10 Rock crabs");
			break;
		case 50:
			incr(p, "Kill 50 King Black Dragons");
			break;
		case 2745:
			incr(p, "Defeat TzTok-Jad");
			break;
		case 8133:
			incr(p, "Kill 50 Corporeal beasts");
			break;
		}
	}

	public void onLogin(Player player) {
		for (int i = 0; i < ACHIEVEMENTS.length; i++) {
			update(player, i);
		}
	}

	public void set(Player player, String name, int set, boolean override) {
		int index = getIndex(name);
		if (index == -1) {
			Exception e = new Exception("Achievement not found: " + name);
			e.printStackTrace();
			return;
		}
		boolean wasComplete = false;
		if (data.get(name) != null) {
			if (!override && data.get(name).intValue() >= COMPLETION_DATA[index]) {
				return;
			} else if (data.get(name).intValue() >= COMPLETION_DATA[index]) {
				wasComplete = true;
			}
			data.remove(name);
		}
		if (set > COMPLETION_DATA[index]) {
			set = COMPLETION_DATA[index];
			if (!wasComplete) {
				complete(player, index);
			}
		}
		data.put(name, Integer.valueOf(set));
		update(player, index);
	}

	public void setAllCompleted() {
		for (int i = 0; i < ACHIEVEMENTS.length; i++) {
			if (data.containsKey(ACHIEVEMENTS[i])) {
				data.remove(ACHIEVEMENTS[i]);
			}
			data.put(ACHIEVEMENTS[i], COMPLETION_DATA[i]);
		}
	}

	public void setData(String[][] set) {
		for (int i = 0; i < set.length; i++) {
			if ((set[i] != null) && (set[i][0] != null) && (set[i][1] != null)) {
				data.put(set[i][0], Integer.valueOf(Integer.parseInt(set[i][1])));
			}
		}
	}

	public void update(Player player, int index) {
		int slot = 15007 + index;
		if (!isHeader(slot)) {
			boolean active = data.containsKey(ACHIEVEMENTS[index]);
			if (!active) {
				player.getClient().queueOutgoingPacket(new SendString("@red@" + ACHIEVEMENTS[index], slot));
			} else {
				int progress = data.get(ACHIEVEMENTS[index]);
				int completed = COMPLETION_DATA[index];
				String line;
				if (isComplete(index)) {
					line = "@gre@" + ACHIEVEMENTS[index];
				} else {
					int percent = ((int) (((double) progress / (double) completed) * 100));
					line = "@yel@" + ACHIEVEMENTS[index] + " (" + percent + "%)";
				}
				player.send(new SendString(line, slot));
			}
		}
	}
}
