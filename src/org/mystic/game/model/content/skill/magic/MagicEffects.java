package org.mystic.game.model.content.skill.magic;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.content.skill.magic.effects.BindEffect;
import org.mystic.game.model.content.skill.magic.effects.BloodBarrageEffect;
import org.mystic.game.model.content.skill.magic.effects.BloodBlitzEffect;
import org.mystic.game.model.content.skill.magic.effects.BloodBurstEffect;
import org.mystic.game.model.content.skill.magic.effects.BloodRushEffect;
import org.mystic.game.model.content.skill.magic.effects.ClawsOfGuthixEffect;
import org.mystic.game.model.content.skill.magic.effects.EntangleEffect;
import org.mystic.game.model.content.skill.magic.effects.FlamesOfZamorakEffect;
import org.mystic.game.model.content.skill.magic.effects.IceBarrageEffect;
import org.mystic.game.model.content.skill.magic.effects.IceBlitzEffect;
import org.mystic.game.model.content.skill.magic.effects.IceBurstEffect;
import org.mystic.game.model.content.skill.magic.effects.IceRushEffect;
import org.mystic.game.model.content.skill.magic.effects.SaradominStrikeEffect;
import org.mystic.game.model.content.skill.magic.effects.ShadowBarrageEffect;
import org.mystic.game.model.content.skill.magic.effects.ShadowBlitzEffect;
import org.mystic.game.model.content.skill.magic.effects.ShadowBurstEffect;
import org.mystic.game.model.content.skill.magic.effects.ShadowRushEffect;
import org.mystic.game.model.content.skill.magic.effects.SmokeBarrageEffect;
import org.mystic.game.model.content.skill.magic.effects.SmokeBlitzEffect;
import org.mystic.game.model.content.skill.magic.effects.SmokeBurstEffect;
import org.mystic.game.model.content.skill.magic.effects.SmokeRushEffect;
import org.mystic.game.model.content.skill.magic.effects.SnareEffect;
import org.mystic.game.model.content.skill.magic.effects.TeleBlockEffect;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.player.Player;

public class MagicEffects {

	private static Map<Integer, CombatEffect2> effects = new HashMap<Integer, CombatEffect2>();

	static {
		effects.put(Integer.valueOf(12939), new SmokeRushEffect());
		effects.put(Integer.valueOf(12987), new ShadowRushEffect());
		effects.put(Integer.valueOf(12901), new BloodRushEffect());
		effects.put(Integer.valueOf(12861), new IceRushEffect());
		effects.put(Integer.valueOf(12963), new SmokeBurstEffect());
		effects.put(Integer.valueOf(13011), new ShadowBurstEffect());
		effects.put(Integer.valueOf(12919), new BloodBurstEffect());
		effects.put(Integer.valueOf(12881), new IceBurstEffect());
		effects.put(Integer.valueOf(12951), new SmokeBlitzEffect());
		effects.put(Integer.valueOf(12999), new ShadowBlitzEffect());
		effects.put(Integer.valueOf(12911), new BloodBlitzEffect());
		effects.put(Integer.valueOf(12871), new IceBlitzEffect());
		effects.put(Integer.valueOf(12975), new SmokeBarrageEffect());
		effects.put(Integer.valueOf(13023), new ShadowBarrageEffect());
		effects.put(Integer.valueOf(12929), new BloodBarrageEffect());
		effects.put(Integer.valueOf(12891), new IceBarrageEffect());
		effects.put(Integer.valueOf(1190), new SaradominStrikeEffect());
		effects.put(Integer.valueOf(1191), new ClawsOfGuthixEffect());
		effects.put(Integer.valueOf(1192), new FlamesOfZamorakEffect());
		effects.put(Integer.valueOf(1572), new BindEffect());
		effects.put(Integer.valueOf(1582), new SnareEffect());
		effects.put(Integer.valueOf(1592), new EntangleEffect());
		effects.put(Integer.valueOf(12445), new TeleBlockEffect());
	}

	public static void doMagicEffects(Player attacker, Entity attacked, int spellId) {
		CombatEffect2 effect = effects.get(Integer.valueOf(spellId));
		if (effect == null) {
			return;
		} else {
			effect.execute(attacker, attacked);
		}
	}
}
