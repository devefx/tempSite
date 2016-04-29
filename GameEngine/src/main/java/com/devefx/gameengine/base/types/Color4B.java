package com.devefx.gameengine.base.types;

public class Color4B {
	
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
}
