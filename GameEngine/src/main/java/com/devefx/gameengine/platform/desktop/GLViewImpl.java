package com.devefx.gameengine.platform.desktop;

import java.awt.Insets;

import javax.swing.JFrame;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.GLView;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class GLViewImpl extends GLView {

	private FPSAnimator animator;
	
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
		
		// getting the capabilities object of GL2 profile
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas
		GLCanvas glcanvas = new GLCanvas(capabilities);
		glcanvas.addGLEventListener(Director.getInstance().glEventListener);
		glcanvas.setIgnoreRepaint(true);
		glcanvas.setSize((int) rect.size.width, (int) rect.size.height);
		// creating frame
		final JFrame frame = new JFrame(viewName);
		// adding canvas to frame
		frame.getContentPane().add(glcanvas);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		// calculate frame size
		frame.addNotify();
		Insets insets = frame.getInsets();
		int width = glcanvas.getWidth() + insets.left + insets.right;
		int height = glcanvas.getHeight() + insets.top + insets.bottom;
		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// create animator
		animator = new FPSAnimator(glcanvas, 60, true);
		animator.start();
		
	/*	try {
			while (animator.isAnimating() && frame.isVisible()) {
				Thread.sleep(1);	
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		return true;
	}
}
