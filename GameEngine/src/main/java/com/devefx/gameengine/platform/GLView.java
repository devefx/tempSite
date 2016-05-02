package com.devefx.gameengine.platform;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.base.types.Size;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLContext;

public abstract class GLView {

	protected Rect viewPortRect;
	protected Size screenSize;
	protected String viewName;
	protected float scaleX = 1.0f;
	protected float scaleY = 1.0f;
	
	protected Size designResolutionSize;
	
	public GLView() {
		viewPortRect = new Rect();
		screenSize = new Size();
		designResolutionSize = new Size();
	}
	
	public Size getFrameSize() {
		return screenSize;
	}
	public void setFrameSize(float width, float height) {
		designResolutionSize = screenSize = new Size(width, height);
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public float getScaleX() {
		return scaleX;
	}
	public float getScaleY() {
		return scaleY;
	}
	public Size getDesignResolutionSize() {
		return designResolutionSize;
	}
	public void setDesignResolutionSize(float width, float height) {
		if (width == 0.0f || height == 0.0f) {
			return;
		}
		designResolutionSize.setSize(width, height);
		updateDesignResolutionSize();
	}
	public void setViewPortInPoints(float x, float y, float width, float height) {
		GL gl = GLContext.getCurrentGL().getGL2();
		gl.glViewport((int) (x * scaleX + viewPortRect.getMinX()),
				(int) (y * scaleY + viewPortRect.getMinY()),
				(int) (width * scaleY),
				(int) (height * scaleY));
	}
	public void setScissorInPoints(float x, float y, float width, float height) {
		GL gl = GLContext.getCurrentGL().getGL2();
		gl.glScissor((int) (x * scaleX + viewPortRect.getMinX()),
				(int) (y * scaleY + viewPortRect.getMinY()),
				(int) (width * scaleY),
				(int) (height * scaleY));
	}
	public boolean isScissorEnabled() {
		GL gl = GLContext.getCurrentGL().getGL2();
		return gl.glIsEnabled(GL.GL_SCISSOR_TEST);
	}
	public Rect getScissorRect() {
		GL gl = GLContext.getCurrentGL().getGL2();
		float[] params = new float[4];
		gl.glGetFloatv(GL.GL_SCISSOR_BOX, params, 0);
		float x = (params[0] - viewPortRect.origin.x) / scaleX;
		float y = (params[1] - viewPortRect.origin.y) / scaleY;
		return new Rect(x, y, params[2] / scaleX, params[3] / scaleY);
	}
	protected void updateDesignResolutionSize() {
		if (screenSize.width > 0 && screenSize.height > 0
				&& designResolutionSize.width > 0 && designResolutionSize.height > 0) {
		
			scaleX = screenSize.width / designResolutionSize.width;
			scaleY = screenSize.height / designResolutionSize.height;
			
			scaleX = scaleY = (scaleX < scaleY ? scaleX : scaleY);
			
			float viewPortW = designResolutionSize.width * scaleX;
			float viewPortH = designResolutionSize.height * scaleY;
			
			viewPortRect.setRect((screenSize.width - viewPortW) / 2,
					(screenSize.height - viewPortH) / 2, viewPortW, viewPortH);
			
			Director director = Director.getInstance();
			director.setWinSizeInPoints(getDesignResolutionSize());
			director.setGLDefaultValues();
		}
	}
}
