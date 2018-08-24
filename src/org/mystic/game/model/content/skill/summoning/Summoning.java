package org.mystic.game.model.content.skill.summoning;

import org.mystic.cache.map.Region;
import org.mystic.game.GameConstants;
import org.mystic.game.model.content.dialogue.impl.ConfirmDialogue;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.impl.GroundItemHandler;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendConfig;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendNPCDialogueHead;
import org.mystic.game.model.networking.outgoing.SendRemoveInterfaces;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.GameDefinitionLoader;
import org.mystic.utility.Misc;

public class Summoning {

	private final Player player;

	private FamiliarMob familiar = null;

	private int time = -1;

	private int special = 60;

	private Task removalTask = null;

	private BOBContainer container = null;

	private boolean attack = false;

	public Summoning(Player player) {
		this.player = player;
	}

	public boolean click(int button) {
		switch (button) {
		case 3249:
		case 70098:
		case 70103:
			doSpecial();
			return true;
		case 3250:
		case 70105:
			if (familiar != null) {
				attack = (!attack);
				player.getClient().queueOutgoingPacket(new SendConfig(333, attack ? 1 : 0));
				if (attack) {
					player.send(new SendMessage("Your familiar will now attack enemies when possible."));
				} else {
					player.send(new SendMessage("Your familiar will no-longer be hostile towards anyone."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
			}
			return true;
		case 3252:
		case 70112:
			if (familiar != null) {
				if (player.getInventory().hasItemId(familiar.getData().pouch)) {
					player.getInventory().remove(familiar.getData().pouch);
					time = 40;
					renew();
					familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
				} else {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("You need another pouch to renew this familiar."));
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
			}
			return true;
		case 3253:
		case 70118:
			if (familiar == null) {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
				return true;
			}
			player.start(new ConfirmDialogue(player, new String[] { "Are you sure you want to dismiss this familiar?",
					"Any stored items will be dropped." }) {
				@Override
				public void onConfirm() {
					if (familiar != null) {
						familiar.remove();
						onFamiliarDeath();
					} else {
						player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar."));
					}
				}
			});
			return true;
		case 3251:
		case 70115:
			if (familiar != null) {
				if (!player.getSummoning().canReposition(familiar.getSize(), player.getLocation())) {
					player.getClient().queueOutgoingPacket(new SendMessage("Your familiar requires more space."));
					return true;
				}
				if (Misc.getManhattanDistance(player.getLocation(), familiar.getLocation()) > 2) {
					if (!player.getSkill().locked()) {
						player.getSkill().lock(1);
					} else {
						return true;
					}
					Location l = GameConstants.getClearAdjacentLocation(player.getLocation(), familiar.getSize());
					familiar.teleport(l != null ? l : player.getLocation());
				}
			} else {
				player.getClient().queueOutgoingPacket(new SendMessage("You do not have a familiar to call."));
			}
			return true;
		}
		return false;
	}

	public void doSpecial() {
		if (familiar == null) {
			player.send(new SendMessage("You do not have a familiar."));
			return;
		} else {
			if (familiar.getId() == 6873 || familiar.getId() == 6874) {
				player.send(new SendMessage(
						"You must use a Winter storage scroll on an item to use your familiars special attack."));
				return;
			}
			final FamiliarSpecial spec = SummoningConstants.getSpecial(familiar.getData());
			if (spec != null) {
				int scroll = SummoningConstants.getScrollForFamiliar(familiar);
				if (scroll == -1) {
					player.getClient()
							.queueOutgoingPacket(new SendMessage("This familiar's scroll could not be found."));
					return;
				}
				if ((spec.getSpecialType() == FamiliarSpecial.SpecialType.COMBAT)
						&& (familiar.getCombat().getAttacking() == null)) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You must be attacking something to use this special move!"));
					return;
				}
				if (player.getSkill().locked()) {
					return;
				}
				if (special < spec.getAmount()) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have enough special points to perform that attack."));
					return;
				}
				if (!player.getInventory().hasItemId(scroll) && !player.isGoldMember()) {
					player.getClient().queueOutgoingPacket(
							new SendMessage("You do not have the scroll required for this special move."));
					return;
				}
				if (spec.execute(player, familiar)) {
					player.getUpdateFlags().sendGraphic(new Graphic(1322));
					player.getUpdateFlags().sendAnimation(7660, 0);
					special -= spec.getAmount();
					player.getSkill().addExperience(23, spec.getExperience());
					if (!player.isGoldMember()) {
						player.getInventory().remove(scroll, 1);
					}
					player.getSkill().lock(5);
					updateSpecialAmount();
				}
			} else {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("This familiar's special attack has not been added yet."));
			}
		}
	}

