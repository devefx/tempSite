package com.devefx.edge.platform;

import com.devefx.edge.base.Director;
import com.devefx.edge.base.types.Rect;
import com.devefx.edge.base.types.Size;
import com.devefx.edge.base.types.Vec2;
import com.devefx.edge.renderer.GLStateCache.GL;
import com.jogamp.opengl.GL2;

public abstract class GLView {
	/**
	 * 分辨率适配策略
	 */
	public enum ResolutionPolicy {
		/**
		 * 屏幕宽与设计宽比作为X方向的缩放因子，屏幕高与设计高比作为Y方向的缩放因子。保证了设计区域完全铺满屏幕，但是可能会出现图像拉伸。
		 */
		EXACT_FIT,
		/**
		 * 屏幕宽、高分别和设计分辨率宽、高计算缩放因子，取较(大)者作为宽、高的缩放因子。保证了设计区域总能一个方向上铺满屏幕，而另一个方向一般会超出屏幕区域。
		 */
		NO_BORDER,
		/**
		 * 屏幕宽、高分别和设计分辨率宽、高计算缩放因子，取较(小)者作为宽、高的缩放因子。保证了设计区域全部显示到屏幕上，但可能会有黑边。
		 */
		SHOW_ALL,
		/**
		 * 保持传入的设计分辨率高度不变，根据屏幕分辨率修正设计分辨率的宽度。
		 */
		FIXED_HEIGHT,
		/**
		 * 保持传入的设计分辨率宽度不变，根据屏幕分辨率修正设计分辨率的高度。
		 */
		FIXED_WIDTH,
		/**
		 * 未知
		 */
		UNKNOWN
	}
	
