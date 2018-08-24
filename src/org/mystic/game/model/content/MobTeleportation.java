package org.mystic.game.model.content;

import org.mystic.game.model.content.skill.magic.MagicSkill.TeleportTypes;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.networking.outgoing.SendMessage;

/**
 * Handles the teleporting of players into the abyss
 * 
 * @author Reece - http://www.rune-server.org/members/Valiant
 *
 */
public class MobTeleportation {

	/**
	 * The string sent when teleporting the player
	 */
	private static final String STRING_ZAMORAK = "Veniens! Sallakar! Rinnesset!";

	/**
	 * The string sent when teleporting the player
	 */
	private static final String STRING_WIZARD = "Seventior disthine molenko!";

	/**
	 * Teleports the player into the abyss.
	 * 
	 * @param player
	 *            the player we are teleporting
	 * @param mob
	 *            the mage teleporting the player
	 */
	public static void teleportZamorak(Player player, Npc mob) {
		mob.face(player);
		mob.getUpdateFlags().sendForceMessage(STRING_ZAMORAK);
		mob.getUpdateFlags().sendGraphic(new Graphic(343));
		mob.getUpdateFlags().sendAnimation(new Animation(1818));
		player.getMagic().teleportNoRequirements(3040, 4842, 0, TeleportTypes.WIZARD);
		player.getSkulling().skull(player);
		player.send(new SendMessage("The Mage of Zamorak telepots you deep into the abyss.."));
	}

	/**
	 * Teleports the player to their desired location
	 * 
	 * @param player
	 *            the player we are teleporting
	 * @param mob
	 *            the mage teleporting the player
	 */
	public static void teleportWizard(Player player, Npc mob, Location loc) {
		mob.face(player);
		mob.getUpdateFlags().sendForceMessage(STRING_WIZARD);
		mob.getUpdateFlags().sendGraphic(new Graphic(343));
		mob.getUpdateFlags().sendAnimation(new Animation(1818));
		player.getMagic().teleport(loc.getX(), loc.getY(), loc.getZ(), TeleportTypes.WIZARD);
		player.send(new SendMessage("The wizard teleports you to your selected destination."));
	}

}
