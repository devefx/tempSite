package com.devefx.gameengine.base.types;

public class Size {
	
	public float width;
	public float height;
	
	public Size() {
	}
	
	public Size(float width, float height) {
		setSize(width, height);
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public static final Size ZERO = new Size(0.0f, 0.0f);
}
