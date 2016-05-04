package com.devefx.test;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JFrame;

import com.devefx.test.demo.BasicFrame;
import com.devefx.test.demo.CubeObject;
import com.devefx.test.demo.GLRenderer;
import com.devefx.test.demo.Triangle;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class OpenGLTest {
	
	public static void main(String[] args) throws InterruptedException {
		// getting the capabilities object of GL2 profile
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		// The Windows
		GLWindow window = GLWindow.create(capabilities);
		window.setSize(800, 600);
		window.setTitle("OpenGL");
		window.addGLEventListener(new VBORendererBak());
		window.setVisible(true);
		
		
		
		FPSAnimator animator = new FPSAnimator(60);
		animator.setUpdateFPSFrames(60, System.err);
		animator.add(window);
		animator.start();
		
		while (animator.isAnimating() && window.isVisible()) {
			Thread.sleep(100);
		}
		animator.stop();
		window.destroy();
		
/*		// The canvas
		GLCanvas glcanvas = new GLCanvas(capabilities);
		glcanvas.addGLEventListener(new Triangle());
		glcanvas.setIgnoreRepaint(true);
		glcanvas.setBackground(Color.BLACK);
		glcanvas.setSize(800, 600);
		// creating frame
		final JFrame frame = new JFrame("OpenGL");
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
		FPSAnimator animator = new FPSAnimator(glcanvas, 300, true);
		animator.start();*/
	}
	
}
