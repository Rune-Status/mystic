package org.mystic.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mystic.cache.map.Region;
import org.mystic.game.model.content.shopping.Shop;
import org.mystic.game.model.content.skill.Skills;
import org.mystic.game.model.definition.CombatSpellDefinition;
import org.mystic.game.model.definition.EquipmentDefinition;
import org.mystic.game.model.definition.ItemBonusDefinition;
import org.mystic.game.model.definition.ItemDefinition;
import org.mystic.game.model.definition.NpcCombatDefinition;
import org.mystic.game.model.definition.NpcDefinition;
import org.mystic.game.model.definition.NpcSpawnDefinition;
import org.mystic.game.model.definition.RangedStrengthDefinition;
import org.mystic.game.model.definition.RangedWeaponDefinition;
import org.mystic.game.model.definition.ShopDefinition;
import org.mystic.game.model.definition.SpecialAttackDefinition;
import org.mystic.game.model.definition.WeaponDefinition;
import org.mystic.game.model.entity.npc.Npc;
import org.mystic.game.model.entity.player.PlayerConstants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

public class GameDefinitionLoader {

	private static XStream xStream = new XStream(new Sun14ReflectionProvider());
	private static int[][] itemValues = new int[PlayerConstants.MAX_ITEM_COUNT][];

	private static Map<Integer, byte[][]> itemRequirements = new HashMap<Integer, byte[][]>();
	private static Map<Integer, ItemDefinition> itemDefinitions = new HashMap<Integer, ItemDefinition>();
	private static Map<Integer, NpcDefinition> npcDefinitions = new HashMap<Integer, NpcDefinition>();
	private static Map<Integer, SpecialAttackDefinition> specialAttackDefinitions = new HashMap<Integer, SpecialAttackDefinition>();
	private static Map<Integer, RangedWeaponDefinition> rangedWeaponDefinitions = new HashMap<Integer, RangedWeaponDefinition>();
	private static Map<Integer, WeaponDefinition> weaponDefinitions = new HashMap<Integer, WeaponDefinition>();
	private static Map<Integer, EquipmentDefinition> equipmentDefinitions = new HashMap<Integer, EquipmentDefinition>();
	private static Map<Integer, ItemBonusDefinition> itemBonusDefinitions = new HashMap<Integer, ItemBonusDefinition>();
	private static Map<Integer, CombatSpellDefinition> combatSpellDefinitions = new HashMap<Integer, CombatSpellDefinition>();
	private static Map<Integer, NpcCombatDefinition> npcCombatDefinitions = new HashMap<Integer, NpcCombatDefinition>();
	private static Map<Integer, RangedStrengthDefinition> rangedStrengthDefinitions = new HashMap<Integer, RangedStrengthDefinition>();

	static {

		xStream.alias("location", org.mystic.game.model.entity.Location.class);
		xStream.alias("item", org.mystic.game.model.entity.item.Item.class);
		xStream.alias("projectile", org.mystic.game.model.entity.Projectile.class);
		xStream.alias("graphic", org.mystic.game.model.entity.Graphic.class);
		xStream.alias("animation", org.mystic.game.model.entity.Animation.class);

		xStream.alias("NpcCombatDefinition", org.mystic.game.model.definition.NpcCombatDefinition.class);
		xStream.alias("skill", org.mystic.game.model.definition.NpcCombatDefinition.Skill.class);
		xStream.alias("melee", org.mystic.game.model.definition.NpcCombatDefinition.Melee.class);
		xStream.alias("magic", org.mystic.game.model.definition.NpcCombatDefinition.Magic.class);
		xStream.alias("ranged", org.mystic.game.model.definition.NpcCombatDefinition.Ranged.class);

		xStream.alias("ItemDefinition", org.mystic.game.model.definition.ItemDefinition.class);
		xStream.alias("ShopDefinition", org.mystic.game.model.definition.ShopDefinition.class);
		xStream.alias("WeaponDefinition", org.mystic.game.model.definition.WeaponDefinition.class);
		xStream.alias("SpecialAttackDefinition", org.mystic.game.model.definition.SpecialAttackDefinition.class);
		xStream.alias("RangedWeaponDefinition", org.mystic.game.model.definition.RangedWeaponDefinition.class);
		xStream.alias("RangedStrengthDefinition", org.mystic.game.model.definition.RangedStrengthDefinition.class);
		xStream.alias("ItemBonusDefinition", org.mystic.game.model.definition.ItemBonusDefinition.class);
		xStream.alias("CombatSpellDefinition", org.mystic.game.model.definition.CombatSpellDefinition.class);
		xStream.alias("NpcDefinition", org.mystic.game.model.definition.NpcDefinition.class);
		xStream.alias("NpcSpawnDefinition", org.mystic.game.model.definition.NpcSpawnDefinition.class);
		xStream.alias("EquipmentDefinition", org.mystic.game.model.definition.EquipmentDefinition.class);

	}

