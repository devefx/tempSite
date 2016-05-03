package com.devefx.gameengine.base.types;

import com.devefx.gameengine.memory.Struct;

public class Size extends Struct {
	
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
}
