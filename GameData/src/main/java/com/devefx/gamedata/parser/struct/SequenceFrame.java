package com.devefx.gamedata.parser.struct;

public class SequenceFrame {

	public int width;
	public int height;
	public int group;
	public int frame;
	
	public int centerX[][];
	public int centerY[][];
	public int pixels[][];
	
	public SequenceFrame(int width, int height, int group, int frame) {
		this.width = width;
		this.height = height;
		this.group = group;
		this.frame = frame;
		centerX = new int[group][frame];
		centerY = new int[group][frame];
		pixels = new int[height * group][width * frame];
	}
	
}
