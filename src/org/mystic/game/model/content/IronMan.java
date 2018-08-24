package org.mystic.game.model.content;

import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;

public class IronMan {

	public static boolean isIronMan(Player player) {
		if (player.getRights().equals(Rights.IRON_MAN) || player.getRights().equals(Rights.ULTIMATE_IRON_MAN)
				|| player.getRights().equals(Rights.HARDCORE_IRON_MAN)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isUltimateIronMan(Player player) {
		if (player.getRights().equals(Rights.ULTIMATE_IRON_MAN)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHardcoreIronMan(Player player) {
		if (player.getRights().equals(Rights.HARDCORE_IRON_MAN)) {
			return true;
		} else {
			return false;
		}
	}

}
