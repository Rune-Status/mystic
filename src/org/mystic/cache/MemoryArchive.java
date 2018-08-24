package org.mystic.cache;

public class MemoryArchive {

	private static final int INDEX_DATA_CHUNK_SIZE = 12;

	private final ByteStream cache;
	private final ByteStream index;

	public MemoryArchive(final ByteStream cache, final ByteStream index) {
		this.cache = cache;
		this.index = index;
	}

	public final int contentSize() {
		return index.length() / 12;
	}

	public final byte[] get(int dataIndex) {
		try {
			if (index.length() < (dataIndex * INDEX_DATA_CHUNK_SIZE)) {
				return null;
			}
			index.setOffset(dataIndex * INDEX_DATA_CHUNK_SIZE);
			long fileOffset = index.getLong();
			int fileSize = index.getInt();
			cache.setOffset(fileOffset);
			byte[] buffer = cache.read(fileSize);
			return buffer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}