package com.devefx.gameengine.base.types;

import java.nio.Buffer;

import com.devefx.gameengine.memory.Struct;

public class Rect extends Struct {
	
	public Vec2 origin = new Vec2();
	public Size size = new Size();
	
	public Rect() {
	}
	
	public Rect(float x, float y, float width, float height) {
		setRect(x, y, width, height);
	}
	
	public void setRect(float x, float y, float width, float height) {
		origin.x = x;
		origin.y = y;
		size.width = width;
		size.height = height;
	}
	
	public float getMinX() {
		return origin.x;
	}
	public float getMidX() {
		return (origin.x + size.width) / 2;
	}
	public float getMaxX() {
		return origin.x + size.width;
	}
	public float getMinY() {
		return origin.y;
	}
	public float getMidY() {
		return (origin.y + size.height) / 2;
	}
	public float getMaxY() {
		return origin.y + size.height;
	}
	
	public boolean containsPoint(Vec2 point) {
		return point.x >= getMinX() && point.x <= getMaxX() && point.y >= getMinY() && point.y <= getMaxY();
	}
	
	public boolean intersectsRect(Rect rect) {
		return (getMaxX() < rect.getMinX() || getMinX() > rect.getMaxX() ||
				getMaxY() < rect.getMinY() || getMinY() > rect.getMaxY());
	}
	
	@Override
	public void write(Buffer buffer) {
		origin.write(buffer);
		size.write(buffer);
	}
}
