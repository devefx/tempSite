package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class Vec2 implements OutputBuffer {
	
	public float x;
	public float y;
	
	public Vec2() {
	}
	
	public Vec2(float x, float y) {
		set(x, y);
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Vec2 v) {
		x += v.x;
		y += v.y;
	}
	
	public void subtract(Vec2 v) {
		x -= v.x;
		y -= v.y;
	}
	
	public void negate() {
		x = -x;
		y = -y;
	}
	
	public void scale(float scalar) {
		x *= scalar;
		y *= scalar;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	@Override
	public void write(ByteBuffer buffer) {
		buffer.putFloat(x);
		buffer.putFloat(y);
	}
}
