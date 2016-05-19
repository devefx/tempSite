package com.devefx.edge.base.types;

import java.nio.ByteBuffer;

import com.devefx.edge.buffer.OutputBuffer;

public final class Vec3 implements OutputBuffer {
	
	public float x;
	public float y;
	public float z;
	
	public Vec3() {
	}

	public Vec3(Vec3 vec3) {
		x = vec3.x;
		y = vec3.y;
		z = vec3.z;
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
	
	@Override
	public Vec3 clone() {
		return new Vec3(this);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vec3 other = (Vec3) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}

}
