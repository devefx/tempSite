package com.devefx.gameengine.base.types;

import com.jogamp.opengl.GL2;

public class BlendFunc {
	
	public int src;
	public int dst;
	
	public BlendFunc() {
	}
	
	public BlendFunc(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}
	
	public static final BlendFunc DISABLE = new BlendFunc(GL2.GL_ONE, GL2.GL_ZERO);
	public static final BlendFunc ALPHA_PREMULTIPLIED = new BlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
	public static final BlendFunc ALPHA_NON_PREMULTIPLIED = new BlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
	public static final BlendFunc ADDITIVE = new BlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
}
