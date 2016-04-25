package com.devefx.gamedata.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferReader extends FilterInputStream {
	
	public static final int CUR = 0;
	public static final int BEG = 1;
	public static final int END = 2;
	
	private long pos;
	private long mark;
	
	public BufferReader(InputStream in) {
		super(in);
	}
	
	@Override
	public int read() throws IOException {
		pos ++;
		return in.read();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		pos += len;
		return in.read(b, off, len);
	}
	
	public boolean readBoolean() throws IOException {
		return read() != 0;
	}
	
	public byte readByte() throws IOException {
		return (byte) read();
	}
	
	public char readChar() throws IOException {
		return (char) readShort();
	}
	
	public short readShort() throws IOException {
		return (short) (read() + (read() << 8));
	}
	
	public int readInt() throws IOException {
		return read() + (read() << 8) + (read() << 16) + (read() << 24);
	}
	
	public long readLong() throws IOException {
		return readInt() + (readInt() << 32);
	}
	
	public String readUTF() throws IOException {
		byte[] buff = new byte[readInt()];
		if (read(buff) > 0) {
			return new String(buff, "utf-8");
		}
		return null;
	}
	
	public void seekg(long off, int dir) throws IOException {
		if (dir == BEG) {
			in.skip(off - this.pos);
			this.pos = off;
		} else if (dir == END) {
			in.skip(available() - pos + off);
			this.pos = available() + off;
		} else {
			in.skip(off);
			this.pos += off;
		}
	}
	
	public long getPos() {
		return pos;
	}
	
	public void mark() {
		this.mark = pos;
	}
	
	public void reset() throws IOException {
		seekg(mark, BEG);
	}
}
