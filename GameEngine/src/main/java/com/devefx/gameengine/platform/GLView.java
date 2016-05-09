package com.devefx.gameengine.platform;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.base.types.Vec2;
import com.devefx.gameengine.renderer.GLStateCache.GL;
import com.jogamp.opengl.GL2;

public abstract class GLView {

	protected Rect viewPortRect;
	protected Size screenSize;
	protected Size designResolutionSize;
	protected String viewName;
	protected float scaleX = 1.0f;
	protected float scaleY = 1.0f;
	protected ResolutionPolicy resolutionPolicy;
	
	public GLView() {
		viewPortRect = new Rect();
		screenSize = new Size();
		designResolutionSize = new Size();
	}
	
	public abstract void end();
	public abstract boolean isOpenGLReady();
	public abstract void swapBuffers();
	public abstract void setCursorVisible(boolean isVisible);
	
	public Size getFrameSize() {
		return screenSize;
	}
	public void setFrameSize(float width, float height) {
		designResolutionSize = screenSize = new Size(width, height);
	}
	
	public Size getVisibleSize() {
		if (resolutionPolicy == ResolutionPolicy.NO_BORDER) {
			return new Size(screenSize.width / scaleX, screenSize.height / scaleY);
		}
		return designResolutionSize;
	}
	public Vec2 getVisibleOrigin() {
		if (resolutionPolicy == ResolutionPolicy.NO_BORDER) {
			return new Vec2((designResolutionSize.width - screenSize.width / scaleX) / 2, 
                    (designResolutionSize.height - screenSize.height / scaleY) / 2);
		}
		return Vec2.ZERO;
	}
	public Rect getVisibleRect() {
		Rect rect = new Rect();
		rect.origin = getVisibleOrigin();
		rect.size = getVisibleSize();
		return rect;
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public Rect getViewPortRect() {
		return viewPortRect;
	}
	public float getScaleX() {
		return scaleX;
	}
	public float getScaleY() {
		return scaleY;
	}
	public ResolutionPolicy getResolutionPolicy() {
		return resolutionPolicy;
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
		GL.getGL().glViewport((int) (x * scaleX + viewPortRect.getMinX()),
				(int) (y * scaleY + viewPortRect.getMinY()),
				(int) (width * scaleY),
				(int) (height * scaleY));
	}
	public void setScissorInPoints(float x, float y, float width, float height) {
		GL.getGL().glScissor((int) (x * scaleX + viewPortRect.getMinX()),
				(int) (y * scaleY + viewPortRect.getMinY()),
				(int) (width * scaleY),
				(int) (height * scaleY));
	}
	public boolean isScissorEnabled() {
		return GL.getGL().glIsEnabled(GL2.GL_SCISSOR_TEST);
	}
	public Rect getScissorRect() {
		float[] params = new float[4];
		GL.getGL().glGetFloatv(GL2.GL_SCISSOR_BOX, params, 0);
		float x = (params[0] - viewPortRect.origin.x) / scaleX;
		float y = (params[1] - viewPortRect.origin.y) / scaleY;
		return new Rect(x, y, params[2] / scaleX, params[3] / scaleY);
	}
	public void updateDesignResolutionSize() {
		if (screenSize.width > 0 && screenSize.height > 0
				&& designResolutionSize.width > 0 && designResolutionSize.height > 0) {
		
			scaleX = screenSize.width / designResolutionSize.width;
			scaleY = screenSize.height / designResolutionSize.height;
			
			if (resolutionPolicy == ResolutionPolicy.NO_BORDER) {
				scaleX = scaleY = (scaleX > scaleY ? scaleX : scaleY);
			} else if (resolutionPolicy == ResolutionPolicy.SHOW_ALL) {
				scaleX = scaleY = (scaleX < scaleY ? scaleX : scaleY);
			} else if (resolutionPolicy == ResolutionPolicy.FIXED_HEIGHT) {
				scaleX = scaleY;
				designResolutionSize.width = (float) Math.ceil(screenSize.width / scaleX);
			} else if (resolutionPolicy == ResolutionPolicy.FIXED_WIDTH) {
				scaleY = scaleX;
				designResolutionSize.height = (float) Math.ceil(screenSize.height / scaleY);
			}
			
			float viewPortW = designResolutionSize.width * scaleX;
			float viewPortH = designResolutionSize.height * scaleY;
			
			viewPortRect.setRect((screenSize.width - viewPortW) / 2,
					(screenSize.height - viewPortH) / 2, viewPortW, viewPortH);
			
			Director director = Director.getInstance();
			director.setWinSizeInPoints(getDesignResolutionSize());
			director.setDirty(true);
		}
	}
	
	public enum ResolutionPolicy {
		EXACT_FIT,
		NO_BORDER,
		SHOW_ALL,
		FIXED_HEIGHT,
		FIXED_WIDTH
	}
}
