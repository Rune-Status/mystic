package org.mystic.game.model.content.combat.special;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.content.combat.CombatEffect2;
import org.mystic.game.model.content.combat.special.effects.AbyssalWhipEffect;
import org.mystic.game.model.content.combat.special.effects.AncientMaceEffect;
import org.mystic.game.model.content.combat.special.effects.BandosGodswordEffect;
import org.mystic.game.model.content.combat.special.effects.BarrelchestAnchorEffect;
import org.mystic.game.model.content.combat.special.effects.DragonScimitarEffect;
import org.mystic.game.model.content.combat.special.effects.DragonSpearEffect;
import org.mystic.game.model.content.combat.special.effects.SaradominGodswordEffect;
import org.mystic.game.model.content.combat.special.effects.SeercullEffect;
import org.mystic.game.model.content.combat.special.effects.StaffOfLightEffect;
import org.mystic.game.model.content.combat.special.effects.StatiusWarhammerEffect;
import org.mystic.game.model.content.combat.special.effects.ZamorakGodswordEffect;
import org.mystic.game.model.content.combat.special.impl.AbyssalWhipSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.AnchorSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.AncientMaceSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.ArmadylGodswordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.BandosGodswordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DarkBowSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonClawsSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonDaggerSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonHalberdSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonLongswordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonMaceSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonScimitarSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.DragonSpearSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.GraniteMaulSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.HandCannonSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.MagicShortbowSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.MorrigansAxeSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.MorrigansJavelinSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.SaradominGodswordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.SaradominSwordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.StaffOfLightSpecial;
import org.mystic.game.model.content.combat.special.impl.StatiusWarhammerSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.VestaLongswordSpecialAttack;
import org.mystic.game.model.content.combat.special.impl.ZamorakGodswordSpecialAttack;
import org.mystic.game.model.entity.Entity;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;
import org.mystic.game.model.networking.outgoing.SendString;
import org.mystic.game.model.networking.outgoing.SendUpdateSpecialBar;

public class SpecialAttackHandler {

	private static final Map<Integer, Special> specials = new HashMap<Integer, Special>();

	private static final Map<Integer, CombatEffect2> effects = new HashMap<Integer, CombatEffect2>();

	private static void add(int weaponId, CombatEffect2 effect) {
		effects.put(Integer.valueOf(weaponId), effect);
	}

	private static void add(int weaponId, Special special) {
		specials.put(Integer.valueOf(weaponId), special);
	}

	static {

		add(15486, new StaffOfLightSpecial());
		add(22207, new StaffOfLightSpecial());
		add(22209, new StaffOfLightSpecial());
		add(22211, new StaffOfLightSpecial());
		add(22213, new StaffOfLightSpecial());
		add(1215, new DragonDaggerSpecialAttack());
		add(1231, new DragonDaggerSpecialAttack());
		add(5680, new DragonDaggerSpecialAttack());
		add(5698, new DragonDaggerSpecialAttack());
		add(4587, new DragonScimitarSpecialAttack());
		add(1305, new DragonLongswordSpecialAttack());
		add(1434, new DragonMaceSpecialAttack());
		add(10887, new AnchorSpecialAttack());
		add(4153, new GraniteMaulSpecialAttack());
		add(13902, new StatiusWarhammerSpecialAttack());
		add(13904, new StatiusWarhammerSpecialAttack());
		add(13926, new StatiusWarhammerSpecialAttack());
		add(13928, new StatiusWarhammerSpecialAttack());
		add(13899, new VestaLongswordSpecialAttack());
		add(13901, new VestaLongswordSpecialAttack());
		add(11694, new ArmadylGodswordSpecialAttack());
		add(11700, new ZamorakGodswordSpecialAttack());
		add(11700, new ZamorakGodswordEffect());
		add(11696, new BandosGodswordSpecialAttack());
		add(11698, new SaradominGodswordSpecialAttack());
		add(3204, new DragonHalberdSpecialAttack());
		add(861, new MagicShortbowSpecialAttack());
		add(859, new MagicShortbowSpecialAttack());
		add(11235, new DarkBowSpecialAttack());
		add(14484, new DragonClawsSpecialAttack());
		add(1249, new DragonSpearSpecialAttack());
		add(13883, new MorrigansAxeSpecialAttack());
		add(13879, new MorrigansJavelinSpecialAttack());
		add(13882, new MorrigansJavelinSpecialAttack());
		add(1249, new DragonSpearEffect());
		add(11696, new BandosGodswordEffect());
		add(4587, new DragonScimitarEffect());
		add(11698, new SaradominGodswordEffect());
		add(10887, new BarrelchestAnchorEffect());
		add(6724, new SeercullEffect());
		add(20074, new StaffOfLightEffect());
		add(20076, new StaffOfLightEffect());
		add(13902, new StatiusWarhammerEffect());
		add(13926, new StatiusWarhammerEffect());
		add(13928, new StatiusWarhammerEffect());
		add(11061, new AncientMaceSpecialAttack());
		add(11061, new AncientMaceEffect());
		add(4151, new AbyssalWhipSpecialAttack());
		add(4151, new AbyssalWhipEffect());
		add(21371, new AbyssalWhipSpecialAttack());
		add(21371, new AbyssalWhipEffect());
		add(15442, new AbyssalWhipSpecialAttack());
		add(15442, new AbyssalWhipEffect());
		add(15441, new AbyssalWhipSpecialAttack());
		add(15441, new AbyssalWhipEffect());
		add(15444, new AbyssalWhipSpecialAttack());
		add(15444, new AbyssalWhipEffect());
		add(15241, new HandCannonSpecialAttack());
		add(11730, new SaradominSwordSpecialAttack());

	}

	public static void executeSpecialEffect(Player player, Entity attacked) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null) {
			return;
		}
		CombatEffect2 effect = effects.get(Integer.valueOf(weapon.getId()));
		if (effect == null) {
			return;
		}
		effect.execute(player, attacked);
	}

	public static void handleSpecialAttack(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null) {
			return;
		}
		Special special = specials.get(Integer.valueOf(weapon.getId()));
		if (special == null) {
			return;
		}
		if (special.checkRequirements(player)) {
			player.getAchievements().incr(player, "Use a Special attack");
			player.getAchievements().incr(player, "Use 1,000 Special attacks");
			special.handleAttack(player);
			player.getSpecialAttack().deduct(special.getSpecialAmountRequired());
		}
	}

	public static boolean hasSpecialAmount(Player player) {
		Item weapon = player.getEquipment().getItems()[3];
		if (weapon == null) {
			return true;
		}
		Special special = specials.get(Integer.valueOf(weapon.getId()));
		if (special == null) {
			return true;
		}
		if (player.getSpecialAttack().getAmount() < special.getSpecialAmountRequired()) {
			player.send(new SendMessage("You do not have enough special attack to do that."));
			return false;
		}
		return true;
	}

	public static void updateSpecialAmount(Player player, int id, int amount) {
		int specialCheck = 100;
		for (int i = 0; i < 10; i++) {
			id--;
			player.send(new SendUpdateSpecialBar(amount >= specialCheck ? 500 : 0, id));
			specialCheck -= 10;
		}
	}

	public static void updateSpecialBarText(Player player, int id, int amount, boolean init) {
		player.send(new SendString(init ? "@yel@Special Attack " + "(" + player.getSpecialAttack().getAmount() + "%)"
				: "@bla@Special Attack " + "(" + player.getSpecialAttack().getAmount() + "%)", id));
	}
}
