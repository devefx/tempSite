package com.devefx.gameengine.base.types;

import java.nio.Buffer;

import com.devefx.gameengine.memory.Struct;

public class V3F_C4B_T2F extends Struct {
	
	public Vec3 vertices ;		// 12 bytes
	public Color4B colors;		//  4 bytes
	public Tex2F texCoords;		//  8 bytes
	
	public V3F_C4B_T2F() {
		vertices = new Vec3();
		colors = new Color4B();
		texCoords = new Tex2F();
	}
	
	@Override
	public void write(Buffer buffer) {
		vertices.write(buffer);
		colors.write(buffer);
		texCoords.write(buffer);
	}
}
