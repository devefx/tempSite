package com.devefx.gamedata.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.input.SwappedDataInputStream;

public class FileDataInputStream extends SwappedDataInputStream {

	private long pos;
	private long markPos;
	
	public FileDataInputStream(InputStream input) {
		super(input);
	}

	@Override
	public boolean readBoolean() throws IOException, EOFException {
		pos += 1;
		return super.readBoolean();
	}

	@Override
	public byte readByte() throws IOException, EOFException {
		pos += 1;
		return super.readByte();
	}

	@Override
	public char readChar() throws IOException, EOFException {
		pos += 2;
		return super.readChar();
	}

	@Override
	public double readDouble() throws IOException, EOFException {
		pos += 8;
		return super.readDouble();
	}

	@Override
	public float readFloat() throws IOException, EOFException {
		pos += 4;
		return super.readFloat();
	}

	@Override
	public void readFully(byte[] data) throws IOException, EOFException {
		pos += data.length;
		super.readFully(data);
	}

	@Override
	public void readFully(byte[] data, int offset, int length)
			throws IOException, EOFException {
		pos += length;
		super.readFully(data, offset, length);
	}

	@Override
	public int readInt() throws IOException, EOFException {
		pos += 4;
		return super.readInt();
	}

	@Override
	public long readLong() throws IOException, EOFException {
		pos += 8;
		return super.readLong();
	}

	@Override
	public short readShort() throws IOException, EOFException {
		pos += 2;
		return super.readShort();
	}

	@Override
	public int readUnsignedByte() throws IOException, EOFException {
		pos += 1;
		return super.readUnsignedByte();
	}

	@Override
	public int readUnsignedShort() throws IOException, EOFException {
		pos += 2;
		return super.readUnsignedShort();
	}

	@Override
	public int read() throws IOException {
		pos += 1;
		return super.read();
	}

	@Override
	public int read(byte[] bts) throws IOException {
		pos += bts.length;
		return super.read(bts);
	}

	@Override
	public int read(byte[] bts, int off, int len) throws IOException {
		pos += len;
		return super.read(bts, off, len);
	}
	
	public void mark() {
		this.markPos = pos;
	}
	
	@Override
	public void reset() throws IOException {
		seekg(markPos);
	}
	
	public void seekg(long pos) throws IOException {
		super.skip(pos - this.pos);
		this.pos = pos;
	}
	
	public long getPos() {
		return pos;
	}
}
