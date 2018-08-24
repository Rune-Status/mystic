package org.mystic.game.model.definition;

import java.util.Arrays;

import org.mystic.game.model.entity.Animation;
import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;
import org.mystic.game.model.entity.item.Item;

public class CombatSpellDefinition {

	private String name;
	private int id;
	private byte baseMaxHit;
	private double baseExperience;
	private Animation animation;
	private Graphic start;
	private Projectile projectile;
	private Graphic end;
	private byte level;
	private int[] weapon;
	private Item[] runes;

	public Animation getAnimation() {
		return animation;
	}

	public double getBaseExperience() {
		return baseExperience;
	}

	public int getBaseMaxHit() {
		return baseMaxHit;
	}

	public Graphic getEnd() {
		return end;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public Item[] getRunes() {
		return runes;
	}

	public Graphic getStart() {
		return start;
	}

	public int[] getWeapons() {
		return weapon;
	}

	@Override
	public String toString() {
		return "CombatSpellDefinition [name=" + name + ", id=" + id + ", level=" + level + ", runes="
				+ Arrays.toString(runes) + "]";
	}
}
