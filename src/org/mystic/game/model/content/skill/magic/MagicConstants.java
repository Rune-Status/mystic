package org.mystic.game.model.content.skill.magic;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.definition.CombatSpellDefinition;
import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.utility.GameDefinitionLoader;

public class MagicConstants {

	public enum SpellType {
		NONE, EARTH, WATER, WIND, FIRE, SLAYER_DART;
	}

	public static final Animation MODERN_TELEPORT_ANIMATION = new Animation(8939, 0);

	public static final Graphic MODERN_TELEPORT_GRAPHIC = Graphic.lowGraphic(1576, 0);

	public static final Graphic MODERN_TELEPORT_END_GRAPHIC = Graphic.lowGraphic(1577, 0);

	public static final Animation MODERN_TELEPORT_END_ANIMATION = new Animation(8941, 0);

	public static final Animation ANCIENT_TELEPORT_ANIMATION = new Animation(9599, 0);

	public static final Graphic ANCIENT_TELEPORT_GRAPHIC = Graphic.lowGraphic(1681, 0);

	public static final Animation ANCIENT_TELEPORT_END_ANIMATION = new Animation(65535, 0);

	public static final Graphic ANCIENT_TELEPORT_END_GRAPHIC = Graphic.lowGraphic(-1, 0);

	public static final Animation TABLET_BREAK_ANIMATION = new Animation(4069, 0);

	public static final Animation TABLET_TELEPORT_ANIMATION = new Animation(4731, 0);

	public static final Graphic TABLET_TELEPORT_GRAPHIC = Graphic.lowGraphic(678, 0);

	public static final Animation TABLET_TELEPORT_END_ANIMATION = new Animation(65535, 0);

	private static final Map<Integer, SpellType> spell_type = new HashMap<Integer, SpellType>();

	static {
		for (int i = 0; i < 50; i++) {
			CombatSpellDefinition def = GameDefinitionLoader.getCombatSpellDefinition(i);
			if (def != null) {
				String spell = def.getName().toLowerCase();
				if (spell.contains("fire")) {
					spell_type.put(Integer.valueOf(def.getId()), SpellType.FIRE);
				} else if (spell.contains("earth")) {
					spell_type.put(Integer.valueOf(def.getId()), SpellType.EARTH);
				} else if ((spell.contains("air")) || (spell.contains("wind"))) {
					spell_type.put(Integer.valueOf(def.getId()), SpellType.WIND);
				} else if (spell.contains("water")) {
					spell_type.put(Integer.valueOf(def.getId()), SpellType.WATER);
				} else if (spell.contains("magic dart")) {
					spell_type.put(Integer.valueOf(def.getId()), SpellType.SLAYER_DART);
				}
			}
		}
	}

	public static SpellType forId(int id) {
		SpellType type = spell_type.get(Integer.valueOf(id));
		if (type != null) {
			return type;
		} else {
			return SpellType.NONE;
		}
	}
}