	public static CombatSpellDefinition getCombatSpellDefinition(int id) {
		return combatSpellDefinitions.get(id);
	}

	public static EquipmentDefinition getEquipmentDefinition(int id) {
		return equipmentDefinitions.get(id);
	}

	public static int getHighAlchemyValue(int id) {
		if (itemValues[id] == null || itemValues[id][1] == 0) {
			return itemDefinitions.get(id).getValue() / 8;
		}
		if (itemValues[id][1] == -1
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][1] == -1) {
			return 0;
		}
		return itemValues[id][1];
	}

	public static ItemBonusDefinition getItemBonusDefinition(int i) {
		return itemBonusDefinitions.get(i);
	}

	public static ItemDefinition getItemDef(int i) {
		return itemDefinitions.get(i);
	}

	public static byte[][] getItemRequirements(int id) {
		return itemRequirements.get(id);
	}

	public static int getLowAlchemyValue(int id) {
		if (itemValues[id] == null || itemValues[id][0] == 0) {
			return itemDefinitions.get(id).getValue() / 3;
		}
		if (itemValues[id][0] == -1
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][0] == -1) {
			return 0;
		}
		return itemValues[id][0];
	}

	public static NpcCombatDefinition getNpcCombatDefinition(int id) {
		return npcCombatDefinitions.get(id);
	}

	public static NpcDefinition getNpcDefinition(int id) {
		return npcDefinitions.get(id);
	}

	public static RangedStrengthDefinition getRangedStrengthDefinition(int id) {
		return rangedStrengthDefinitions.get(id);
	}

	public static RangedWeaponDefinition getRangedWeaponDefinition(int id) {
		return rangedWeaponDefinitions.get(id);
	}

	public static SpecialAttackDefinition getSpecialDefinition(int id) {
		return specialAttackDefinitions.get(id);
	}

	public static int getStoreBuyFromValue(int id) {
		if (itemValues[id] == null || itemValues[id][2] == 0) {
			if (getHighAlchemyValue(id) > itemDefinitions.get(id).getValue() * 2) {
				return (int) (getHighAlchemyValue(id) * 0.75D);
			}
			return itemDefinitions.get(id).getValue() * 2;
		}
		if (itemValues[id][2] == -1
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][2] == -1) {
			return 0;
		}
		if (itemValues[id][2] < itemValues[id][3]) {
			if ((itemValues[id] == null) || (itemValues[id][3] == 0)) {
				return itemDefinitions.get(id).getValue() * 1;
			}

			return itemValues[id][3];
		}
		if (itemDefinitions.get(id).isNote()) {
			if (itemValues[id] == null || itemValues[id][2] == 0) {
				return itemDefinitions.get(itemDefinitions.get(id).getNoteId()).getValue() * 4;
			}
			if (getHighAlchemyValue(id) > itemValues[itemDefinitions.get(id).getNoteId()][2]) {
				return (int) (getHighAlchemyValue(id) * 0.75D);
			}
			return itemValues[itemDefinitions.get(id).getNoteId()][2];
		}
		if (getHighAlchemyValue(id) > itemValues[id][2]) {
			return (int) (getHighAlchemyValue(id) * 1.02D);
		}
		return itemValues[id][2];
	}

	public static int getStoreSellToValue(int id) {
		if ((itemValues[id] == null) || (itemValues[id][3] == 0)) {
			return itemDefinitions.get(id).getValue() * 1;
		}
		if (itemValues[id][3] == -1
				|| itemDefinitions.get(id).isNote() && itemValues[itemDefinitions.get(id).getNoteId()][3] == -1) {
			return 0;
		}
		if (itemValues[id][3] > itemValues[id][2]) {
			if ((itemValues[id] == null) || (itemValues[id][2] == 0)) {
				return itemDefinitions.get(id).getValue() * 2;
			}
			return itemValues[id][2];
		}
		if (itemDefinitions.get(id).isNote()) {
			if (itemValues[itemDefinitions.get(id).getNoteId()][3] == 0) {
				return itemDefinitions.get(id).getValue() * 5;
			}
			return itemValues[itemDefinitions.get(id).getNoteId()][3];
		}
		return itemValues[id][3];
	}

	public static WeaponDefinition getWeaponDefinition(int id) {
		return weaponDefinitions.get(id);
	}

	public static XStream getxStream() {
		return xStream;
	}

	@SuppressWarnings("unchecked")
	public static void loadCombatSpellDefinitions() throws FileNotFoundException, IOException {
		List<CombatSpellDefinition> list = (List<CombatSpellDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/magic/CombatSpellDefinitions.xml"));
		for (CombatSpellDefinition definition : list) {
			combatSpellDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " combat spell definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadEquipmentDefinitions() throws FileNotFoundException, IOException {
		List<EquipmentDefinition> list = (List<EquipmentDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/EquipmentDefinitions.xml"));
		for (EquipmentDefinition definition : list) {
			equipmentDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " equipment definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadItemBonusDefinitions() throws FileNotFoundException, IOException {
		List<ItemBonusDefinition> list = (List<ItemBonusDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/ItemBonusDefinitions.xml"));
		for (ItemBonusDefinition definition : list) {
			itemBonusDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " item bonus definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadItemDefinitions() throws FileNotFoundException, IOException {
		List<ItemDefinition> list = (List<ItemDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/ItemDefinitions.xml"));
		for (ItemDefinition definition : list) {
			itemDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " item definitions.");
	}

	public static final void loadItemValues() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("./data/def/items/ItemValues.txt"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				int id = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int a = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int b = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int c = Integer.parseInt(line.substring(0, line.indexOf(":")));
				line = line.substring(line.indexOf(":") + 1);
				int d = Integer.parseInt(line.substring(0, line.length()));
				if (a > 0 || b > 0 || c > 0 || d > 0) {
					itemValues[id] = new int[] { a, b, c, d };
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcCombatDefinitions() throws FileNotFoundException, IOException {
		List<NpcCombatDefinition> list = (List<NpcCombatDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/npcs/NpcCombatDefinitions.xml"));
		for (NpcCombatDefinition definition : list) {
			npcCombatDefinitions.put(definition.getId(), definition);
		}

		System.out.println("Loaded " + list.size() + " npc combat definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcDefinitions() throws FileNotFoundException, IOException {
		List<NpcDefinition> list = (List<NpcDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/npcs/NpcDefinitions.xml"));
		for (NpcDefinition definition : list) {
			npcDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " npc definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadNpcSpawns() throws FileNotFoundException, IOException {
		List<NpcSpawnDefinition> list = (List<NpcSpawnDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/npcs/NpcSpawnDefinitions.xml"));
		for (NpcSpawnDefinition def : list) {
			if (Region.getRegion(def.getLocation().getX(), def.getLocation().getY()) == null) {
				continue;
			}
			if (npcDefinitions.get(def.getId()).isAttackable() && npcCombatDefinitions.get(def.getId()) == null) {
				continue;
			}
			Npc m = new Npc(def.getId(),
					npcDefinitions.get(def.getId()).getName().equals("Fishing spot") ? false : def.isWalk(),
					def.getLocation());
			if (def.getFace() > 0) {
				m.setFaceDir(def.getFace());
			} else {
				m.setFaceDir(-1);
			}
		}
		System.out.println("Loaded " + list.size() + " npc spawns.");
	}

	@SuppressWarnings("unchecked")
	public static void loadRangedStrengthDefinitions() throws FileNotFoundException, IOException {
		List<RangedStrengthDefinition> list = (List<RangedStrengthDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/RangedStrengthDefinitions.xml"));
		for (RangedStrengthDefinition definition : list) {
			rangedStrengthDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " ranged strength bonus definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadRangedWeaponDefinitions() throws FileNotFoundException, IOException {
		List<RangedWeaponDefinition> list = (List<RangedWeaponDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/RangedWeaponDefinitions.xml"));
		for (RangedWeaponDefinition definition : list) {
			rangedWeaponDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " ranged weapon definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadShopDefinitions() throws FileNotFoundException, IOException {
		List<ShopDefinition> list = (List<ShopDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/ShopDefinitions.xml"));
		for (ShopDefinition def : list) {
			Shop.getShops()[def.getId()] = new Shop(def.getId(), def.getItems(), def.isGeneral(), def.getName());
		}
		System.out.println("Loaded " + list.size() + " shops.");
	}

	@SuppressWarnings("unchecked")
	public static void loadSpecialAttackDefinitions() throws FileNotFoundException, IOException {
		List<SpecialAttackDefinition> list = (List<SpecialAttackDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/SpecialAttackDefinitions.xml"));
		for (SpecialAttackDefinition definition : list) {
			specialAttackDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " special attack definitions.");
	}

	@SuppressWarnings("unchecked")
	public static void loadWeaponDefinitions() throws FileNotFoundException, IOException {
		List<WeaponDefinition> list = (List<WeaponDefinition>) xStream
				.fromXML(new FileInputStream("./data/def/items/WeaponDefinitions.xml"));
		for (WeaponDefinition definition : list) {
			weaponDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " weapon definitions.");
	}

	public static void setAlchValue(int id, int low, int high) {
		if (itemValues[id] == null) {
			itemValues[id] = new int[4];
		}

		itemValues[id][0] = low;
		itemValues[id][1] = high;
	}

	public static void setNotTradable(int id) {
		itemDefinitions.get(id).setUntradable();
	}

	public static void setRequirements() {
		for (Object def : equipmentDefinitions.values().toArray()) {
			EquipmentDefinition definition = (EquipmentDefinition) def;
			if (definition == null || definition.getRequirements() == null) {
				continue;
			}
			byte[][] requirements = new byte[Skills.SKILL_COUNT][2];
			int count = 0;
			for (int i = 0; i < definition.getRequirements().length; i++) {
				if (definition.getRequirements()[i] == 1) {
					continue;
				} else {
					requirements[count][0] = (byte) i;
					requirements[count][1] = definition.getRequirements()[i];
					count++;
				}
			}
			byte[][] set = new byte[count][2];
			for (int i = 0; i < count; i++) {
				set[i][0] = requirements[i][0];
				set[i][1] = requirements[i][1];
			}
			itemRequirements.put(((EquipmentDefinition) def).getId(), set);
			((EquipmentDefinition) def).setRequirements(null);
		}
	}

	public static void setValue(int id, int storeBuyFromValue, int storeSellToValue) {
		if (itemValues[id] == null) {
			itemValues[id] = new int[4];
		}
		itemValues[id][2] = storeBuyFromValue;
		itemValues[id][3] = storeSellToValue;
	}

	public static void setValueToZero(int id) {
		itemValues[id] = new int[] { -1, -1, -1, -1 };
	}

}