	/**
	 * 屏幕分辨率大小
	 */
	protected Size screenSize;
	/**
	 * 设计分辨率大小
	 */
	protected Size designResolutionSize;
	/**
	 * 窗口的尺寸
	 */
	protected Rect viewPortRect;
	/**
	 * 窗口的标题
	 */
	protected String viewName;
	/**
	 * 宽度缩放比例
	 */
	protected float scaleX;
	/**
	 * 高度缩放比例
	 */
	protected float scaleY;
	/**
	 * 屏幕适配策略
	 */
	protected ResolutionPolicy resolutionPolicy;
	/**
	 * 构造函数，初始化成员
	 */
	public GLView() {
		screenSize = new Size();
		designResolutionSize = new Size();
		viewPortRect = new Rect();
		resolutionPolicy = ResolutionPolicy.UNKNOWN;
	}
	/**
	 * 销毁窗口
	 */
	public abstract void end();
	/**
	 * opengl渲染系统是否准备好
	 * @return boolean
	 */
	public abstract boolean isOpenGLReady();
	/**
	 * 交互前后台缓存
	 */
	public abstract void swapBuffers();
	/**
	 * 调用消息事件
	 */
	public abstract void pollEvents();
	/**
	 * 获取窗口大小，一般来说返回客户区大小
	 * @return Size
	 */
	public final Size getFrameSize() {
		return screenSize;
	}
	/**
	 * 设置窗口大小
	 * @param width 窗口的宽度
	 * @param height 窗口的高度
	 */
	public void setFrameSize(float width, float height) {
		designResolutionSize.setSize(width, height);
		screenSize.setSize(width, height);
	}
	/**
	 * 设置指针是否可见
	 * @param isVisible 是否显示鼠标指针
	 */
	public abstract void setCursorVisible(boolean isVisible);
	/**
	 * 获取opengl视口大小
	 * @return Size
	 */
	public final Size getVisibleSize() {
		if (resolutionPolicy == ResolutionPolicy.NO_BORDER) {
			return new Size(screenSize.width / scaleX, screenSize.height / scaleY);
		}
		return designResolutionSize;
	}
	/**
	 * 获取opengl视口原点
	 * @return Vec2
	 */
	public final Vec2 getVisibleOrigin() {
		if (resolutionPolicy == ResolutionPolicy.NO_BORDER) {
			return new Vec2((designResolutionSize.width - screenSize.width / scaleX) / 2, 
                    (designResolutionSize.height - screenSize.height / scaleY) / 2);
		}
		return new Vec2(0, 0);
	}
	/**
	 * 获取opengl视口矩形 
	 * @return Rect
	 */
	public final Rect getVisibleRect() {
		Rect rect = new Rect();
		rect.origin = getVisibleOrigin();
		rect.size = getVisibleSize();
		return rect;
	}
	/**
	 * 更新设计分辨率大小
	 */
	protected void updateDesignResolutionSize() {
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
			
			// 计算视口矩形
			float viewPortW = designResolutionSize.width * scaleX;
			float viewPortH = designResolutionSize.height * scaleY;
			
			viewPortRect.setRect((screenSize.width - viewPortW) / 2,
					(screenSize.height - viewPortH) / 2, viewPortW, viewPortH);
			
			// 重置Director的可见矩形
			Director director = Director.getInstance();
			director.setWinSizeInPoints(getDesignResolutionSize());
			director.setStatusLabelUpdated(true);
			director.setGLDefaultValues();
		}
	}
	/**
	 * 设置设计分辨率大小
	 * @param width 宽度
	 * @param height 高度
	 * @param resolutionPolicy 适配策略
	 */
	public void setDesignResolutionSize(float width, float height, ResolutionPolicy resolutionPolicy) {
		assert(resolutionPolicy != ResolutionPolicy.UNKNOWN) : "should set resolutionPolicy";
		if (width != 0.0f && height != 0.0f) {
			designResolutionSize.setSize(width, height);
			this.resolutionPolicy = resolutionPolicy;
			updateDesignResolutionSize();
		}
	}
	/**
	 * 获取设计分辨率大小
	 * @return Size
	 */
	public final Size getDesignResolutionSize() {
		return designResolutionSize;
	}
	/**
	 * 设置opengl视口矩形
	 * @param x 起始X坐标
	 * @param y 起始Y坐标
	 * @param w 宽度
	 * @param h 高度
	 */
	public void setViewPortInPoints(float x, float y, float w, float h) {
		GL.getGL().glViewport((int)(x * scaleX + viewPortRect.origin.x),
				(int)(y * scaleY + viewPortRect.origin.y), (int)(w * scaleX), (int)(h * scaleY));
	}
	/**
	 * 设置opengl裁剪矩形
	 * @param x 起始X坐标
	 * @param y 起始Y坐标
	 * @param w 宽度
	 * @param h 高度
	 */
	public void setScissorInPoints(float x, float y, float w, float h) {
		GL.getGL().glScissor((int)(x * scaleX + viewPortRect.origin.x),
				(int)(y * scaleY + viewPortRect.origin.y), (int)(w * scaleX), (int)(h * scaleY));
	}
	/**
	 * 是否启用 GL_SCISSOR_TEST
	 * @return boolean
	 */
	public boolean isScissorEnabled() {
		return GL.getGL().glIsEnabled(GL2.GL_SCISSOR_TEST);
	}
	/**
	 * 获取opengl当前裁剪矩形
	 * @return Rect
	 */
	public Rect getScissorRect() {
		float[] params = new float[4];
		GL.getGL().glGetFloatv(GL2.GL_SCISSOR_BOX, params, 0);
		float x = (params[0] - viewPortRect.origin.x) / scaleX;
		float y = (params[1] - viewPortRect.origin.y) / scaleY;
		float w = params[2] / scaleX;
		float h = params[3] / scaleY;
		return new Rect(x, y, w, h);
	}
	/**
	 * 设置窗口标题
	 * @param viewName 窗口标题
	 */
	public abstract void setViewName(String viewName);
	/**
	 * 获取窗口标题
	 * @return String
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * 获取opengl视口矩形
	 * @return Rect
	 */
	public Rect getViewPortRect() {
		return viewPortRect;
	}
	/**
	 * 获取窗口水平缩放比例
	 * @return float
	 */
	public float getScaleX() {
		return scaleX;
	}
	/**
	 * 获取窗口垂直缩放比例
	 * @return float
	 */
	public float getScaleY() {
		return scaleY;
	}
	/**
	 * 获取分辨率适配策略
	 * @return ResolutionPolicy
	 */
	public ResolutionPolicy getResolutionPolicy() {
		return resolutionPolicy;
	}
	/**
	 * 触摸结束和取消事件
	 * @param num 触摸点数量
	 * @param ids 触摸标识
	 * @param xs x坐标
	 * @param ys y坐标
	 */
	protected void handleTouchesOfEndOrCancel(int num, int[] ids, float xs[], float ys[]) {
		
	}
	/**
	 * 触摸开始事件
	 * @param num 触摸点数量
	 * @param ids 触摸标识
	 * @param xs x坐标
	 * @param ys y坐标
	 */
	public void handleTouchesBegin(int num, int[] ids, float xs[], float ys[]) {
		
	}
	/**
	 * 触摸移动事件
	 * @param num 触摸点数量
	 * @param ids 触摸标识
	 * @param xs x坐标
	 * @param ys y坐标
	 */
	public void handleTouchesMove(int num, int[] ids, float xs[], float ys[]) {
		
	}
	/**
	 * 触摸结束事件
	 * @param num 触摸点数量
	 * @param ids 触摸标识
	 * @param xs x坐标
	 * @param ys y坐标
	 */
	public void handleTouchesEnd(int num, int[] ids, float xs[], float ys[]) {
		
	}
	/**
	 * 触摸取消事件
	 * @param num 触摸点数量
	 * @param ids 触摸标识
	 * @param xs x坐标
	 * @param ys y坐标
	 */
	public void handleTouchesCancel(int num, int[] ids, float xs[], float ys[]) {
		
	}
}
