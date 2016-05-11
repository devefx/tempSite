package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class Tex2F implements OutputBuffer {
	
	public float u;
	public float v;
	
	public Tex2F() {
	}
	
	public Tex2F(Tex2F tex2f) {
		u = tex2f.u;
		v = tex2f.v;
	}
	
	public Tex2F(float u, float v) {
		this.u = u;
		this.v = v;
	}

	@Override
	public Tex2F clone() {
		return new Tex2F(this);
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		buffer.putFloat(u);
		buffer.putFloat(v);
	}
}
