package com.devefx.gameengine.base.types;

import com.devefx.gameengine.memory.Struct;

public class Color4F extends Struct {
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color4F() {
	}
	
	public Color4F(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public static final Color4F WHITE	= new Color4F(   1f,    1f,    1f, 1);
	public static final Color4F YELLOW	= new Color4F(   1f,    1f,    0f, 1);
	public static final Color4F BLUE	= new Color4F(   0f,    1f,    0f, 1);
	public static final Color4F GREEN	= new Color4F(   0f,    0f,    1f, 1);
	public static final Color4F RED		= new Color4F(   1f,    0f,    0f, 1);
	public static final Color4F MAGENTA	= new Color4F(   1f,    0f,    1f, 1);
	public static final Color4F BLACK	= new Color4F(   0f,    0f,    0f, 1);
	public static final Color4F ORANGE	= new Color4F(   1f,  0.5f,    0f, 1);
	public static final Color4F GRAY	= new Color4F(0.65f, 0.65f, 0.65f, 1);
}
