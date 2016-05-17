package com.devefx.edge.base.types;

public final class Size {
	
	public float width;
	public float height;
	
	public Size() {
	}
	
	public Size(Size size) {
		assert(size != null);
		setSize(size.width, size.height);
	}
	
	public Size(float width, float height) {
		setSize(width, height);
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Size clone() {
		return new Size(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
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
		Size other = (Size) obj;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}

	public static final Size ZERO = new Size(0.0f, 0.0f);
}
