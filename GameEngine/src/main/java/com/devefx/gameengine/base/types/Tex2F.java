package com.devefx.gameengine.base.types;

import com.devefx.gameengine.memory.Struct;

public class Tex2F extends Struct {
	
	public float u;
	public float v;
	
	public Tex2F() {
	}
	
	public Tex2F(float u, float v) {
		this.u = u;
		this.v = v;
	}
}
