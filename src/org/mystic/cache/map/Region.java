package org.mystic.cache.map;

import java.util.ArrayList;
import java.util.List;

import org.mystic.game.model.entity.Location;

public class Region {

	private static Region[] regions;

	public static RSObject getObject(int x, int y, int z) {
		Region region = getRegion(x, y);
		if (region == null) {
			return null;
		}
		int regionAbsX = (region.id >> 8) << 6;
		int regionAbsY = (region.id & 0xff) << 6;
		if (z > 3) {
			z = z % 4;
		}
		if (region.objects[z] == null) {
			return null;
		}
		return region.objects[z][x - regionAbsX][y - regionAbsY];
	}

	/**
	 * Fetches a region after they are sorted by id.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @return the region.
	 */
	public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (regionId > regions.length - 1) {
			return null;
		}
		if (regionId < 0) {
			System.out.println("FATAL CLIPPING ERROR: regionId < 0");
			System.exit(0);
		}
		if (regions[regionId] == null) {
			return null;
		}
		return regions[regionId];
	}

	public static Region getRegion(Location l) {
		return getRegion(l.getX(), l.getY());
	}

	public static Region getRegionById(int id) {
		for (Region region : regions) {
			if (region == null) {
				continue;
			}
			if (region.id() == id) {
				return region;
			}
		}
		return null;
	}

	public static Region[] getRegions() {
		return regions;
	}

	public static int getStaticClip(int x, int y, int z) {
		Region region = getRegion(x, y);
		int regionAbsX = (region.id >> 8) << 6;
		int regionAbsY = (region.id & 0xff) << 6;
		if (z > 3) {
			z = z % 4;
		}
		if (x - regionAbsX < 0 || y - regionAbsY < 0 || x - regionAbsX > 63 || y - regionAbsY > 63) {
			return getRegion(x, y).getClip(x, y, z);
		}
		try {
			if (region.clips[z] == null) {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return region.clips[z][x - regionAbsX][y - regionAbsY];
	}

	public static int getStaticClip(Location p) {
		return getStaticClip(p.getX(), p.getY(), p.getZ());
	}

	/**
	 * Used to fetch a region before they are sorted by id.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @return the region.
	 */
	public static Region getUnsortedRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region region : regions) {
			if (region == null) {
				continue;
			}
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	public static boolean objectExists(int objectId, int x, int y, int z) {
		Region r = getRegion(x, y);
		if (r == null) {
			return false;
		}
		int regionAbsX = (r.id >> 8) << 6;
		int regionAbsY = (r.id & 0xff) << 6;
		if (z > 3) {
			z = z % 4;
		}
		if (r.objects[z] == null) {
			return false;
		}
		return r.objects[z][x - regionAbsX][y - regionAbsY] != null
				&& r.objects[z][x - regionAbsX][y - regionAbsY].getId() == objectId;
	}

	public static void setRegions(Region[] set) {
		regions = set;
	}

	public static void sort() {
		Region[] sorted = new Region[70000];
		for (int i = 0; i < regions.length; i++) {
			if (regions[i] == null) {
				continue;
			}
			sorted[regions[i].id()] = regions[i];
		}
		regions = sorted;

	}

	private int id;

	private int[][][] clips;

	private int[][][] shootable;

	private RSObject[][][] objects;

	private List<Tile> npcs = new ArrayList<Tile>();

	public Region(int id) {
		this.id = id;
		this.clips = new int[4][][];
		this.shootable = new int[4][][];
		this.objects = new RSObject[4][][];
	}

	public void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
			for (int z = 0; z < 4; z++) {
				for (int x2 = 0; x < 64; x++) {
					for (int y2 = 0; y < 64; y++) {
						clips[z][x2][y2] = -1;
					}
				}
			}
		}
		if (clips[height][x - regionAbsX][y - regionAbsY] == -1) {
			clips[height][x - regionAbsX][y - regionAbsY] = 0;
		}
		clips[height][x - regionAbsX][y - regionAbsY] += shift;
	}

	/**
	 * Adds an object to this region.
	 * 
	 * @param object
	 *            the object.
	 */
	public void addObject(RSObject object) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		int z = object.getZ();
		if (z > 3) {
			z = z % 4;
		}
		if (objects[z] == null) {
			objects[z] = new RSObject[64][64];
		}
		objects[z][object.getX() - regionAbsX][object.getY() - regionAbsY] = object;
	}

	public void addShootable(int x, int y, int z, int flag) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		if (shootable[z] == null) {
			shootable[z] = new int[64][64];
		}
		shootable[z][x - regionAbsX][y - regionAbsY] |= flag;
	}

	public boolean blockedEast(int x, int y, int z) {
		return (getClip(x + 1, y, z) & 0x1280180) != 0 || getClip(x + 1, y, z) == -1;
	}

	public boolean blockedNorth(int x, int y, int z) {
		return (getClip(x, y + 1, z) & 0x1280120) != 0 || getClip(x, y + 1, z) == -1;
	}

	public boolean blockedNorthEast(int x, int y, int z) {
		return (getClip(x + 1, y + 1, z) & 0x12801e0) != 0 || getClip(x + 1, y + 1, z) == -1;
	}

	public boolean blockedNorthWest(int x, int y, int z) {
		return (getClip(x - 1, y + 1, z) & 0x1280138) != 0 || getClip(x - 1, y + 1, z) == -1;
	}

	public boolean blockedSouth(int x, int y, int z) {
		return (getClip(x, y - 1, z) & 0x1280102) != 0 || getClip(x, y - 1, z) == -1;
	}

	public boolean blockedSouthEast(int x, int y, int z) {
		return (getClip(x + 1, y - 1, z) & 0x1280183) != 0 || getClip(x + 1, y - 1, z) == -1;
	}

	public boolean blockedSouthWest(int x, int y, int z) {
		return (getClip(x - 1, y - 1, z) & 0x128010e) != 0 || getClip(x - 1, y - 1, z) == -1;
	}

	public boolean blockedWest(int x, int y, int z) {
		return (getClip(x - 1, y, z) & 0x1280108) != 0 || getClip(x - 1, y, z) == -1;
	}

	/**
	 * Tells you if this direction is walkable.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @param direction
	 *            the direction.
	 * @return if the direction is walkable.
	 */
	public boolean canMove(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !blockedNorthWest(x, y, z) && !blockedNorth(x, y, z) && !blockedWest(x, y, z);
		} else if (direction == 1) {
			return !blockedNorth(x, y, z);
		} else if (direction == 2) {
			return !blockedNorthEast(x, y, z) && !blockedNorth(x, y, z) && !blockedEast(x, y, z);
		} else if (direction == 3) {
			return !blockedWest(x, y, z);
		} else if (direction == 4) {
			return !blockedEast(x, y, z);
		} else if (direction == 5) {
			return !blockedSouthWest(x, y, z) && !blockedSouth(x, y, z) && !blockedWest(x, y, z);
		} else if (direction == 6) {
			return !blockedSouth(x, y, z);
		} else if (direction == 7) {
			return !blockedSouthEast(x, y, z) && !blockedSouth(x, y, z) && !blockedEast(x, y, z);
		}
		return false;
	}

	public boolean canMove(Location l, int dir) {
		return canMove(l.getX(), l.getY(), l.getZ(), dir);
	}

	/**
	 * Tells you if this direction is shootable.
	 * 
	 * @param x
	 *            the x coordinate.
	 * @param y
	 *            the y coordinate.
	 * @param z
	 *            the z coordinate.
	 * @param direction
	 *            the direction.
	 * @return if the direction is walkable.
	 */
	public boolean canShoot(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !shotBlockedWest(x, y, z) && !shotBlockedNorth(x, y, z) && !shotBlockedWest(x, y, z);
		} else if (direction == 1) {
			return !shotBlockedNorth(x, y, z);
		} else if (direction == 2) {
			return !shotBlockedNorthEast(x, y, z) && !shotBlockedNorth(x, y, z) && !shotBlockedEast(x, y, z);
		} else if (direction == 3) {
			return !shotBlockedWest(x, y, z);
		} else if (direction == 4) {
			return !shotBlockedEast(x, y, z);
		} else if (direction == 5) {
			return !shotBlockedSouthWest(x, y, z) && !shotBlockedSouth(x, y, z) && !shotBlockedWest(x, y, z);
		} else if (direction == 6) {
			return !shotBlockedSouth(x, y, z);
		} else if (direction == 7) {
			return !shotBlockedSouthEast(x, y, z) && !shotBlockedSouth(x, y, z) && !shotBlockedEast(x, y, z);
		}
		return false;
	}

	public int getClip(int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		if (z > 3) {
			z = z % 4;
		}

		if (x - regionAbsX < 0 || y - regionAbsY < 0 || x - regionAbsX > 63 || y - regionAbsY > 63) {
			if (id < regions.length && regions[id] != null && regions[id].id == id) {

				if (getRegion(x, y) == null) {
					return 0;
				}

				return getRegion(x, y).getClip(x, y, z);
			} else {
				if (getUnsortedRegion(x, y) == null) {
					return 0;
				}

				return getUnsortedRegion(x, y).getClip(x, y, z);
			}
		}

		if (clips[z] == null) {
			return 0;
		}

		return clips[z][x - regionAbsX][y - regionAbsY];
	}

	public int getClip(Location location) {
		int x = location.getX();
		int y = location.getY();
		int z = location.getZ();

		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		if (z > 3) {
			z = z % 4;
		}

		if (x - regionAbsX < 0 || y - regionAbsY < 0 || x - regionAbsX > 63 || y - regionAbsY > 63) {
			return getRegion(x, y).getClip(x, y, z);
		}

		if (clips[z] == null) {
			return 0;
		}

		return clips[z][x - regionAbsX][y - regionAbsY];
	}

	public int[][][] getClips() {
		return clips;
	}

	public int getId() {
		return id;
	}

	public List<Tile> getNpcs() {
		return npcs;
	}

	public RSObject[][][] getObjects() {
		return objects;
	}

	public int[][][] getShootable() {
		return shootable;
	}

	public int getShootable(int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;

		if (z > 3) {
			z = z % 4;
		}

		if (x - regionAbsX < 0 || y - regionAbsY < 0 || x - regionAbsX > 63 || y - regionAbsY > 63) {
			return getRegion(x, y).getClip(x, y, z);
		}

		if (shootable[z] == null) {
			return 0;
		}

		return shootable[z][x - regionAbsX][y - regionAbsY];
	}

	/**
	 * @return the region id.
	 */
	public int id() {
		return id;
	}

	public boolean isNpcOnTile(int x, int y, int z) {
		if (z > 3) {
			z = z % 4;
		}

		if (npcs == null) {
			return false;
		}

		return npcs.contains(new Tile(x, y, z));
	}

	public void removeObject(RSObject object) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		int z = object.getZ();
		if (z > 3) {
			z = z % 4;
		}
		if (objects[z] == null) {
			return;
		}
		objects[z][object.getX() - regionAbsX][object.getY() - regionAbsY] = null;
	}

	public void setClip(int x, int y, int z, int clip) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		if (clips[z] == null) {
			return;
		}
		clips[z][x - regionX][y - regionY] = clip;
	}

	public void setClips(int[][][] clips) {
		this.clips = clips;
	}

	public void setClipToZero(int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		clips[z][x - regionAbsX][y - regionAbsY] = 0;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNpcOnTile(boolean onTile, int x, int y, int z) {
		if (z > 3) {
			z = z % 4;
		}
		if (onTile) {
			if (npcs == null) {
				npcs = new ArrayList<Tile>();
			}
			npcs.add(new Tile(x, y, z));
		} else if (npcs != null) {
			npcs.remove(new Tile(x, y, z));
			if (npcs.size() == 0) {
				npcs = null;
			}
		}
	}

	public void setNpcs(List<Tile> npcs) {
		this.npcs = npcs;
	}

	public void setObjects(RSObject[][][] objects) {
		this.objects = objects;
	}

	public void setProjecileClipToInfinity(int x, int y, int z) {
		int regionAbsX = (id >> 8) << 6;
		int regionAbsY = (id & 0xff) << 6;
		shootable[z][x - regionAbsX][y - regionAbsY] = 0x200000;
	}

	public void setShootable(int[][][] shootable) {
		this.shootable = shootable;
	}

	public boolean shotBlockedEast(int x, int y, int z) {
		return (getShootable(x + 1, y, z) & 0x1280180) != 0;
	}

	public boolean shotBlockedNorth(int x, int y, int z) {
		return (getShootable(x, y + 1, z) & 0x1280120) != 0;
	}

	public boolean shotBlockedNorthEast(int x, int y, int z) {
		return (getShootable(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public boolean shotBlockedNorthWest(int x, int y, int z) {
		return (getShootable(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public boolean shotBlockedSouth(int x, int y, int z) {
		return (getShootable(x, y - 1, z) & 0x1280102) != 0;
	}

	public boolean shotBlockedSouthEast(int x, int y, int z) {
		return (getShootable(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public boolean shotBlockedSouthWest(int x, int y, int z) {
		return (getShootable(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	public boolean shotBlockedWest(int x, int y, int z) {
		return (getShootable(x - 1, y, z) & 0x1280108) != 0;
	}

	public boolean withinRegion(int x, int y) {
		int regionAbsX = x - ((id >> 8) << 6);
		int regionAbsY = y - ((id & 0xff) << 6);
		return !(regionAbsX < 0 || regionAbsY < 0 || regionAbsX > 63 || regionAbsY > 63);
	}

}
