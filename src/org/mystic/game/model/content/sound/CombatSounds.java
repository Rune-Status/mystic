package org.mystic.game.model.content.sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;

/**
 * Manages the sending of combat sounds
 * 
 * @author Vali - http://www.rune-server.org/members/Valiant
 * @since December 22nd, 2016.
 */
public enum CombatSounds {

	STAFF(1387, 394, 0, 5),

	STAFF1(772, 394, 0, 5),

	STAFF2(1381, 394, 0, 5),

	STAFF3(1383, 394, 0, 5),

	STAFF4(1385, 394, 0, 5),

	STAFF5(1389, 394, 0, 5),

	STAFF6(1391, 394, 0, 5),

	STAFF7(1393, 394, 0, 5),

	STAFF8(1395, 394, 0, 5),

	STAFF9(1397, 394, 0, 5),

	STAFF10(1399, 394, 0, 5),

	STAFF11(1401, 394, 0, 5),

	STAFF12(1403, 394, 0, 5),

	STAFF13(1405, 394, 0, 5),

	STAFF14(1407, 394, 0, 5),

	STAFF15(1409, 394, 0, 5),

	STAFF16(9100, 394, 0, 5),

	STAFF17(4675, 394, 0, 5),

	STAFF18(15486, 394, 0, 5),

	STAFF19(15487, 394, 0, 5),

	STAFF20(22207, 394, 0, 5),

	STAFF21(22209, 394, 0, 5),

	STAFF22(22211, 394, 0, 5),

	STAFF23(22213, 394, 0, 5),

	STAFF24(6563, 394, 0, 5),

	STAFF25(4710, 394, 0, 5),

	STAFF26(6726, 394, 0, 5),

	STAFF27(9087, 394, 0, 5),

	STAFF28(13867, 394, 0, 5),

	DHAROKS_AXE_ATTACK(4718, 1057, 0, 10),

	TORAG_HAMMER_ATTACK(4747, 1062, 0, 18),

	TORAG_HAMMER_ATTACK_100(4958, 1062, 0, 18),

	TORAG_HAMMER_ATTACK_75(4959, 1062, 0, 18),

	TORAG_HAMMER_ATTACK_50(4960, 1062, 0, 18),

	TORAG_HAMMER_ATTACK_25(4961, 1062, 0, 18),

	GRANITE_MAUL_ATTACK(4153, 1079, 0, 10),

	MAGIC_SHORT_BOW_RAPID(861, 370, 0, 10),

	YEW_SHORT_BOW_RAPID(857, 370, 0, 10),

	MAPLE_SHORT_BOW_RAPID(853, 370, 0, 10),

	WILLOW_SHORT_BOW_RAPID(849, 370, 0, 10),

	OAK_SHORT_BOW_RAPID(843, 370, 0, 10),

	SHORT_BOW_RAPID(841, 370, 0, 10),

	BRONZE_SCIMITAR_SLASH(1321, 396, 0, 10),

	IRON_SCIMITAR_SLASH(1323, 396, 0, 10),

	STEEL_SCIMITAR_SLASH(1325, 396, 0, 10),

	MITHRIL_SCIMITAR_SLASH(1329, 396, 0, 10),

	ADAMANT_SCIMITAR_SLASH(1331, 396, 0, 10),

	RUNE_SCIMITAR_SLASH(1333, 396, 0, 10),

	DRAGON_SCIMITAR_SLASH(4587, 396, 0, 10),

	WHIP_ATTACK_1(15441, 1080, 0, 10),

	WHIP_ATTACK_2(15442, 1080, 0, 10),

	WHIP_ATTACK_3(15443, 1080, 0, 10),

	WHIP_ATTACK_4(15444, 1080, 0, 10),

	WHIP_ATTACK_5(21371, 1080, 0, 10),

	WHIP_ATTACK_6(21372, 1080, 0, 10),

	WHIP_ATTACK_7(21373, 1080, 0, 10),

	WHIP_ATTACK_8(21374, 1080, 0, 10),

	WHIP_ATTACK_9(21375, 1080, 0, 10),

	WHIP_ATTACK(4151, 1080, 0, 10);

	private final int weaponId, soundId, type, delay;

	private static Map<Integer, CombatSounds> sound = new HashMap<Integer, CombatSounds>();

	static {
		for (CombatSounds data : values()) {
			sound.put(Integer.valueOf(data.getWeaponId()), data);
		}
	}

	public static CombatSounds forId(int id) {
		return sound.get(Integer.valueOf(id));
	}

	CombatSounds(final int weaponId, final int soundId, final int type, final int delay) {
		this.weaponId = weaponId;
		this.soundId = soundId;
		this.type = type;
		this.delay = delay;
	}

	public final int getSound() {
		return soundId;
	}

	public final int getWeaponId() {
		return weaponId;
	}

	public final int getType() {
		return type;
	}

	public final int getDelay() {
		return delay;
	}

	public static void executeAttack(Player player, Item weapon) {
		if (Objects.nonNull(weapon)) {
			CombatSounds data = CombatSounds.forId(weapon.getId());
			if (Objects.nonNull(data)) {
				SoundPlayer.play(player, data.getSound(), data.getType(), data.getDelay());
			}
		}
	}

}