package com.devefx.gameengine.base;

public class EventTouch extends Event {
	
	protected EventCode eventCode;
	
	
	public enum EventCode {
		BEGAN,
        MOVED,
        ENDED,
        CANCELLED
	}
}
