package com.devefx.gameengine.base.types;

import java.nio.ByteBuffer;

import com.devefx.gameengine.buffer.OutputBuffer;

public class Vec2 implements OutputBuffer {
	
	public float x;
	public float y;
	
	public Vec2() {
	}
	
	public Vec2(Vec2 vec2) {
		assert(vec2 != null);
		set(vec2.x, vec2.y);
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vec2 other = (Vec2) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public Vec2 clone() {
		return new Vec2(this);
	}
	
	public static final Vec2 ZERO					= new Vec2(0.0f, 0.0f);
	public static final Vec2 ONE					= new Vec2(1.0f, 1.0f);
	public static final Vec2 UNIT_X					= new Vec2(1.0f, 0.0f);
	public static final Vec2 UNIT_Y					= new Vec2(0.0f, 1.0f);
	public static final Vec2 ANCHOR_MIDDLE			= new Vec2(0.5f, 0.5f);
	public static final Vec2 ANCHOR_BOTTOM_LEFT 	= new Vec2(0.0f, 0.0f);
	public static final Vec2 ANCHOR_TOP_LEFT		= new Vec2(0.0f, 1.0f);
	public static final Vec2 ANCHOR_BOTTOM_RIGHT	= new Vec2(1.0f, 0.0f);
	public static final Vec2 ANCHOR_TOP_RIGHT		= new Vec2(1.0f, 1.0f);
	public static final Vec2 ANCHOR_MIDDLE_RIGHT	= new Vec2(1.0f, 0.5f);
	public static final Vec2 ANCHOR_MIDDLE_LEFT		= new Vec2(0.0f, 0.5f);
	public static final Vec2 ANCHOR_MIDDLE_TOP		= new Vec2(0.5f, 1.0f);
	public static final Vec2 ANCHOR_MIDDLE_BOTTOM	= new Vec2(0.5f, 0.0f);
}
