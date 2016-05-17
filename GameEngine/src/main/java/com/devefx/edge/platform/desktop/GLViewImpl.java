package com.devefx.edge.platform.desktop;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.devefx.edge.base.Director;
import com.devefx.edge.base.types.Rect;
import com.devefx.edge.platform.GLView;
import com.jogamp.opengl.awt.GLCanvas;

public class GLViewImpl extends GLView {
	
	protected Frame frame;
	protected boolean openGLReady;
	protected Director director;
	
	/**
	 * 创建一个尺寸为960x640的窗口
	 * @param viewName
	 * @return GLViewImpl
	 */
	public static GLViewImpl create(String viewName) {
		return create(viewName, new Rect(0, 0, 960, 640));
	}
	/**
	 * 根据矩形创建一个窗口
	 * @param viewName 窗口的名称
	 * @param rect 窗口矩形
	 * @return GLViewImpl
	 */
	public static GLViewImpl create(String viewName, Rect rect) {
		GLViewImpl glView = new GLViewImpl();
		if (glView.initWithRect(viewName, rect)) {
			return glView;
		}
		return null;
	}
	/**
	 * 构造函数，初始化属性
	 */
	public GLViewImpl() {
		frame = new Frame();
		director = Director.getInstance();
	}
	/**
	 * 根据矩形初始化窗口
	 * @param viewName 窗口的名称
	 * @param rect 窗口矩形
	 * @return boolean
	 */
	public boolean initWithRect(String viewName, Rect rect) {
		// setting the view name and size
		setViewName(viewName);
		setFrameSize(rect.size.width, rect.size.height);
		// getting canvas of Director
		GLCanvas glcanvas = director.getGLCanvas();
		glcanvas.setSize((int) rect.size.width, (int) rect.size.height);
		// The Windows
		frame.setLayout(new BorderLayout());
		frame.setSize(glcanvas.getSize());
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(glcanvas, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				director.end();
			}
		});
		frame.pack();
		frame.setVisible(true);
		openGLReady = true;
		return true;
	}
	
	@Override
	public void end() {
		System.exit(0);
	}

	@Override
	public boolean isOpenGLReady() {
		return openGLReady;
	}

	@Override
	public void swapBuffers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pollEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCursorVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setViewName(String viewName) {
		this.viewName = viewName;
		frame.setTitle(viewName);
	}
}
