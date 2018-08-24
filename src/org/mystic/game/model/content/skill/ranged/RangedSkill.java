package org.mystic.game.model.content.skill.ranged;

import org.mystic.game.model.content.combat.impl.Ranged;
import org.mystic.game.model.definition.RangedWeaponDefinition;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.item.BasicItemContainer;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.item.ItemContainer;
import org.mystic.game.model.entity.npc.impl.SeaTrollQueen;
import org.mystic.game.model.entity.player.EquipmentConstants;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.utility.Misc;

public class RangedSkill {

	public static final boolean requiresArrow(Player player, int id) {
		if (id == 4214 || id == 10034 || id == 10033) {
			return false;
		}
		Item weapon = player.getEquipment().getItems()[3];
		if ((weapon == null) || (weapon.getRangedDefinition() == null)
				|| (weapon.getRangedDefinition().getType() == RangedWeaponDefinition.RangedTypes.THROWN)) {
			return false;
		}
		return true;
	}

	private final Player player;

	private Item arrow = null;

	private Location arrow_location = null;

	private final ItemContainer savedArrows = new BasicItemContainer(40);

	private boolean onyxEffectActive = false;
	private boolean karilEffectActive = false;

	public RangedSkill(Player player) {
		this.player = player;
	}

	public boolean canUseRanged() {
		Item weapon = player.getEquipment().getItems()[3];
		Item ammo = player.getEquipment().getItems()[13];
		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return false;
		}
		RangedWeaponDefinition def = weapon.getRangedDefinition();
		Item[] arrows = def.getArrows();
		if ((def.getType() == RangedWeaponDefinition.RangedTypes.SHOT) && (requiresArrow(player, weapon.getId()))
				&& (arrows != null) && (arrows.length != 0)) {
			if (ammo == null) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You do not have the correct ammo to use this weapon."));
				return false;
			}
			boolean has = false;
			for (Item i : arrows) {
				if (i != null) {
					if ((ammo.equals(i)) && (ammo.getAmount() >= i.getAmount())) {
						has = true;
					}
				}
			}
			if (!has) {
				player.getClient()
						.queueOutgoingPacket(new SendMessage("You do not have the correct ammo to use this weapon."));
				return false;
			}
		}
		return true;
	}

	public void doActionsForChinchompa(Entity attacking) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon != null && weapon.getId() == 10034 || weapon.getId() == 10033) {
			attacking.getUpdateFlags().sendGraphic(Graphic.highGraphic(157, 0));
		} else {
			return;
		}
	}

	public void doActionsForDarkBow(Entity attacking) {
		Item weapon = player.getEquipment().getItems()[3];
		if ((weapon == null) || (weapon.getId() != 11235)) {
			return;
		}
		Ranged r = player.getCombat().getRanged();
		player.getSpecialAttack().isInitialized();
		r.setProjectile(new Projectile(player.getCombat().getRanged().getProjectile().getId()));
		r.getProjectile().setDelay(35);
		r.execute(attacking);
		r.setProjectile(new Projectile(player.getCombat().getRanged().getProjectile().getId()));
		player.getSpecialAttack().isInitialized();
	}

	public void doOnyxEffect(int damage) {
		if (damage > 0) {
			int max = player.getMaxLevels()[3];
			int newLvl = player.getSkill().getLevels()[3] + (int) (damage * 0.25D);
			int set = newLvl > max ? max : newLvl;
			player.getSkill().getLevels()[3] = ((byte) set);
			player.getSkill().update(3);
			player.getClient().queueOutgoingPacket(new SendMessage("You absorb some of your opponent's hitpoints."));
		}
		onyxEffectActive = false;
	}

	public void dropArrowAfterHit() {
		if ((arrow == null) || (arrow_location == null)) {
			return;
		}
		if ((arrow.getId() == 15243) || (arrow.getId() == 4740) || (arrow.getId() == 10034)
				|| (arrow.getId() == 10033)) {
			return;
		}
		if (Misc.randomNumber(2) == 0) {
			player.getGroundItems().drop(arrow, arrow_location);
		} else {
			Item cape = player.getEquipment().getItems()[1];
			if (cape != null) {
				boolean ava = cape.getId() == 10499 || cape.getId() == 10498;
				if (ava) {
					if (Misc.randomNumber(100) <= 40) {
						savedArrows.add(arrow.getId(), 1, false);
					}
				}
			}
		}
		arrow = null;
		arrow_location = null;
	}

	public void getFromAvasAccumulator() {
		for (Item i : savedArrows.getItems()) {
			if (i != null) {
				int r = player.getInventory().add(new Item(i));
				savedArrows.remove(new Item(i.getId(), r));
			}
		}
	}

	public ItemContainer getSavedArrows() {
		return savedArrows;
	}

	public boolean isKarilEffectActive() {
		return karilEffectActive;
	}

	public boolean isOnyxEffectActive() {
		return onyxEffectActive;
	}

	public void removeArrowsOnAttack() {
		final Item weapon = player.getEquipment().getItems()[3];
		final Item ammo = player.getEquipment().getItems()[13];
		final Item cape = player.getEquipment().getItems()[EquipmentConstants.CAPE_SLOT];
		if ((weapon == null) || (weapon.getRangedDefinition() == null)) {
			return;
		}
		final RangedWeaponDefinition def = weapon.getRangedDefinition();
		if (cape != null) {
			boolean ava = cape.getId() == 10499 || cape.getId() == 10498;
			int chance = Misc.random(100);
			if (ava) {
				if (70 >= chance) {
					if ((def != null) && (Misc.randomNumber(100) <= 40)) {
						if ((def.getType() == RangedWeaponDefinition.RangedTypes.SHOT) && (ammo != null)) {
							savedArrows.add(ammo.getId(), 1, false);
						} else if ((def.getType() == RangedWeaponDefinition.RangedTypes.THROWN) && (weapon != null)) {
							savedArrows.add(weapon.getId(), 1, false);
						}
					}
					return;
				}
			}
		}
		switch (def.getType()) {
		case SHOT:
			Item[] arrows = weapon.getRangedDefinition().getArrows();
			for (Item i : arrows) {
				if (i != null && ammo != null) {
					if ((ammo.equals(i)) && (ammo.getAmount() >= i.getAmount())) {
						arrow = new Item(i.getId(), i.getAmount());
						break;
					}
				}
			}
			if (ammo != null && arrow != null) {
				ammo.remove(arrow.getAmount());
				if (ammo.getAmount() == 0) {
					player.getEquipment().unequip(13);
					player.getClient().queueOutgoingPacket(new SendMessage("You have run out of ammo."));
				} else {
					player.getEquipment().update(13);
				}
				Entity attack = player.getCombat().getAttacking();
				arrow_location = (attack == null ? player.getLocation() : attack.getLocation());
				if (attack != null && attack instanceof SeaTrollQueen) {
					arrow_location = new Location(2344, 3699);
				}
			}
			break;
		case THROWN:
			if (weapon.getAmount() == 1) {
				player.getClient().queueOutgoingPacket(new SendMessage("You threw the last of your ammo!"));
			}
			weapon.remove(1);
			if (weapon.getAmount() == 0)
				player.getEquipment().unequip(3);
			else {
				player.getEquipment().update(3);
			}
			arrow = new Item(weapon.getId(), 1);
			Entity attack = player.getCombat().getAttacking();
			arrow_location = (attack == null ? player.getLocation() : attack.getLocation());
			break;
		}
	}

	public void setKarilEffectActive(boolean karilEffectActive) {
		this.karilEffectActive = karilEffectActive;
	}

	public void setOnyxEffectActive(boolean onyxEffectActive) {
		this.onyxEffectActive = onyxEffectActive;
	}
}