	public BOBContainer getContainer() {
		return container;
	}

	public Familiar getFamiliarData() {
		return familiar != null ? familiar.getData() : null;
	}

	public String getMinutes(int ticks) {
		if ((familiar != null) && (ticks > 0)) {
			int minutes = ticks / 100;
			int seconds = (int) (ticks % 100 * 0.6D);
			return minutes + "." + (seconds < 10 ? "0" + seconds : Integer.valueOf(seconds));
		}
		return "0.0";
	}

	public int getSpecialAmount() {
		return special;
	}

	public int getTime() {
		return time;
	}

	public boolean hasFamiliar() {
		return familiar != null;
	}

	public FamiliarMob getFamiliar() {
		return familiar;
	}

	public boolean interact(Npc mob, int option) {
		if ((mob == null) || (mob.getOwner() == null) || !(mob instanceof FamiliarMob)) {
			return false;
		}
		if (((mob instanceof FamiliarMob)) && (!mob.getOwner().equals(player))) {
			player.getClient().queueOutgoingPacket(new SendMessage("That is not your familiar!"));
			return true;
		}
		if ((option == 2) && familiar.getData() != null && (familiar.getData().bobSlots > 0) && (container != null)) {
			container.open();
			return true;
		}
		return false;
	}

	public boolean isAttack() {
		return attack;
	}

	public boolean isFamilarBOB() {
		return (familiar != null) && (container != null);
	}

	public boolean isFamiliar(Entity e) {
		return (familiar != null) && (e.equals(familiar));
	}

	public void onFamiliarDeath() {
		if (familiar.getData().bobSlots > 0) {
			if (player.getInterfaceManager().hasBOBInventoryOpen()) {
				player.getClient().queueOutgoingPacket(new SendRemoveInterfaces());
			}
			for (Item i : container.getItems()) {
				if (i != null) {
					GroundItemHandler.add(i, familiar.getLocation(), player);
				}
			}
			container.clear();
			container = null;
		}
		familiar.remove();
		familiar = null;
		attack = false;
		refreshSidebar();
		player.getClient().queueOutgoingPacket(new SendString("[SUMMOfalse", 0));
	}

	public void onLogin() {
		if (player.getAttributes().get("summoningfamsave") != null) {
			summonOnLogin(player.getAttributes().getInt("summoningfamsave"));
			if (player.getAttributes().get("summoningbobinventory") != null) {
				container.setItems((Item[]) player.getAttributes().get("summoningbobinventory"));
			}
		}
		TaskQueue.queue(new Task(player, 35) {
			@Override
			public void execute() {
				if (special < 60) {
					special += 5;
					if (special > 60) {
						special = 60;
					}
					updateSpecialAmount();
				}
			}

			@Override
			public void onStop() {
			}
		});
		refreshSidebar();
	}

