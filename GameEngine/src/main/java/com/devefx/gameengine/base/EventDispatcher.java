package com.devefx.gameengine.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDispatcher {
	
	protected Map<String, List<EventListener>> listenerMap;
	protected List<EventListener> listeners;
	protected boolean isEnabled;
	
	public EventDispatcher() {
		listenerMap = new HashMap<String, List<EventListener>>();
		listeners = new ArrayList<EventListener>();
		isEnabled = true;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public void addEventListener(EventListener listener) {
		listeners.add(listener);
	}
	
	public void removeEventListener(EventListener listener) {
		listeners.remove(listener);
	}
	
	public void dispatchEvent(Event event) {
		if (isEnabled) {
			
			String listenerID = getListenerID(event);
			if (listenerID != null) {

				List<EventListener> listeners = listenerMap.get(listenerID);
				if (listeners != null) {
					for (EventListener listener : listeners) {
						listener.onEvent(event);
						if (event.isStopped()) {
							break;
						}
					}
				}
				
			}
			
		}
	}
	
	protected String getListenerID(Event event) {
		if (event instanceof EventMouse) {
			return "__mouse";
		} else if (event instanceof EventKeyboard) {
			return "__keyboard";
		} else if (event instanceof EventTouch) {
			return "__touch";
		}
		return null;
	}
}
