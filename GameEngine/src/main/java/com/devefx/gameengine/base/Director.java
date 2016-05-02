package com.devefx.gameengine.base;

import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.renderer.Renderer;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Director {

	protected GLView openGLView;
	protected Renderer renderer;
	
	protected Size winSizeInPoints;
	
	private static Director director;
	
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
		}
		return director;
	}
	
	public boolean init() {
		renderer = new Renderer();
		
		return true;
	}
	
	public void setOpenGLView(GLView openGLView) {
		if (this.openGLView != openGLView) {
			
			this.openGLView = openGLView;
			
			if (openGLView != null) {
				winSizeInPoints = openGLView.getDesignResolutionSize();
				setGLDefaultValues();
			}
		}
	}
	
	public GLView getOpenGLView() {
		return openGLView;
	}
	public Size getWinSizeInPoints() {
		return winSizeInPoints;
	}
	public void setWinSizeInPoints(Size winSizeInPoints) {
		this.winSizeInPoints = winSizeInPoints;
	}
	
	public void setGLDefaultValues() {
		// TODO Auto-generated method stub
		System.out.println(1);
	}
	
	public final GLEventListener glEventListener = new GLEventListener() {
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			
		}
		@Override
		public void init(GLAutoDrawable drawable) {
			renderer.initGLView();
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			renderer.render();
		}
	};
}
