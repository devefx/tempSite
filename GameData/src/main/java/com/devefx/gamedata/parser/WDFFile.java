package com.devefx.gamedata.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

public class WDFFile {
	
	private RandomAccessFile file;
	private FileChannel channel;
	private MappedByteBuffer buffer;
	
	private int number;
	private int offset;
	private Map<Integer, DataBlock> blocks;
	
	public boolean open(String filename) throws IOException {
		try {
			file = new RandomAccessFile(filename, "rw");
			channel = file.getChannel();
			buffer = channel.map(MapMode.READ_WRITE, 0, file.length());
			if (readSwappedInt() == 1464092240) {
				process();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void close() throws IOException {
		channel.close();
		file.close();
	}
	
	public MappedByteBuffer getBuffer() {
		return buffer;
	}
	
	public byte[] read(int id) {
		DataBlock block = blocks.get(id);
		if (block != null) {
			byte[] dst = new byte[block.length];
			buffer.position(block.offset);
			buffer.get(dst);
			return dst;
		}
		return null;
	}
	
	public DataBlock getDataBlock(int id) {
		return blocks.get(id);
	}
	
	int readSwappedInt() {
		return (buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8) 
				+ ((buffer.get() & 0xFF) << 16) + ((buffer.get() & 0xFF) << 24);
	}
	
	short readSwappedShort() {
		return (short) ((buffer.get() & 0xFF) + ((buffer.get() & 0xFF) << 8));
	}
	
	void process() {
		number = readSwappedInt();
		offset = readSwappedInt();
		blocks = new HashMap<Integer, DataBlock>(number);
		
		buffer.position(offset);
		for (int i = 0; i < number; i++) {
			DataBlock block = new DataBlock();
			block.id = readSwappedInt();
			block.offset = readSwappedInt();
			block.length = readSwappedInt();
			block.space = readSwappedInt();
			// assertType
			int pos = buffer.position();
			buffer.position(block.offset);
			block.type = assertType(readSwappedShort());
			buffer.position(pos);
			// push
			blocks.put(block.id, block);
		}
	}
	
	String assertType(short data) {
		switch (data & 0xFFFF) {
		case 0x5053:
			return "WAS";
		case 0x4D42:
			return "BMP";
		case 0x4952:
			return "WAV";
		case 0xD8FF:
			return "JPEG";
		case 0xF2FF:
			return "MP3";
		}
		return "UNKNOWN";
	}
	
	class DataBlock {
		int id;
		int offset;
		int length;
		int space;
		String type;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		WDFFile file = new WDFFile();
		
		if (file.open("F:\\梦幻西游\\addon.wdf")) {
			
			file.read(0x4F8312F6);
			
		}
	}
}
