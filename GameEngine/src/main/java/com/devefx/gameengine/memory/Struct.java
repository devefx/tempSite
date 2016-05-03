package com.devefx.gameengine.memory;

import java.nio.Buffer;

import com.devefx.gameengine.base.types.Types;

public abstract class Struct {
	
	private static final int HEAD_SIZE = 12;
	
	public void write(Buffer buffer) {
		Memory.copy(this, HEAD_SIZE, buffer, Memory.ix(buffer), Types.sizeof(getClass()));
	}
}
