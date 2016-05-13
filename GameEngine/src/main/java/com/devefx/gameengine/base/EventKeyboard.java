package com.devefx.gameengine.base;

public class EventKeyboard extends Event {

	protected int keyCode;
	protected boolean isPressed;
	
	public EventKeyboard(int keyCode, boolean isPressed) {
		this.keyCode = keyCode;
		this.isPressed = isPressed;
	}
	
}
