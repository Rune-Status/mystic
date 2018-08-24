package org.mystic.game.model.content.combat.formula;

import java.util.Random;

import org.mystic.game.model.definition.NpcCombatDefinition;
import org.mystic.game.model.entity.Entity;
import org.mystic.utility.GameDefinitionLoader;

public class FormulaData {

	public static final Random r = new Random(System.currentTimeMillis());

	public static double getChance(double attack, double defence) {
		double A = Math.floor(attack);
		double D = Math.floor(defence);
		double chance = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
		chance = chance > 0.9999 ? 0.9999 : chance < 0.0001 ? 0.0001 : chance;
		return chance;
	}

	public static NpcCombatDefinition getCombatDefinition(Entity entity) {
		return GameDefinitionLoader.getNpcCombatDefinition(entity.getMob().getId());
	}

	public static boolean isAccurateHit(double chance) {
		return r.nextDouble() <= chance;
	}

}