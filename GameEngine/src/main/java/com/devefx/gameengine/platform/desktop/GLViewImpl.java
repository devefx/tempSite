package com.devefx.gameengine.platform.desktop;

import java.awt.Insets;

import javax.swing.JFrame;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.GLView;
import com.jogamp.opengl.awt.GLCanvas;

public class GLViewImpl extends GLView {

	public static GLViewImpl create(String viewName, Rect rect) {
		GLViewImpl glViewImpl = new GLViewImpl();
		if (glViewImpl.initWithRect(viewName, rect)) {
			return glViewImpl;
		}
		return null;
	}
	
	protected boolean initWithRect(String viewName, Rect rect) {
		// set the view name
		setViewName(viewName);
		setFrameSize(rect.size.width, rect.size.height);
		// init canvas
		Director director = Director.getInstance();
		GLCanvas canvas = director.initGLCanvas((int) rect.size.width, (int) rect.size.height);
		// creating frame
		final JFrame frame = new JFrame(viewName);
		// adding canvas to frame
		frame.getContentPane().add(canvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		// calculate frame size
		frame.addNotify();
		Insets insets = frame.getInsets();
		int width = (int) rect.size.width + insets.left + insets.right;
		int height = (int) rect.size.height + insets.top + insets.bottom;
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		return true;
	}
}
