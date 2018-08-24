package org.mystic.cache.map;

import org.mystic.cache.ByteStreamExt;

@SuppressWarnings("unused")
public final class ObjectDef {

	private static ByteStreamExt stream;

	public static int[] streamIndices;

	public static ObjectDef class46;

	private static int objects = 0;

	public static boolean lowMem;

	private static int cacheIndex;

	private static ObjectDef[] cache;

	public static byte[] getBuffer(String s) {
		try {
			java.io.File f = new java.io.File("./data/map/objectdata/" + s);
			if (!f.exists()) {
				return null;
			}
			byte[] buffer = new byte[(int) f.length()];
			java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (Exception e) {
		}
		return null;
	}

	public static ObjectDef getObjectDef(int i) {
		if (i > streamIndices.length) {
			i = streamIndices.length - 1;
		}
		for (int j = 0; j < 20; j++) {
			if (cache[j].type == i) {
				return cache[j];
			}
		}
		cacheIndex = (cacheIndex + 1) % 20;
		class46 = cache[cacheIndex];
		if (i > streamIndices.length - 1 || i < 0) {
			return null;
		}
		stream.currentOffset = streamIndices[i];
		class46.type = i;
		class46.setDefaults();
		class46.readValues(stream);
		if (i == 14210 || i == 14211) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = false;
			class46.objectSizeX = 2;
		}
		if (i == 14438 || i == 14437) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 486 || i == 15641 || i == 15644) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 26425 || i == 26427 || i == 26426 || i == 26428) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 14695) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
			class46.objectSizeX = 1;
		}
		if (i == 6775) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 734) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (i == 509 || i == 510 || i == 511) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 9292) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
			class46.objectSizeX = 1;
		}
		if (i == 9382) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 9381) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 9372) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 9360) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 24265) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (i == 9374) {
			class46.aBoolean779 = true;
			class46.aBoolean757 = true;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("flowerbed")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("jungle plant")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("creeping plant")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("flowers")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("sunflowers")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("flower")) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		if (class46.name != null && class46.name.equalsIgnoreCase("daisies") || i == 38692) {
			class46.aBoolean779 = false;
			class46.aBoolean757 = false;
		}
		return class46;
	}

	public static int getObjects() {
		return objects;
	}

	public static void loadConfig() {
		stream = new ByteStreamExt(getBuffer("loc.dat"));
		ByteStreamExt stream = new ByteStreamExt(getBuffer("loc.idx"));
		objects = stream.readUnsignedWord();
		streamIndices = new int[objects];
		int i = 2;
		for (int j = 0; j < objects; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ObjectDef[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new ObjectDef();
		}
		System.out.println("Loaded " + objects + " Objects.");
		System.gc();
	}

	public boolean aBoolean736;

	private byte aByte737;

	private int anInt738;

	public String name;

	private int anInt740;
	private byte aByte742;
	public int objectSizeX;
	private int anInt745;
	public int anInt746;
	int[] originalModelColors;
	private int anInt748;
	public int anInt749;
	private boolean aBoolean751;
	public int type;
	public boolean aBoolean757;
	public int anInt758;
	public int childrenIDs[];
	private int anInt760;
	public int objectSizeY;
	public boolean aBoolean762;
	public boolean aBoolean764;
	private boolean aBoolean766;
	public boolean aBoolean767;
	public int anInt768;
	private boolean aBoolean769;
	private int anInt772;
	int[] anIntArray773;
	public int anInt774;
	public int anInt775;
	int[] anIntArray776;
	public byte description[];
	public boolean hasActions;
	public boolean aBoolean779;
	public int anInt781;
	private int anInt783;
	int[] modifiedModelColors;
	public String actions[];

	private ObjectDef() {
		type = -1;
	}

	public boolean hasActions() {
		return hasActions || actions != null;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	private void readValues(ByteStreamExt buffer) {
		int i = -1;
		label0: do {
			int opcode;
			do {
				opcode = buffer.readUnsignedByte();
				if (opcode == 0)
					break label0;
				if (opcode == 1) {
					int k = buffer.readUnsignedByte();
					if (k > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = new int[k];
							anIntArray773 = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								anIntArray773[k1] = buffer.readUnsignedWord();
								anIntArray776[k1] = buffer.readUnsignedByte();
							}
						} else {
							buffer.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 3)
					description = buffer.readBytes();
				else if (opcode == 5) {
					int l = buffer.readUnsignedByte();
					if (l > 0)
						if (anIntArray773 == null || lowMem) {
							anIntArray776 = null;
							anIntArray773 = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								anIntArray773[l1] = buffer.readUnsignedWord();
						} else {
							;// buffer.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					objectSizeX = stream.readUnsignedByte();
				else if (opcode == 15)
					objectSizeY = stream.readUnsignedByte();
				else if (opcode == 17)
					aBoolean767 = false;
				else if (opcode == 18)
					aBoolean757 = false;
				else if (opcode == 19) {
					i = buffer.readUnsignedByte();
					if (i == 1)
						hasActions = true;
				} else if (opcode == 21)
					aBoolean762 = true;
				else if (opcode == 22)
					aBoolean769 = true;//
				else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					anInt781 = buffer.readUnsignedWord();
					if (anInt781 == 65535)
						anInt781 = -1;
				} else if (opcode == 28)
					anInt775 = buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readSignedByte();
				else if (opcode == 39)
					aByte742 = buffer.readSignedByte();
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					modifiedModelColors = new int[i1];
					originalModelColors = new int[i1];
					for (int i2 = 0; i2 < i1; i2++) {
						modifiedModelColors[i2] = buffer.readUnsignedWord();
						originalModelColors[i2] = buffer.readUnsignedWord();
					}
				} else if (opcode == 60)
					anInt746 = buffer.readUnsignedWord();
				else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedWord();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedWord();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedWord();
				else if (opcode == 68)
					anInt758 = buffer.readUnsignedWord();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readSignedWord();
				else if (opcode == 71)
					anInt745 = buffer.readSignedWord();
				else if (opcode == 72)
					anInt783 = buffer.readSignedWord();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74) {
					aBoolean766 = true;
				} else {
					if (opcode != 75)
						continue;
					anInt760 = buffer.readUnsignedByte();
				}
				continue label0;
			} while (opcode != 77);
			anInt774 = buffer.readUnsignedWord();
			if (anInt774 == 65535)
				anInt774 = -1;
			anInt749 = buffer.readUnsignedWord();
			if (anInt749 == 65535)
				anInt749 = -1;
			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 1];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedWord();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}

		} while (true);
		if (i == -1) {
			hasActions = anIntArray773 != null && (anIntArray776 == null || anIntArray776[0] == 10);
			if (actions != null)
				hasActions = true;
		}
		if (aBoolean766) {
			aBoolean767 = false;
			aBoolean757 = false;
		}
		if (anInt760 == -1)
			anInt760 = aBoolean767 ? 1 : 0;
	}

	private void setDefaults() {
		anIntArray773 = null;
		anIntArray776 = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		objectSizeX = 1;
		objectSizeY = 1;
		aBoolean767 = true;
		aBoolean757 = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}

	public int xLength() {
		return objectSizeX;
	}

	public int yLength() {
		return objectSizeY;
	}
}