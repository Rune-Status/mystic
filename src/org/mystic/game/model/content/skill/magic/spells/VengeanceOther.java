package org.mystic.game.model.content.skill.magic.spells;

import org.mystic.game.model.content.IronMan;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.controllers.ControllerManager;
import org.mystic.game.model.networking.outgoing.SendMessage;

public class VengeanceOther {

	private final static Animation animation = new Animation(4411);

	private final static Graphic graphic = new Graphic(725);

	private final static Item rune1 = new Item(9075, 4);

	private final static Item rune2 = new Item(557, 10);

	private final static Item rune3 = new Item(560, 2);

	private static boolean hasReqs(Player caster) {
		if (caster.getInventory().playerHasItem(rune1) && caster.getInventory().playerHasItem(rune2)
				&& caster.getInventory().playerHasItem(rune3)) {
			return true;
		}
		caster.send(new SendMessage("You don't have all the required runes to cast this spell."));
		return false;
	}

	public static boolean execute(Player caster, Player other) {
		if (caster.inDuelArena() || other.inDuelArena()) {
			caster.send(new SendMessage("You can't cast that spell here."));
			return false;
		}
		if (IronMan.isIronMan(other)) {
			caster.send(new SendMessage("This player is an Iron man and can't accept your assistance."));
			return false;
		}
		if (caster.getController().equals(ControllerManager.DUEL_STAKE_CONTROLLER)
				|| caster.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)
				|| caster.getController().equals(ControllerManager.DUELING_CONTROLLER)) {
			caster.send(new SendMessage("You can't cast this spell here."));
			return false;
		} else if (other.getController().equals(ControllerManager.DUEL_STAKE_CONTROLLER)
				|| other.getController().equals(ControllerManager.DUEL_ARENA_CONTROLLER)
				|| other.getController().equals(ControllerManager.DUELING_CONTROLLER)) {
			return false;
		}
		if (System.currentTimeMillis() - caster.getMagic().getLastVengeance() < 30000L) {
			caster.send(new SendMessage("You can only cast Vengeance once every 30 seconds."));
			return false;
		} else if (other.getAcceptAid() == 0) {
			other.send(new SendMessage(caster.getDisplay() + " tried to give you vengeance but your aid is off."));
			caster.send(new SendMessage("This player does not have their accept aid on."));
			return false;
		} else if (other.getMagic().isVengeanceActive()) {
			caster.send(new SendMessage("This player already has the power of vengeance."));
			return false;
		} else if (hasReqs(caster)) {
			caster.getMagic().setLastVengeance(System.currentTimeMillis());
			caster.getUpdateFlags().sendFaceToDirection(other.getLocation());
			caster.getSkill().addExperience(Skills.MAGIC, 112);
			caster.getInventory().remove(rune1);
			caster.getInventory().remove(rune2);
			caster.getInventory().remove(rune3);
			caster.getInventory().update();
			caster.getUpdateFlags().sendAnimation(animation);
			caster.send(new SendMessage("You cast Vegeance upon " + other.getUsername() + "."));
			other.getUpdateFlags().sendGraphic(graphic);
			other.send(new SendMessage("You have been granted the power of vengeance."));
			other.getMagic().activateVengeance();
			return true;
		}
		return false;
	}

}