	public void onUpdateInventory() {
		if (familiar != null) {
			int scroll = SummoningConstants.getScrollForFamiliar(familiar);
			int am = player.getInventory().getItemAmount(scroll);
			if (player.isGoldMember()) {
				player.getClient().queueOutgoingPacket(new SendString("Inf", 18024));
			} else {
				player.getClient().queueOutgoingPacket(new SendString("" + (scroll == -1 ? 0 : am), 18024));
			}
			player.getClient().queueOutgoingPacket(new SendConfig(330, am > 0 ? 1 : 0));
		} else {
			player.getClient().queueOutgoingPacket(new SendString(" 0", 18024));
			player.getClient().queueOutgoingPacket(new SendConfig(330, 0));
		}
	}

	public void refreshSidebar() {
		if (familiar == null) {
			player.getClient().queueOutgoingPacket(new SendString("  " + getMinutes(0), 18043));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(4000, 18021));
			player.getClient().queueOutgoingPacket(new SendString("None", 18028));
			player.getClient().queueOutgoingPacket(new SendConfig(333, 0));
		} else {
			player.getClient().queueOutgoingPacket(new SendConfig(333, attack ? 1 : 0));
		}
		updateSpecialAmount();
		onUpdateInventory();
	}

	public void removeForLogout() {
		familiar.remove();
	}

	public void renew() {
		if (familiar != null) {
			if (removalTask != null) {
				removalTask.stop();
			}
			player.getClient()
					.queueOutgoingPacket(new SendString(getMinutes(time * (familiar.getData().time / 20)), 18043));
			removalTask = new Task(familiar, familiar.getData().time / 20) {
				@Override
				public void execute() {
					if ((familiar == null) || (familiar.isDead())) {
						stop();
						return;
					}
					time -= 1;
					player.getClient().queueOutgoingPacket(
							new SendString(getMinutes(time * (familiar.getData().time / 20)), 18043));
					if (time == 1) {
						player.getClient().queueOutgoingPacket(new SendMessage("Your familiar will disapear in "
								+ (int) (familiar.getData().time / 20 * 0.6D) + " seconds."));
					}
					if (time == 0) {
						familiar.getUpdateFlags()
								.sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
						setTaskDelay(4);
						return;
					}
					if (time < 0) {
						if (familiar != null) {
							familiar.remove();
							onFamiliarDeath();
							player.getClient().queueOutgoingPacket(new SendMessage("Your familiar has disappeared."));
						}
						stop();
					}
				}

				@Override
				public void onStop() {
				}
			};
			TaskQueue.queue(removalTask);
		}
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public void setTime(int time) {
		this.time = time;
	}

	/**
	 * 
	 * @param a
	 *            is the familiar hoping to be relocated
	 * @return if familiar a can be relocated
	 */
	public boolean canReposition(int size, Location location) {
		final int x = location.getX();
		final int y = location.getY();
		final int z = location.getZ();
		final Region r = Region.getRegion(x, y);
		if (r == null || r.getClips() == null || r.getShootable() == null) {
			return true;
		}
		for (int i = 0; i < size; i++) {
			int x2 = x + i;
			int y2 = y + 1;
			if (i < 1) {
				if (r.getClip(x2, y2, z) != 0) {
					return false;
				}
			} else {
				if (r.getClip(x, y, z) != 0) {
					return false;
				}
				if (r.getClip(x2, y2, z) != 0) {
					return false;
				}
				if (r.getClip(x, y2, z) != 0) {
					return false;
				}
				if (r.getClip(x2, y, z) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean summon(int id) {
		final Familiar f = SummoningConstants.getFamiliarForPouch(id);
		if (f != null) {
			if ((!GameConstants.DEV_MODE) && (!SummoningConstants.isAllowed(f))) {
				return true;
			}
			int size = GameDefinitionLoader.getNpcDefinition(f.mob).getSize();
			if (!player.getSummoning().canReposition(size, player.getLocation())) {
				player.getClient().queueOutgoingPacket(new SendMessage("Your familiar requires more space."));
				return true;
			}
			if (player.getController().equals(ControllerManager.PEST_WAITING_ROOM_CONTROLLER)
					|| player.getController().equals(ControllerManager.PEST_CONTROLLER)) {
				player.getClient().queueOutgoingPacket(new SendMessage("You cannot summon familiars here."));
				return true;
			}
			if (player.getMaxLevels()[23] < f.levelRequired) {
				player.getClient().queueOutgoingPacket(new SendMessage(
						"You need a Summoning level of " + f.levelRequired + " to summon this familiar."));
				return true;
			}
			if ((familiar != null) || (player.getPets().hasPet())) {
				player.getClient().queueOutgoingPacket(new SendMessage("You already have a familiar."));
				return true;
			}
			if (player.getLevels()[23] < f.pointsForSummon) {
				player.getClient().queueOutgoingPacket(new SendMessage("You must recharge your summoning points."));
				return true;
			}
			Location l = GameConstants.getClearAdjacentLocation(player.getLocation(),
					GameDefinitionLoader.getNpcDefinition(f.mob).getSize());
			if (l == null) {
				player.getClient().queueOutgoingPacket(new SendMessage("You must make room for your familiar!"));
				return true;
			}
			short[] tmp243_238 = player.getLevels();
			tmp243_238[23] = ((short) (tmp243_238[23] - tmp243_238[23] / 3));
			player.getSkill().update(23);
			player.getInventory().remove(id, 1, true);
			familiar = new FamiliarMob(f, player, l);
			familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
			player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(f.mob, 18021));
			player.getClient()
					.queueOutgoingPacket(new SendString(GameDefinitionLoader.getNpcDefinition(f.mob).getName(), 18028));
			player.getClient()
					.queueOutgoingPacket(new SendString(player.getSkill().getLevels()[Skills.SUMMONING] + "/"
							+ player.getSkill().getLevelForExperience(Skills.SUMMONING,
									player.getSkill().getExperience()[Skills.SUMMONING]),
							18045));
			player.getClient().queueOutgoingPacket(new SendString("[SUMMOtrue", 0));
			time = 40;
			renew();
			onUpdateInventory();
			if (f.bobSlots > 0) {
				container = new BOBContainer(player, f.bobSlots);
			} else {
				container = null;
			}
			attack = false;
			player.getClient().queueOutgoingPacket(new SendConfig(333, 0));
			return true;
		}
		return false;
	}

	public void summonOnLogin(int id) {
		final Familiar f = Familiar.forMobId(id);
		if ((f == null) || (!SummoningConstants.isAllowed(f))) {
			return;
		}
		Location l = GameConstants.getClearAdjacentLocation(player.getLocation(),
				GameDefinitionLoader.getNpcDefinition(f.mob).getSize());
		familiar = new FamiliarMob(f, player, l == null ? player.getLocation() : l);
		familiar.getUpdateFlags().sendGraphic(new Graphic(familiar.getSize() == 1 ? 1314 : 1315, 0, false));
		player.getClient().queueOutgoingPacket(new SendNPCDialogueHead(f.mob, 18021));
		player.getClient().queueOutgoingPacket(new SendString(
				player.getSkill().getLevels()[Skills.SUMMONING] + "/" + player.getSkill()
						.getLevelForExperience(Skills.SUMMONING, player.getSkill().getExperience()[Skills.SUMMONING]),
				18045));
		player.getClient()
				.queueOutgoingPacket(new SendString(GameDefinitionLoader.getNpcDefinition(f.mob).getName(), 18028));
		player.getClient().queueOutgoingPacket(new SendString("[SUMMOtrue", 0));
		renew();
		onUpdateInventory();
		if (f.bobSlots > 0) {
			container = new BOBContainer(player, f.bobSlots);
		} else {
			container = null;
		}
	}

	public void updateSpecialAmount() {
		for (int i = 0; i < 17; i++) {
			player.getClient()
					.queueOutgoingPacket(new SendConfig(334 + i, (special == 60) || (special > 3 * (i + 1)) ? 0 : 1));
		}
	}
}
