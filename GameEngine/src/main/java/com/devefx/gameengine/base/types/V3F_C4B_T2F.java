package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class V3F_C4B_T2F implements OutputBuffer {
	
	public Vec3 vertices ;		// 12 bytes
	public Color4B colors;		//  4 bytes
	public Tex2F texCoords;		//  8 bytes
	
	public V3F_C4B_T2F() {
		vertices = new Vec3();
		colors = new Color4B();
		texCoords = new Tex2F();
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		vertices.write(buffer);
		colors.write(buffer);
		texCoords.write(buffer);
	}
	
	@Override
	public V3F_C4B_T2F clone() {
		V3F_C4B_T2F v = new V3F_C4B_T2F();
		v.vertices = vertices.clone();
		v.colors = colors.clone();
		v.texCoords = texCoords.clone();
		return v;
	}
}
