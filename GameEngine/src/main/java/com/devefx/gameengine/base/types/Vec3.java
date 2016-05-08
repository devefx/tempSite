package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class Vec3 implements OutputBuffer {
	
	public float x;
	public float y;
	public float z;
	
	public Vec3() {
	}

	public Vec3(float x, float y, float z) {
		set(x, y, z);
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void add(Vec3 v) {
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public void subtract(Vec3 v) {
		x -= v.x;
		y -= v.y;
		z -= v.z;
	}
	
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public void scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	@Override
	public void write(ByteBuffer buffer) {
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.putFloat(z);
	}
}
