package org.mystic.game.model.entity.npc;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.combat.CombatEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.npc.abilities.BarrelchestAbility;
import org.mystic.game.model.entity.npc.abilities.BorkAbility;
import org.mystic.game.model.entity.npc.abilities.CorporealBeastAbility;
import org.mystic.game.model.entity.npc.abilities.HobgoblinGeomancerAbility;
import org.mystic.game.model.entity.npc.abilities.IcyBonesAbility;
import org.mystic.game.model.entity.npc.abilities.JadAbility;
import org.mystic.game.model.entity.npc.abilities.TormentedDemonAbility;
import org.mystic.game.model.entity.npc.abilities.UnholyCurseBearerAbility;

public class NpcAbilities {

	private static final Map<Integer, CombatEffect> abilities = new HashMap<Integer, CombatEffect>();

	static {
		abilities.put(Integer.valueOf(5666), new BarrelchestAbility());
		abilities.put(Integer.valueOf(2745), new JadAbility());
		abilities.put(Integer.valueOf(10057), new IcyBonesAbility());
		abilities.put(Integer.valueOf(10127), new UnholyCurseBearerAbility());
		abilities.put(Integer.valueOf(10072), new HobgoblinGeomancerAbility());
		abilities.put(Integer.valueOf(7133), new BorkAbility());
		abilities.put(Integer.valueOf(8133), new CorporealBeastAbility());
		abilities.put(Integer.valueOf(8351), new TormentedDemonAbility());
		abilities.put(Integer.valueOf(8350), new TormentedDemonAbility());
		abilities.put(Integer.valueOf(8349), new TormentedDemonAbility());
	}

	public static void executeAbility(int id, Npc mob, Entity a) {
		CombatEffect e = abilities.get(Integer.valueOf(id));
		if (e != null) {
			e.execute(mob, a);
		}
	}

}
