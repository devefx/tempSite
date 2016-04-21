package com.devefx.gameengine.base;

public abstract class Color {
	
	protected float r;
	protected float g;
	protected float b;
	protected float a;
	
	public Color() {
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public float getRed() {
		return r;
	}
	
	public float getGreen() {
		return g;
	}
	
	public float getBlue() {
		return b;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(a);
		result = prime * result + Float.floatToIntBits(b);
		result = prime * result + Float.floatToIntBits(g);
		result = prime * result + Float.floatToIntBits(r);
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
		Color other = (Color) obj;
		if (Float.floatToIntBits(a) != Float.floatToIntBits(other.a))
			return false;
		if (Float.floatToIntBits(b) != Float.floatToIntBits(other.b))
			return false;
		if (Float.floatToIntBits(g) != Float.floatToIntBits(other.g))
			return false;
		if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r))
			return false;
		return true;
	}

	
	
	public static class Color3B extends Color {
		
		public Color3B() {
		}
		
		public Color3B(float r, float g, float b) {
			super(r, g, b, 0);
		}
		
		public static final Color3B WHITE	= new Color3B(255, 255, 255);
		public static final Color3B YELLOW	= new Color3B(255, 255,   0);
		public static final Color3B GREEN	= new Color3B(  0, 255,   0);
		public static final Color3B BLUE	= new Color3B(  0,   0, 255);
		public static final Color3B RED		= new Color3B(255,   0,   0);
		public static final Color3B MAGENTA	= new Color3B(255,   0, 255);
		public static final Color3B BLACK	= new Color3B(  0,   0,   0);
		public static final Color3B ORANGE	= new Color3B(255, 127,   0);
		public static final Color3B GRAY	= new Color3B(166, 166, 166);
	}
	
	public static class Color4B extends Color {
		
		public Color4B() {
		}
		
		public Color4B(float r, float g, float b, float a) {
			super(r, g, b, a);
		}
		
		public float getAlpha() {
			return a;
		}
		
		public static final Color4B WHITE	= new Color4B(255, 255, 255, 255);
		public static final Color4B YELLOW	= new Color4B(255, 255,   0, 255);
		public static final Color4B GREEN	= new Color4B(  0, 255,   0, 255);
		public static final Color4B BLUE	= new Color4B(  0,   0, 255, 255);
		public static final Color4B RED		= new Color4B(255,   0,   0, 255);
		public static final Color4B MAGENTA	= new Color4B(255,   0, 255, 255);
		public static final Color4B BLACK	= new Color4B(  0,   0,   0, 255);
		public static final Color4B ORANGE	= new Color4B(255, 127,   0, 255);
		public static final Color4B GRAY	= new Color4B(166, 166, 166, 255);
	}
	
	public static class Color4F extends Color {

		public Color4F() {
		}
		
		public Color4F(float r, float g, float b, float a) {
			super(r, g, b, a);
		}
		
		public float getAlpha() {
			return a;
		}
		
		public static final Color4F WHITE	= new Color4F(1, 1, 1, 1);
		public static final Color4F YELLOW	= new Color4F(1, 1, 0, 1);
		public static final Color4F GREEN	= new Color4F(0, 1, 0, 1);
		public static final Color4F BLUE	= new Color4F(0, 0, 1, 1);
		public static final Color4F RED		= new Color4F(1, 0, 0, 1);
		public static final Color4F MAGENTA	= new Color4F(1, 0, 1, 1);
		public static final Color4F BLACK	= new Color4F(0, 0, 0, 1);
		public static final Color4F ORANGE	= new Color4F(1, 0.5f, 0, 1);
		public static final Color4F GRAY	= new Color4F(0.65f, 0.65f, 0.65f, 1);
	}
}
