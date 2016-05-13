package com.devefx.gameengine.platform.desktop;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.EventListenerAdapter;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.GLView;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class GLViewImpl extends GLView {

	public static GLViewImpl create(String viewName, Rect rect) {
		GLViewImpl glViewImpl = new GLViewImpl();
		if (glViewImpl.initWithRect(viewName, rect)) {
			return glViewImpl;
		}
		return null;
	}
	
	protected boolean initWithRect(String viewName, Rect rect) {
		// setting the view name and size
		setViewName(viewName);
		setFrameSize(rect.size.width, rect.size.height);
		// getting the capabilities object of GL2 profile
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas
		GLCanvas glcanvas = new GLCanvas(capabilities);
		glcanvas.addGLEventListener(Director.getInstance().glEventListener);
		glcanvas.setSize((int) rect.size.width, (int) rect.size.height);
		// Add evnet listener
		final EventListenerAdapter eventAdapter = new EventListenerAdapter();
		glcanvas.addMouseListener(eventAdapter);
		glcanvas.addMouseWheelListener(eventAdapter);
		glcanvas.addMouseMotionListener(eventAdapter);
		glcanvas.addKeyListener(eventAdapter);
		// 
		final Animator animator = new Animator(glcanvas);
		// The Windows
		Frame frame = new Frame(viewName);
		frame.setLayout(new BorderLayout());
		frame.setSize(glcanvas.getSize());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(glcanvas, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				animator.start();
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
		animator.start();
		return true;
	}

	@Override
	public void end() {
		
	}

	@Override
	public boolean isOpenGLReady() {
		return false;
	}

	@Override
	public void swapBuffers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCursorVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}
	
}
