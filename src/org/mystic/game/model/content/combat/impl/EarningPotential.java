package org.mystic.game.model.content.combat.impl;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.QuestTab;
import org.mystic.game.model.content.dialogue.DialogueManager;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.player.Inventory;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class EarningPotential {

	public enum StatueData {

		ARTEFACT_RECEIPT(11974, 400000),

		ANCIENT(14876, 5000000),

		SEREN(14877, 2500000),

		ARMADYL(14878, 700000),

		ZAMORAK(14879, 1000000),

		SARADOMIN(14880, 800000),

		BANDOS(14881, 750000),

		RUBY(14882, 600000),

		GUTHIX(14883, 500000),

		ZAMORAK_B(14885, 500000),

		THIRD_AGE(14891, 350000),

		STAT_HEADDRESS(14892, 350000),

		SARADOMIN_B(14886, 250000),

		BANDOS_B(14887, 150000),

		SARADOMIN_C(14888, 150000),

		ANCIENT_B(14889, 150000),

		BRONZED_CLAW(14890, 150000),

		ARMADYL_B(14884, 100000);

		private static final Map<Integer, Integer> stats = new HashMap<Integer, Integer>();

		static {
			for (StatueData i : values()) {
				stats.put(Integer.valueOf(i.id), Integer.valueOf(i.coins));
			}
		}

		public static final int forId(int id) {
			if (stats.containsKey(Integer.valueOf(id))) {
				return stats.get(Integer.valueOf(id)).intValue();
			}
			return 0;
		}

		private final int id, coins;

		private StatueData(final int id, final int coins) {
			this.id = id;
			this.coins = coins;
		}
	}

	public static int getCarriedWealth(Player p) {
		int w = 0;
		for (Item i : p.getEquipment().getItems()) {
			if (i != null) {
				w += GameDefinitionLoader.getHighAlchemyValue(i.getId()) * i.getAmount();
			}
		}
		for (Item i : p.getInventory().getItems()) {
			if (i != null) {
				w += GameDefinitionLoader.getHighAlchemyValue(i.getId()) * i.getAmount();
			}
		}
		return w < 0 ? -w : w;
	}

	private final Player player;

	private int pkp = 0;

	private long[] killOrKilledBy = new long[5];

	public EarningPotential(Player player) {
		this.player = player;
	}

	public void add(Player player) {
		for (int i = 0; i < killOrKilledBy.length; i++) {
			if (killOrKilledBy[i] == 0L) {
				killOrKilledBy[i] = player.getUsernameToLong();
				return;
			}
		}
		killOrKilledBy = new long[5];
		killOrKilledBy[0] = player.getUsernameToLong();
	}

	public void addPKP() {
		int amount = getAmount();
		player.getEarningPotential().setPkp(player.getEarningPotential().getPkp() + amount);
		QuestTab.update(player);
		player.getAchievements().incr(player, "Achieve 500 total PKP", amount);
	}

	public void addPKP(int amount) {
		player.getEarningPotential().setPkp(player.getEarningPotential().getPkp() + amount);
		QuestTab.update(player);
		player.getAchievements().incr(player, "Achieve 500 total PKP", amount);
	}

	public boolean allow(Player other, boolean message) {
		if (PlayerConstants.isOwner(player) || PlayerConstants.isOwner(other)) {
			return true;
		}
		if (!other.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)
				|| !player.getController().equals(ControllerManager.WILDERNESS_CONTROLLER)) {
			return false;
		}
		if (other.getClient().getHostId() == player.getClient().getHostId()) {
			if (message)
				other.getClient().queueOutgoingPacket(
						new SendMessage("You do not receive PKP for killing a player with the same IP address."));
			return false;
		}
		if (getCarriedWealth(player) < 75000) {
			if (message)
				other.getClient().queueOutgoingPacket(new SendMessage(
						"The other player didn't have enough carried wealth for you to gain any Pk Points."));
			return false;
		}
		if (getCarriedWealth(other) < 75000) {
			if (message)
				other.getClient().queueOutgoingPacket(
						new SendMessage("You do not have enough carried wealth for you to gain any Pk Points."));
			return false;
		}
		return true;
	}

	public boolean contains(Player player) {
		for (long i : killOrKilledBy) {
			if (i == player.getUsernameToLong()) {
				return true;
			}
		}
		return false;
	}

	public void decreasePKP(int amount) {
		pkp -= amount;
	}

	public void drop(Player killer) {
		killer.incrKills();
		player.incrDeaths();
		if (killer.getEP() >= 30) {
			for (int i = 0; i < 1 + Misc.randomNumber(2); i++) {
				Item stat = null;
				stat = new Item(StatueData.values()[Misc.randomNumber(StatueData.values().length)].id, 1);
				if (stat != null) {
					GroundItemHandler.add(stat, player.getLocation(), killer);
					killer.setEP(0);
					QuestTab.update(killer);
					return;
				}
			}
		}
	}

	public int getAmount() {
		int amount = 0;
		if (player.getWildernessLevel() > 25) {
			amount = 2;
		} else {
			return 1;
		}
		return amount;
	}

	public long[] getKillOrKilledBy() {
		return killOrKilledBy;
	}

	public int getPkp() {
		return pkp;
	}

	public void onKilled(Entity killer) {
		if (killer == null) {
			return;
		}
		if (!killer.isNpc()) {
			Player other = org.mystic.game.World.getPlayers()[killer.getIndex()];
			if (other == null) {
				return;
			}
			if ((contains(other))) {
				other.getClient().queueOutgoingPacket(new SendMessage(
						"@blu@You have recently killed or have been killed by " + player.getUsername() + "."));
				return;
			}
			add(other);
			if (allow(other, true)) {
				other.getEarningPotential().add(player);
				this.drop((Player) killer);
				other.getEarningPotential().addPKP();
				Killstreaks.increase((Player) killer);
				other.getAchievements().incr(player, "Kill 25 players in Wild");
				other.send(new SendMessage("@blu@You recieve " + getAmount() + " Pk-Credit(s) for your kill."
						+ " You now have " + other.getEarningPotential().getPkp() + " Pk-Credit(s)."));
				QuestTab.update(other);
				QuestTab.update(player);
			}
		}
	}

	public void setKillOrKilledBy(long[] killOrKilledBy) {
		this.killOrKilledBy = killOrKilledBy;
	}

	public void setPkp(int pkp) {
		this.pkp = pkp;
	}

	public void trade() {
		Inventory inv = player.getInventory();
		int total = 0;
		for (int i = 0; i < 28; i++) {
			Item k = inv.get(i);
			if (k != null) {
				int am = StatueData.forId(k.getId());
				if ((am > 0) && (inv.hasSpaceOnRemove(new Item(k.getId(), 1), new Item(995, am)))) {
					total += am;
					inv.setSlot(null, i);
					inv.add(995, am, false);
				}
			}
		}
		if (total == 0) {
			DialogueManager.sendStatement(player, new String[] { "You do not have any artifacts to exchange." });
		} else {
			player.getInventory().update();
			DialogueManager.sendStatement(player,
					new String[] { "You recieve " + "@red@" + Misc.formatCoins(total) + "@bla@ for your artifacts." });
		}
	}

}