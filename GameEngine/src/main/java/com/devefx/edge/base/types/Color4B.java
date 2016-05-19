package com.devefx.edge.base.types;

import java.nio.ByteBuffer;

import com.devefx.edge.buffer.OutputBuffer;

public final class Color4B implements OutputBuffer {
	
	public byte r;
	public byte g;
	public byte b;
	public byte a;
	
	public Color4B() {
	}
	
	public Color4B(Color4B color) {
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
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
	
	@Override
	public Color4B clone() {
		return new Color4B(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Color4B other = (Color4B) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
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
