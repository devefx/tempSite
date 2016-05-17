package com.devefx.edge.base.types;

public final class Color4F {
	
	public float r;
	public float g;
	public float b;
	public float a;
	
	public Color4F() {
	}
	
	public Color4F(Color4F color) {
		r = color.r;
		g = color.g;
		b = color.b;
		a = color.a;
	}
	
	public Color4F(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	@Override
	protected Color4F clone() {
		return new Color4F(this);
	};
	
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
		Color4F other = (Color4F) obj;
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
