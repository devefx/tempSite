package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class Color4B implements OutputBuffer {
	
	public byte r;
	public byte g;
	public byte b;
	public byte a;
	
	public Color4B() {
	}
	
	public Color4B(int r, int g, int b, int a) {
		this.r = (byte) r;
		this.g = (byte) g;
		this.b = (byte) b;
		this.a = (byte) a;
	}
	
	public Color4B(int color) {
		r = (byte) ((color >> 16) & 0xff);
		g = (byte) ((color >> 8) & 0xff);
		b = (byte) ((color >> 0) & 0xff);
		a = (byte) ((color >> 24) & 0xff);
	}

	@Override
	public void write(ByteBuffer buffer) {
		buffer.put(r);
		buffer.put(g);
		buffer.put(b);
		buffer.put(a);
	}
	
	public static final Color4B WHITE	= new Color4B(255, 255, 255, 255);
	public static final Color4B YELLOW	= new Color4B(255, 255,   0, 255);
	public static final Color4B BLUE	= new Color4B(  0, 255,   0, 255);
	public static final Color4B GREEN	= new Color4B(  0,   0, 255, 255);
	public static final Color4B RED		= new Color4B(255,   0,   0, 255);
	public static final Color4B MAGENTA	= new Color4B(255,   0, 255, 255);
	public static final Color4B BLACK	= new Color4B(  0,   0,   0, 255);
	public static final Color4B ORANGE	= new Color4B(255, 127,   0, 255);
	public static final Color4B GRAY	= new Color4B(166, 166, 166, 255);
}
