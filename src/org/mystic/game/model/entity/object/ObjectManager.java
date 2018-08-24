package org.mystic.game.model.entity.object;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.mystic.cache.map.MapLoading;
import org.mystic.cache.map.RSObject;
import org.mystic.cache.map.Region;
import org.mystic.game.World;
import org.mystic.game.model.entity.Location;
import org.mystic.game.model.entity.player.Player;

public class ObjectManager {

	public static final int BLANK_OBJECT_ID = 2376;

	private static final List<GameObject> active = new LinkedList<GameObject>();

	private static final Deque<GameObject> register = new LinkedList<GameObject>();

	private static final Queue<GameObject> send = new ConcurrentLinkedQueue<GameObject>();

	public static void add(GameObject o) {
		active.add(o);
	}

	public static void addClippedObject(GameObject o) {
		register.add(o);
	}

	public static void declare() {
		for (GameObject i : active) {
			send(getBlankObject(i.getLocation()));
		}
		active.clear();
		delete(3092, 3957, 0);
		delete(2330, 3802, 0);
		delete(2330, 3810, 0);
		delete(2339, 3805, 0);
		delete(2342, 3807, 0);
		delete(2344, 3809, 0);
		delete(2345, 3806, 0);
		delete(2340, 3806, 0);
		delete(2340, 3807, 0);
		delete(2340, 3808, 0);
		delete(2341, 3811, 0);
		delete(2342, 3812, 0);
		delete(2341, 3812, 0);
		delete(2342, 3814, 0);
		delete(2341, 3814, 0);
		delete(2344, 3814, 0);
		delete(2344, 3813, 0);
		delete(2344, 3812, 0);
		delete(2343, 3812, 0);
		delete(2352, 3795, 0);
		delete(2317, 3806, 0);
		delete(3096, 3479, 0);
		delete(3096, 3476, 0);
		delete(3090, 3476, 0);
		delete(3090, 3479, 0);
		delete(3210, 9898, 0);
		delete(2543, 10143, 0);
		delete(2545, 10141, 0);
		delete(2545, 10145, 0);
		delete(2328, 3805, 0);
		delete(2328, 3804, 0);
		delete(2339, 3801, 0);
		delete(2347, 3801, 0);
		delete(2345, 3807, 0);
		delete(2352, 3801, 0);
		delete(2336, 3805, 0);
		delete(2341, 3814, 0);
		delete(2342, 3814, 0);
		delete(2341, 3812, 0);
		delete(2332, 3812, 0);
		delete(2854, 3546, 0);
		delete(2855, 3546, 0);
		delete(3180, 3442, 0);
		delete(3180, 3443, 0);
		delete(3180, 3444, 0);
		delete(3180, 3435, 0);
		delete(3180, 3436, 0);
		delete(3180, 3438, 0);
		delete(3180, 3440, 0);
		delete(3095, 3498, 0);
		delete(3095, 3499, 0);
		delete(3095, 3510, 0);
		delete(3096, 3511, 0);
		delete(3095, 3498, 0);
		delete(3094, 3511, 0);
		delete(3092, 3511, 0);
		delete(3091, 3510, 0);
		delete(3099, 3507, 0);
		delete(3100, 3508, 0);
		delete(3100, 3513, 0);
		delete(3100, 3513, 0);
		delete(3099, 3512, 0);
		delete(3096, 3513, 0);
		delete(3097, 3513, 0);
		delete(3098, 3513, 0);
		delete(2334, 3805, 0);
		delete(3094, 3510, 0);
		delete(3100, 3512, 0);
		delete(3093, 3513, 0);
		delete(3092, 3513, 0);
		delete(3091, 3513, 0);
		delete(3091, 3508, 0);
		delete(3091, 3507, 0);
		delete(3095, 3507, 0);
		delete(3096, 3507, 0);
		delete(3097, 3507, 0);
		delete(3098, 3507, 0);
		delete(3100, 3507, 0);
		delete(3094, 3507, 0);
		delete(3093, 3507, 0);
		delete(3091, 3511, 0);
		delete(3101, 3509, 0);
		delete(3101, 3510, 0);
		delete(2326, 3802, 0);
		delete(2670, 2593, 0);
		delete(2670, 2592, 0);
		delete(2657, 2585, 0);
		delete(2656, 2585, 0);
		delete(2643, 2592, 0);
		delete(2643, 2593, 0);
		delete(3092, 3496, 0);
		delete(3095, 3513, 0);
		delete(3094, 9513, 0);
		delete(3094, 3510, 0);
		delete(3094, 3509, 0);
		delete(3093, 3510, 0);
		delete(3093, 3509, 0);
		delete(3091, 3512, 0);
		delete(3090, 3512, 0);

		deleteWithObject(3090, 3503, 0);
		deleteWithObject(3084, 3502, 0);

		// Slayer tower door
		delete(3445, 3554, 2);// Slayer tower door
		setClipToZero(3445, 3554, 2);

		// Webs
		delete(3105, 3958, 0);
		delete(3106, 3958, 0);
		delete(3093, 3957, 0);
		delete(3095, 3957, 0);
		delete(3158, 3951, 0);
		delete(3030, 3852, 0);
		delete(3115, 3859, 0);

		// barrows chest
		delete(3551, 9695, 0);

		deleteWithObject(3090, 3494, 0);
		deleteWithObject(3090, 3496, 0);
		deleteWithObject(3091, 3495, 0);

		spawnWithObject(23925, 3090, 3494, 0, 10, 0);
		spawnWithObject(23929, 3090, 3496, 0, 10, 0);

		deleteWithObject(3426, 3555, 1);
		deleteWithObject(3427, 3555, 1);
		deleteWithObject(3091, 3512, 1);
		deleteWithObject(3090, 3512, 1);
		deleteWithObject(3445, 3554, 2);

		deleteWithObject(3091, 3495, 1);

		deleteWithObject(2854, 3546, 0);
		deleteWithObject(2855, 3546, 0);

		deleteWithObject(3096, 3501, 0);
		setClipToZero(3096, 3501, 0);

		deleteWithObject(3083, 3500, 0);
		spawnWithObject(29953, 3083, 3500, 0, 10, 0);

		deleteWithObject(2444, 3083, 0);
		spawnWithObject(4483, 2444, 3083, 0, 10, 1);

		// Safe wars portal
		spawnWithObject(28213, 3084, 3483, 0, 10, 0);

		// Webs
		spawnWithObject(734, 3105, 3958, 0, 10, 3);
		spawnWithObject(734, 3106, 3958, 0, 10, 3);
		spawnWithObject(734, 3158, 3951, 0, 10, 1);
		spawnWithObject(734, 3093, 3957, 0, 10, 0);
		spawnWithObject(734, 3095, 3957, 0, 10, 0);
		spawnWithObject(734, 3030, 3852, 0, 10, 0);
		spawnWithObject(734, 3115, 3859, 0, 10, 0);

		// Crafting Guild
		delete(2936, 3292, 0);
		spawnWithObject(4309, 2937, 3292, 0, 10, 4);// spinning wheel
		spawnWithObject(2191, 2936, 3292, 0, 10, 0);// crystal chest

		spawnWithObject(2807, 3187, 3683, 0, 10, 0);// corp cave

		spawnWithObject(2469, 3503, 3562, 0, 10, 0);// exit out of nm minigame

		// Edgeville
		spawnWithObject(12355, 3098, 3513, 0, 10, 0);
		spawnWithObject(12309, 3100, 3512, 0, 10, -1);
		spawnWithObject(409, 3095, 3507, 0, 10, 0);
		spawnWithObject(13643, 3091, 3509, 0, 10, 1);
		spawnWithObject(1815, 3097, 3499, 0, 4, -3);
		spawnWithObject(2191, 3100, 3508, 0, 10, -1);

		// edgeville stalls
		spawnWithObject(2561, 3091, 3505, 0, 10, 0);
		spawnWithObject(2560, 3093, 3505, 0, 10, 0);
		spawnWithObject(2565, 3095, 3505, 0, 10, 0);
		spawnWithObject(2564, 3097, 3505, 0, 10, 0);
		spawnWithObject(2562, 3099, 3505, 0, 10, 0);

		// Home
		spawnWithObject(13643, 2339, 3806, 0, 10, 3);// altar
		spawnWithObject(409, 2334, 3805, 0, 10, 1);// altar

		spawnWithObject(2783, 2341, 3813, 0, 10, 0);// Anvil

		// barrows
		spawnWithObject(6775, 3551, 9695, 0, 10, 0);// barrows chest
		spawnWithObject(170, 3503, 3572, 0, 10, 0);// barrows chest

		// RFD
		spawnWithObject(12309, 3219, 9623, 0, 10, 3);// chest

		setClipToZero(3002, 3961, 0);
		setClipToZero(3002, 3960, 0);
		setClipToZero(2539, 4716, 0);
		setClipToZero(3068, 10255, 0);
		setClipToZero(3068, 10256, 0);
		setClipToZero(3068, 10258, 0);
		setClipToZero(3067, 10255, 0);
		setClipToZero(3067, 10254, 0);
		setClipToZero(3066, 10256, 0);
		setClipToZero(3426, 3555, 1);
		setClipToZero(3427, 3555, 1);
		setClipToZero(3445, 3554, 2);

		// warrios guild
		setClipToZero(2851, 3550, 0);
		setClipToZero(2855, 3545, 0);
		setClipToZero(2855, 3546, 0);
		setClipToZero(2854, 3546, 0);
		setClipToZero(2854, 3544, 0);
		setClipToZero(2854, 3545, 0);
		setClipToZero(2849, 3547, 0);
		setClipToZero(2847, 3546, 0);
		setClipToZero(2846, 3547, 0);
		setClipToZero(2842, 3538, 0);
		setClipToZero(2843, 3538, 0);
		setClipToZero(2847, 3545, 0);

		// kbd
		setClipToZero(3067, 10253, 0);

		// Godwars
		setPClipInfinity(2837, 5294, 2);
		setPClipInfinity(2836, 5294, 2);
		setPClipInfinity(2835, 5294, 2);
		setPClipInfinity(2834, 5294, 2);
		setPClipInfinity(2833, 5294, 2);
		setPClipInfinity(2832, 5294, 2);
		setPClipInfinity(2831, 5294, 2);
		setPClipInfinity(2830, 5294, 2);
		setPClipInfinity(2829, 5294, 2);
		setPClipInfinity(2828, 5294, 2);
		setPClipInfinity(2827, 5294, 2);
		setPClipInfinity(2826, 5294, 2);
		setPClipInfinity(2838, 5294, 2);
		setPClipInfinity(2839, 5294, 2);
		setPClipInfinity(2840, 5294, 2);
		setPClipInfinity(2841, 5294, 2);
		setPClipInfinity(2842, 5294, 2);
		setPClipInfinity(2843, 5294, 2);
		setPClipInfinity(2844, 5294, 2);

		// bandos
		setPClipInfinity(2863, 5355, 2);
		setPClipInfinity(2863, 5356, 2);
		setPClipInfinity(2863, 5357, 2);
		setPClipInfinity(2863, 5358, 2);
		setPClipInfinity(2863, 5359, 2);
		setPClipInfinity(2863, 5360, 2);
		setPClipInfinity(2863, 5361, 2);
		setPClipInfinity(2863, 5362, 2);
		setPClipInfinity(2863, 5363, 2);
		setPClipInfinity(2863, 5364, 2);
		setPClipInfinity(2863, 5365, 2);
		setPClipInfinity(2863, 5366, 2);
		setPClipInfinity(2863, 5367, 2);
		setPClipInfinity(2863, 5368, 2);
		setPClipInfinity(2863, 5369, 2);
		setPClipInfinity(2863, 5370, 2);
		setPClipInfinity(2863, 5354, 2);
		setPClipInfinity(2863, 5353, 2);
		setPClipInfinity(2863, 5352, 2);
		setPClipInfinity(2863, 5351, 2);
		setPClipInfinity(2863, 5350, 2);
		setPClipInfinity(2863, 5349, 2);
		setPClipInfinity(2863, 5348, 2);

		// Saradomin
		setPClipInfinity(2908, 5265, 0);
		setPClipInfinity(2908, 5266, 0);
		setPClipInfinity(2908, 5267, 0);
		setPClipInfinity(2908, 5268, 0);
		setPClipInfinity(2908, 5269, 0);
		setPClipInfinity(2908, 5270, 0);
		setPClipInfinity(2908, 5271, 0);
		setPClipInfinity(2908, 5272, 0);
		setPClipInfinity(2908, 5273, 0);
		setPClipInfinity(2908, 5274, 0);
		setPClipInfinity(2908, 5275, 0);
		setPClipInfinity(2908, 5276, 0);
		setPClipInfinity(2908, 5277, 0);
		setPClipInfinity(2908, 5278, 0);
		setPClipInfinity(2908, 5264, 0);
		setPClipInfinity(2908, 5263, 0);
		setPClipInfinity(2908, 5262, 0);
		setPClipInfinity(2908, 5261, 0);
		setPClipInfinity(2908, 5259, 0);
		setPClipInfinity(2908, 5258, 0);
		setPClipInfinity(2908, 5257, 0);

		setPClipInfinity(2909, 5265, 0);
		setPClipInfinity(2909, 5266, 0);
		setPClipInfinity(2909, 5267, 0);
		setPClipInfinity(2909, 5268, 0);
		setPClipInfinity(2909, 5269, 0);
		setPClipInfinity(2909, 5270, 0);
		setPClipInfinity(2909, 5271, 0);
		setPClipInfinity(2909, 5272, 0);
		setPClipInfinity(2909, 5273, 0);
		setPClipInfinity(2909, 5274, 0);
		setPClipInfinity(2909, 5275, 0);
		setPClipInfinity(2909, 5276, 0);
		setPClipInfinity(2909, 5277, 0);
		setPClipInfinity(2909, 5278, 0);
		setPClipInfinity(2909, 5264, 0);
		setPClipInfinity(2909, 5263, 0);
		setPClipInfinity(2909, 5262, 0);
		setPClipInfinity(2909, 5261, 0);
		setPClipInfinity(2909, 5259, 0);
		setPClipInfinity(2909, 5258, 0);
		setPClipInfinity(2909, 5257, 0);
		for (GameObject i : active) {
			send(i);
		}
		System.out.println("Object spawns loaded.");
	}

