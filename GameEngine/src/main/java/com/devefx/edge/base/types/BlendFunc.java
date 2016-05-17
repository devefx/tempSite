package com.devefx.edge.base.types;

import com.jogamp.opengl.GL2;

public final class BlendFunc {
	
	public int src;
	public int dst;
	
	public BlendFunc() {
	}
	
	public BlendFunc(BlendFunc blendFunc) {
		src = blendFunc.src;
		dst = blendFunc.dst;
	}
	
	public BlendFunc(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}
	
	@Override
	public BlendFunc clone() {
		return new BlendFunc(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dst;
		result = prime * result + src;
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
		BlendFunc other = (BlendFunc) obj;
		if (dst != other.dst)
			return false;
		if (src != other.src)
			return false;
		return true;
	}

	public static final BlendFunc DISABLE = new BlendFunc(GL2.GL_ONE, GL2.GL_ZERO);
	public static final BlendFunc ALPHA_PREMULTIPLIED = new BlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
	public static final BlendFunc ALPHA_NON_PREMULTIPLIED = new BlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	public static final BlendFunc ADDITIVE = new BlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
}
