package com.devefx.gameengine.base.types;

import java.nio.Buffer;

import com.devefx.gameengine.memory.Struct;

public class V3F_C4B_T2F_Quad extends Struct {
	/**
	 * bottom left
	 */
	public V3F_C4B_T2F bl = new V3F_C4B_T2F();
	/**
	 * bottom right
	 */
	public V3F_C4B_T2F br = new V3F_C4B_T2F();
	/**
	 * top left
	 */
	public V3F_C4B_T2F tl = new V3F_C4B_T2F();
	/**
	 * top right
	 */
	public V3F_C4B_T2F tr = new V3F_C4B_T2F();
	
	@Override
	public void write(Buffer buffer) {
		bl.write(buffer);
		br.write(buffer);
		tl.write(buffer);
		tr.write(buffer);
	}
}
