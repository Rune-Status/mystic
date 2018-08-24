package org.mystic.game.model.content.skill.summoning;

import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.impl.Attack;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.npc.NpcAbilities;
import org.mystic.game.model.entity.npc.Walking;
import org.mystic.game.model.entity.player.Player;
import org.mystic.utility.GameDefinitionLoader;

public class FamiliarMob extends Npc {

	private final Familiar familiar;

	public FamiliarMob(Familiar familiar, Player owner, Location l) {
		super(owner, null, familiar.mob, false, false, true, l);
		getFollowing().setFollow(owner);
		getFollowing().setIgnoreDistance(true);
		this.familiar = familiar;
		getCombat().getMelee().setAttack(new Attack(1, 5), new Animation(familiar.attackAnimation));
		getCombat().getMagic().setAttack(new Attack(3, 5), new Animation(familiar.attackAnimation), null, null, null);
		if (familiar.wildMob > 0) {
			getMaxLevels()[0] = ((short) familiar.attack);
			getMaxLevels()[1] = ((short) familiar.defence);
			getMaxLevels()[3] = ((short) (GameDefinitionLoader.getNpcDefinition(familiar.wildMob).getLevel() * 2));
			getLevels()[0] = ((short) familiar.attack);
			getLevels()[1] = ((short) familiar.defence);
			getLevels()[3] = ((short) (GameDefinitionLoader.getNpcDefinition(familiar.wildMob).getLevel() * 2));
		}
	}

	@Override
	public void afterCombatProcess(Entity attack) {
		if (attack.isDead()) {
			getCombat().reset();
		} else {
			NpcAbilities.executeAbility(getId(), this, attack);
		}
		getCombat().setCombatType(CombatTypes.MELEE);
	}

	@Override
	public void doAliveMobProcessing() {
		if ((inWilderness()) && (getId() != familiar.wildMob))
			transform(familiar.wildMob);
		else if ((!inWilderness()) && (getId() != familiar.mob)) {
			transform(familiar.mob);
		}
		if (getOwner().getSummoning().isAttack()) {
			if ((getOwner().getCombat().getAttacking() != null) && (inMultiArea())
					&& (getOwner().getCombat().getAttacking().inMultiArea())) {
				getCombat().setAttack(getOwner().getCombat().getAttacking());
			} else if (getCombat().getAttacking() != null) {
				getCombat().reset();
				getFollowing().setFollow(getOwner());
			}
		} else if (getCombat().getAttacking() != null) {
			getCombat().reset();
			getFollowing().setFollow(getOwner());
		}
	}

	public Familiar getData() {
		return familiar;
	}

	@Override
	public Animation getDeathAnimation() {
		return new Animation(familiar.deathAnimation);
	}

	@Override
	public int getMaxHit(CombatTypes type) {
		if (getAttributes().get("summonfammax") != null) {
			int max = getAttributes().getInt("summonfammax");
			getAttributes().remove("summonfammax");
			return max;
		}
		return getData().max;
	}

	@Override
	public void onDeath() {
		getOwner().getSummoning().onFamiliarDeath();
	}

	@Override
	public void teleport(Location p) {
		Walking.setNpcOnTile(this, false);
		getMovementHandler().getLastLocation().setAs(new Location(p.getX(), p.getY() + 1));
		getLocation().setAs(p);
		Walking.setNpcOnTile(this, true);
		setPlacement(true);
		getMovementHandler().resetMoveDirections();
		getUpdateFlags().sendGraphic(new Graphic(getSize() == 1 ? 1314 : 1315, 0, false));
	}

	@Override
	public void updateCombatType() {
	}
}
