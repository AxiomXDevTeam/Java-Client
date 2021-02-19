package net.axiomx.compression;

import java.util.List;

public interface Decompressor {

	public List<String> decompress(byte [] l, int len, int start);

	public int version();
}
