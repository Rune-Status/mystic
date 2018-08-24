package org.mystic.game.model.networking.packet.command.impl;

import java.util.Random;

import org.mystic.game.model.content.skill.SkillManager;
import org.mystic.game.model.content.skill.magic.MagicSkill.SpellBookTypes;
import org.mystic.game.model.entity.item.Item;
import org.mystic.game.model.entity.player.Player;
import org.mystic.game.model.entity.player.Rights;
import org.mystic.game.model.networking.packet.command.Command;

public class HybridSetupCommand implements Command {

	private static int[] capes = { 2412, 2413, 2414 };

	private static int[] torso = { 4091, 4101, 4111 };

	private static int[] legs = { 4093, 4103, 4113 };

	private static int[] staff = { 4675, 6914 };

	private static int book[] = { 3842 };

	private static int[] dragonhideBody = { 2503, 10370, 10386, 10378 };

	private static int[] plateBody = { 10551 };

	private static int[] platelegs = { 1079, 6130 };

	public void spawn(Player player) {
		player.getEquipment().clear();
		player.getEquipment().getItems()[1] = new Item(capes[new Random().nextInt(capes.length)]);
		player.getEquipment().getItems()[4] = new Item(torso[new Random().nextInt(torso.length)]);
		player.getEquipment().getItems()[7] = new Item(legs[new Random().nextInt(legs.length)]);
		player.getEquipment().getItems()[3] = new Item(staff[new Random().nextInt(staff.length)]);
		player.getEquipment().getItems()[0] = new Item(10828);
		player.getEquipment().getItems()[10] = new Item(3105);
		player.getEquipment().getItems()[2] = new Item(1712);
		player.getEquipment().getItems()[9] = new Item(7462);
		player.getEquipment().getItems()[5] = new Item(book[new Random().nextInt(book.length)]);
		player.getInventory().clear();
		player.getInventory().add(new Item(4151));
		player.getInventory().add(new Item(dragonhideBody[new Random().nextInt(dragonhideBody.length)]));
		player.getInventory().add(new Item(5698));
		player.getInventory().add(new Item(20087));
		player.getInventory().add(new Item(20072));
		player.getInventory().add(new Item(6570));
		player.getInventory().add(new Item(15272));
		player.getInventory().add(new Item(3024));
		player.getInventory().add(new Item(plateBody[new Random().nextInt(plateBody.length)]));
		player.getInventory().add(new Item(platelegs[new Random().nextInt(platelegs.length)]));
		player.getInventory().add(new Item(15272));
		player.getInventory().add(new Item(3024));
		player.getInventory().add(new Item(15272, 3));
		player.getInventory().add(new Item(3024));
		player.getInventory().add(new Item(15272, 3));
		player.getInventory().add(new Item(6685));
		player.getInventory().add(new Item(15272, 3));
		player.getInventory().add(new Item(6685));
		player.getInventory().add(new Item(15272, 2));
		player.getInventory().add(new Item(20085));
		player.getInventory().add(new Item(8013, 10));
		player.getInventory().update();
		player.getEquipment().onLogin();
		player.getMagic().setSpellBookType(SpellBookTypes.ANCIENT);
		player.getMagic().setMagicBook(12855);
		for (int i = 0; i < 25; i++) {
			player.getLevels()[i] = 99;
			player.getMaxLevels()[i] = 99;
			player.getSkill().getExperience()[i] = SkillManager.EXP_FOR_LEVEL[98];
			player.getSkill().update(i);
		}
		player.getSkill().updateCombatLevel();
		player.setAppearanceUpdateRequired(true);
	}

	@Override
	public void handleCommand(Player player, String command) {
		spawn(player);
	}

	@Override
	public boolean meetsRequirements(Player player) {
		return true;
	}

	@Override
	public Rights rightsRequired() {
		return Rights.OWNER;
	}
}
