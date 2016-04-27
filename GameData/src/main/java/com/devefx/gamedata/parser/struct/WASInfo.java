package com.devefx.gamedata.parser.struct;

public class WASInfo {
	
	public short flag;
	public short headerLen;
	public short group;
	public short frame;
	public short width;
	public short height;
	public short centerX;
	public short centerY;
	public short palette[];
	public int offset[][];
	
	public WASInfo() {
		this.palette = new short[256];
	}
}
