package com.devefx.gameengine.base.types;

import com.devefx.gameengine.memory.Struct;

public class Vec2 extends Struct {
	
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
}