	private static void delete(int x, int y, int z) {
		RSObject object = Region.getObject(x, y, z);
		if (object == null) {
			if (z > 0) {
				active.add(new GameObject(2376, x, y, z, 10, 0));
			}
			return;
		}
		MapLoading.removeObject(object.getId(), x, y, z, object.getType(), object.getFace());
		if ((object.getType() != 10) || (z > 0)) {
			active.add(new GameObject(2376, x, y, z, object.getType(), 0));
		}
	}

	private static void deleteWithObject(int x, int y, int z) {
		RSObject object = Region.getObject(x, y, z);
		if (object == null) {
			active.add(new GameObject(2376, x, y, z, 10, 0));
			return;
		}
		MapLoading.removeObject(object.getId(), x, y, z, object.getType(), object.getFace());
		active.add(new GameObject(2376, x, y, z, object.getType(), 0));
	}

	public static List<GameObject> getActive() {
		return active;
	}

	public static GameObject getBlankObject(Location p) {
		return new GameObject(2376, p.getX(), p.getY(), p.getZ(), 10, 0, false);
	}

	public static GameObject getBlankObject(Location p, int type) {
		return new GameObject(2376, p.getX(), p.getY(), p.getZ(), type, 0, false);
	}

	public static GameObject getGameObject(int x, int y, int z) {
		int index = active.indexOf(new GameObject(x, y, z));
		if (index == -1) {
			return null;
		}
		return active.get(index);
	}

