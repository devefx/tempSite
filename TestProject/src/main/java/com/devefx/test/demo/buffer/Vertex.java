package com.devefx.test.demo.buffer;

import com.jogamp.common.nio.Buffers;

public class Vertex {
	
	public static final int SIZE = Buffers.SIZEOF_FLOAT * 8 + Buffers.SIZEOF_LONG;
	
	public float x, y, z;
	public float nx, ny, nz;
	public long col;
	public float tx, ty;
	
}
