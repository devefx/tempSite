package com.devefx.gameengine.base;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.devefx.gameengine.base.EventMouse.MouseEventType;

public class EventListenerAdapter implements MouseListener, MouseMotionListener, MouseWheelListener,
					KeyListener {

	protected EventDispatcher dispatcher;
	
	public EventListenerAdapter() {
		dispatcher = Director.getInstance().getEventDispatcher();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		EventKeyboard event = new EventKeyboard(e.getKeyCode(), true);
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		EventKeyboard event = new EventKeyboard(e.getKeyCode(), false);
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		EventMouse event = new EventMouse(MouseEventType.MOUSE_SCROLL);
		event.setCursorPosition(e.getX(), e.getY());
		event.setWheelRotation(e.getWheelRotation());
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		EventMouse event = new EventMouse(MouseEventType.MOUSE_MOVE);
		event.setCursorPosition(e.getX(), e.getY());
		event.setMouseButton(e.getButton());
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		EventMouse event = new EventMouse(MouseEventType.MOUSE_CLICK);
		event.setCursorPosition(e.getX(), e.getY());
		event.setMouseButton(e.getButton());
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		EventMouse event = new EventMouse(MouseEventType.MOUSE_DOWN);
		event.setCursorPosition(e.getX(), e.getY());
		event.setMouseButton(e.getButton());
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		EventMouse event = new EventMouse(MouseEventType.MOUSE_UP);
		event.setCursorPosition(e.getX(), e.getY());
		event.setMouseButton(e.getButton());
		dispatcher.dispatchEvent(event);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
}
