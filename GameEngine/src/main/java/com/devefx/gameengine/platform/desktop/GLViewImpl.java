package com.devefx.gameengine.platform.desktop;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JFrame;

import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.GLView;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
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
		
		
		// getting the capabilities object of GL2 profile
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The canvas
		GLCanvas glcanvas = new GLCanvas(capabilities);
		glcanvas.setSize((int) rect.size.width, (int) rect.size.height);
		glcanvas.setIgnoreRepaint(true);
		glcanvas.setBackground(Color.RED);
		glcanvas.addGLEventListener(new GLEventListener() {
			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y, int width,
					int height) {
				
			}
			@Override
			public void init(GLAutoDrawable drawable) {
				System.out.println(1);
			}
			@Override
			public void dispose(GLAutoDrawable drawable) {
				
			}
			@Override
			public void display(GLAutoDrawable drawable) {
				drawable.getGL().getGL2().glClearColor(0, 0, 0, 0);
			}
		});
		// create frame
		JFrame frame = new JFrame(viewName);
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
		
		
		return true;
	}
}
