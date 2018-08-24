package org.mystic.game.model.entity.npc.impl;

import org.mystic.game.World;
import org.mystic.game.model.content.combat.Combat.CombatTypes;
import org.mystic.game.model.content.combat.Hit;
import org.mystic.game.model.content.skill.magic.MagicConstants;
import org.mystic.game.model.definition.NpcCombatDefinition;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.PlayerConstants;
import org.mystic.game.task.Task;
import org.mystic.game.task.TaskQueue;
import org.mystic.utility.GameDefinitionLoader;

public class GelatinnothMother extends Npc {

	public static final int[] STAGES = { 3497, 3498, 3499, 3500, 3501, 3502 };

	private byte stage = 0;

	public GelatinnothMother(Location location, Player owner) {
		super(3497, false, location, owner, false, false, null);
		TaskQueue.queue(new Task(this, 20) {
			@Override
			public void execute() {
				GelatinnothMother tmp4_1 = GelatinnothMother.this;
				tmp4_1.stage = ((byte) (tmp4_1.stage + 1));
				if (stage == GelatinnothMother.STAGES.length) {
					stage = 0;
				}
				transform(GelatinnothMother.STAGES[stage]);
			}

			@Override
			public void onStop() {
			}
		});
	}

	@Override
	public int getAffectedDamage(Hit hit) {
		if (hit.getAttacker() != null && !hit.getAttacker().isNpc()) {
			Player p = World.getPlayers()[hit.getAttacker().getIndex()];
			if (p != null && PlayerConstants.isOwner(p)) {
				return hit.getDamage();
			}
		}
		if ((hit.getAttacker() != null) && (!hit.getAttacker().isNpc())) {
			Player p = org.mystic.game.World.getPlayers()[hit.getAttacker().getIndex()];
			if (p != null) {
				if (getId() == STAGES[0]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC) && (MagicConstants.forId(
							p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.WIND)) {
						return hit.getDamage();
					}
					return 0;
				}
				if (getId() == STAGES[1]) {
					if (p.getCombat().getCombatType() == CombatTypes.MELEE) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[2]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC) && (MagicConstants.forId(
							p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.WATER)) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[3]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC) && (MagicConstants.forId(
							p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.FIRE)) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[4]) {
					if (p.getCombat().getCombatType() == CombatTypes.RANGED) {
						return hit.getDamage();
					}

					return 0;
				}
				if (getId() == STAGES[5]) {
					if ((p.getCombat().getCombatType() == CombatTypes.MAGIC) && (MagicConstants.forId(
							p.getMagic().getSpellCasting().getCurrentSpellId()) == MagicConstants.SpellType.EARTH)) {
						return hit.getDamage();
					}

					return 0;
				}
			}
		}
		return hit.getDamage();
	}

	@Override
	public NpcCombatDefinition getCombatDefinition() {
		return GameDefinitionLoader.getNpcCombatDefinition(STAGES[0]);
	}

}