package com.devefx.gameengine.base.types;

import com.devefx.gameengine.memory.Struct;

public class BlendFunc extends Struct {
	
	public int src;
	public int dst;
	
	public BlendFunc() {
	}
	
	public BlendFunc(int src, int dst) {
		this.src = src;
		this.dst = dst;
	}
}
