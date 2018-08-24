package org.mystic.game.model.content.skill.ranged;

import java.util.HashMap;
import java.util.Map;

import org.mystic.game.model.entity.Graphic;
import org.mystic.game.model.entity.Projectile;

public enum AmmoData {

	BRONZE_ARROW(new int[] { 882, 883, 5616, 5622 }, Graphic.highGraphic(19, 0), Graphic.highGraphic(1104, 0),
			new Projectile(10)),

	IRON_ARROW(new int[] { 884, 885, 5617, 5623 }, Graphic.highGraphic(18, 0), Graphic.highGraphic(1105, 0),
			new Projectile(9)),

	STEEL_ARROW(new int[] { 886, 887, 5618, 5624 }, Graphic.highGraphic(20, 0), Graphic.highGraphic(1106, 0),
			new Projectile(11)),

	MITHRIL_ARROW(new int[] { 888, 889, 5619, 5625 }, Graphic.highGraphic(21, 0), Graphic.highGraphic(1107, 0),
			new Projectile(12)),

	ADAMANT_ARROW(new int[] { 890, 891, 5620, 5626 }, Graphic.highGraphic(22, 0), Graphic.highGraphic(1108, 0),
			new Projectile(13)),

	RUNE_ARROW(new int[] { 892, 893, 5621, 5627 }, Graphic.highGraphic(24, 0), Graphic.highGraphic(1109, 0),
			new Projectile(15)),

	DRAGON_ARROW(new int[] { 11212, 11227, 0, 11228 }, Graphic.highGraphic(11229, 0), Graphic.highGraphic(1111, 0),
			new Projectile(17)),

	BOLTS(new int[] { 9144, 9245, 9242, 9241, 9243, 9244 }, Graphic.highGraphic(28, 0), null, new Projectile(27));

	private static final Map<Integer, AmmoData> ammoData = new HashMap<Integer, AmmoData>();

	static {
		for (AmmoData ammo : values()) {
			int[] ids = ammo.getIds();
			int[] arrayOfInt1;
			int m = (arrayOfInt1 = ids).length;
			for (int k = 0; k < m; k++) {
				Integer i = Integer.valueOf(arrayOfInt1[k]);
				ammoData.put(i, ammo);
			}
		}
	}

	public static AmmoData forId(int id) {
		return ammoData.get(Integer.valueOf(id));
	}

	private final int[] ids;

	private final Graphic drawback1;

	private final Graphic drawback2;

	private final Projectile projectile;

	private AmmoData(final int[] ids, final Graphic drawback1, final Graphic drawback2, final Projectile projectile) {
		this.ids = ids;
		this.drawback1 = drawback1;
		this.drawback2 = drawback2;
		this.projectile = projectile;
	}

	public final Graphic getDrawback1() {
		return drawback1;
	}

	public final Graphic getDrawback2() {
		return drawback2;
	}

	public final int[] getIds() {
		return ids;
	}

	public final Projectile getProjectile() {
		return projectile;
	}

}