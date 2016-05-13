package com.devefx.gameengine.base;

public class EventMouse extends Event {

	protected float x;
	protected float y;
	protected int mouseButton;
	protected MouseEventType mouseEventType;
	protected int wheelRotation;
	
	public EventMouse(MouseEventType mouseEventType) {
		this.mouseEventType = mouseEventType;
	}
	
	public void setCursorPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getCursorX() {
		return x;
	}
	
	public float getCursorY() {
		return y;
	}
	
	public void setMouseButton(int mouseButton) {
		this.mouseButton = mouseButton;
	}
	
	public int getWheelRotation() {
		return wheelRotation;
	}
	
	public void setWheelRotation(int wheelRotation) {
		this.wheelRotation = wheelRotation;
	}
	
	public enum MouseEventType {
		MOUSE_DOWN,
		MOUSE_UP,
		MOUSE_CLICK,
		MOUSE_MOVE,
		MOUSE_SCROLL
	}
}