	public static Queue<GameObject> getSend() {
		return send;
	}

	public static boolean objectExists(Location location) {
		for (GameObject object : active) {
			if (location.equals(object.getLocation())) {
				return true;
			}
		}
		return false;
	}

	public static void process() {
		for (Iterator<GameObject> i = register.iterator(); i.hasNext();) {
			GameObject reg = i.next();
			active.remove(reg);
			active.add(reg);
			send.add(reg);
			i.remove();
		}
	}

	public static void queueSend(GameObject o) {
		send.add(o);
	}

	public static void register(GameObject o) {
		GameObject gameObject = ObjectManager.getGameObject(o.getLocation().getX(), o.getLocation().getY(),
				o.getLocation().getZ());
		if (gameObject != null) {
			ObjectManager.removeFromList(gameObject);
		}
		register.add(o);
	}

	public static void remove(GameObject o) {
		removeFromList(o);
		send.add(getBlankObject(o.getLocation(), o.getType()));
	}

	public static void remove2(GameObject o) {
		send.add(getBlankObject(o.getLocation(), o.getType()));
	}

	public static void removeFromList(GameObject o) {
		active.remove(o);
	}

	public static void send(GameObject o) {
		for (Player player : World.getPlayers()) {
			if ((player != null) && (player.isActive())) {
				if ((player.withinRegion(o.getLocation()))
						&& (player.getLocation().getZ() % 4 == o.getLocation().getZ() % 4)) {
					player.getObjects().add(o);
				}
			}
		}
	}

	public static void setClipToZero(int x, int y, int z) {
		Region region = Region.getRegion(x, y);
		region.setClipToZero(x, y, z);
	}

	public static void setPClipInfinity(int x, int y, int z) {
		Region region = Region.getRegion(x, y);
		region.setProjecileClipToInfinity(x, y, z);
	}

	public static void spawnWithObject(int id, int x, int y, int z, int type, int face) {
		active.add(new GameObject(id, x, y, z, type, face));
		MapLoading.addObject(false, id, x, y, z, type, face);
		send(new GameObject(id, x, y, z, type, face));
	}

}