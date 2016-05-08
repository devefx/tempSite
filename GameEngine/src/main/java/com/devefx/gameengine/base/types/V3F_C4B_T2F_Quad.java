package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class V3F_C4B_T2F_Quad implements OutputBuffer {
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
	public void write(ByteBuffer buffer) {
		bl.write(buffer);
		br.write(buffer);
		tl.write(buffer);
		tr.write(buffer);
	}
}
