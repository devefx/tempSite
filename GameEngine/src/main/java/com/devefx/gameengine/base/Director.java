package com.devefx.gameengine.base;

import java.util.Stack;

import com.devefx.gameengine.base.types.Color4F;
import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.renderer.GLStateCache.GL;
import com.devefx.gameengine.renderer.Renderer;
import com.devefx.gameengine.ui.Scene;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

public class Director {

	protected Stack<Mat4> modelViewMatrixStack;
	protected Stack<Mat4> projectionMatrixStack;
	protected Stack<Mat4> textureMatrixStack;
	
	protected Projection projection;
	
	protected GLView openGLView;
	protected Renderer renderer;
	
	protected Size winSizeInPoints;
	
	protected Stack<Scene> scenesStack;
	protected Scene runningScene;
	protected Scene nextScene;
	protected boolean sendCleanupToScene;
	
	protected EventListener listener;
	protected boolean dirty;
	
	private static Director director;
	
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
			
		}
		return director;
	}
	
	public void setListener(EventListener listener) {
		this.listener = listener;
	}
	
	public Director() {
		modelViewMatrixStack = new Stack<Mat4>();
		projectionMatrixStack = new Stack<Mat4>();
		textureMatrixStack = new Stack<Mat4>();
		
		scenesStack = new Stack<Scene>();
		nextScene = null;
		sendCleanupToScene = false;
	}
	
	public boolean init() {
		renderer = new Renderer();
		
		initMatrixStack();
		
		return true;
	}
	
	public void setOpenGLView(GLView openGLView) {
		if (this.openGLView != openGLView) {
			
			this.openGLView = openGLView;
			
			if (openGLView != null) {
				winSizeInPoints = openGLView.getDesignResolutionSize();
				
				//setGLDefaultValues();
			}
			setDirty(true);
			//renderer.initGLView();
		}
	}
	public Renderer getRenderer() {
		return renderer;
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
		setAlphaBlending(true);
		setDepthTest(false);
		setProjection(Projection._2D);
	}
	
	public Size getWinSize() {
		return winSizeInPoints;
	}
	
	public Size getVisibleSize() {
		if (openGLView != null) {
			return openGLView.getVisibleSize();
		}
		return Size.ZERO;
	}
	
	public void runWithScene(Scene scene) {
		assert(scene != null);
		assert(runningScene == null);
		pushScene(scene);
	}
	
	public void pushScene(Scene scene) {
		
		sendCleanupToScene = false;
		
		scenesStack.push(scene);
		nextScene = scene;
	}
	
	public void popScene() {
		scenesStack.pop();
		int i = scenesStack.size();
		if (i == 0) {
			
		} else {
			sendCleanupToScene = true;
			nextScene = scenesStack.get(i - 1);
		}
	}
	
	protected void drawScene() {
		
		renderer.clear();
		
		if (nextScene != null) {
			setNextScene();
		}
		
		pushMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
		
		if (runningScene != null) {
			
			renderer.clearDrawStats();
			
			runningScene.render(renderer);
		}
		
		renderer.render();
		
		popMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
	}
	
	protected void setNextScene() {
		runningScene = nextScene;
		nextScene = null;
		
		runningScene.onEnter();
	}
	
	
	
	//
	// FIXME TODO
	// Matrix code MUST NOT be part of the Director
	// MUST BE moved outide.
	// Why the Director must have this code ?
	//
	public void initMatrixStack() {
		modelViewMatrixStack.clear();
		projectionMatrixStack.clear();
		textureMatrixStack.clear();
		
		modelViewMatrixStack.push(new Mat4());
		projectionMatrixStack.push(new Mat4());
		textureMatrixStack.push(new Mat4());
	}
	
	public void popMatrix(MatrixStackType type) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			modelViewMatrixStack.pop();
			return;
		case MATRIX_STACK_PROJECTION:
			projectionMatrixStack.pop();
			return;
		case MATRIX_STACK_TEXTURE:
			textureMatrixStack.pop();
			return;
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public void loadIdentityMatrix(MatrixStackType type) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			modelViewMatrixStack.get(0).loadIdentity();
			return;
		case MATRIX_STACK_PROJECTION:
			projectionMatrixStack.get(0).loadIdentity();
			return;
		case MATRIX_STACK_TEXTURE:
			textureMatrixStack.get(0).loadIdentity();
			return;
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public void loadMatrix(MatrixStackType type, Mat4 matrix) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			modelViewMatrixStack.set(0, matrix);
			return;
		case MATRIX_STACK_PROJECTION:
			projectionMatrixStack.set(0, matrix);
			return;
		case MATRIX_STACK_TEXTURE:
			textureMatrixStack.set(0, matrix);
			return;
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public void multiplyMatrix(MatrixStackType type, Mat4 matrix) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			modelViewMatrixStack.get(0).multiply(matrix);
			return;
		case MATRIX_STACK_PROJECTION:
			projectionMatrixStack.get(0).multiply(matrix);
			return;
		case MATRIX_STACK_TEXTURE:
			textureMatrixStack.get(0).multiply(matrix);
			return;
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public void pushMatrix(MatrixStackType type) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			modelViewMatrixStack.push(modelViewMatrixStack.firstElement().clone());
			return;
		case MATRIX_STACK_PROJECTION:
			projectionMatrixStack.push(projectionMatrixStack.firstElement().clone());
			return;
		case MATRIX_STACK_TEXTURE:
			textureMatrixStack.push(textureMatrixStack.firstElement().clone());
			return;
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public Mat4 getMatrix(MatrixStackType type) {
		switch (type) {
		case MATRIX_STACK_MODELVIEW:
			return modelViewMatrixStack.firstElement();
		case MATRIX_STACK_PROJECTION:
			return projectionMatrixStack.firstElement();
		case MATRIX_STACK_TEXTURE:
			return textureMatrixStack.firstElement();
		}
		throw new IllegalArgumentException("unknow matrix stack type");
	}
	
	public Projection getProjection() {
		return projection;
	}
	
	public void setProjection(Projection projection) {
		switch (projection) {
		case _2D:
			loadIdentityMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
			Mat4 orthoMatrix = new Mat4();
			Mat4.createOrthographicOffCenter(0, winSizeInPoints.width, 0, winSizeInPoints.height,
					-1024, 1024, orthoMatrix);
			multiplyMatrix(MatrixStackType.MATRIX_STACK_PROJECTION, orthoMatrix);
			loadIdentityMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
			break;
		}
		this.projection = projection;
	}
	
	public void setAlphaBlending(boolean on) {
		if (on) {
			GL.blendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL.blendFunc(GL2.GL_ONE, GL2.GL_ZERO);
		}
	}
	
	public void setDepthTest(boolean on) {
		renderer.setDepthTest(on);
	}
	
	public void setClearColor(Color4F clearColor) {
		renderer.setClearColor(clearColor);
	}

	
	
	
	
	public enum Projection {
		_2D
	}
	
	public enum MatrixStackType {
		MATRIX_STACK_MODELVIEW,
		MATRIX_STACK_PROJECTION,
		MATRIX_STACK_TEXTURE
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public void updateDirty() {
		if (dirty) {
			dirty = false;
			if (openGLView != null) {
				setGLDefaultValues();
			}
			renderer.initGLView();
		}
	}
	
	public GLEventListener glEventListener = new GLEventListener() {
		@Override
		public void init(GLAutoDrawable drawable) {
			GL.setGL(drawable.getGL().getGL2());
			updateDirty();
			if (listener != null) {
				listener.init();
			}
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			updateDirty();
			
			drawScene();
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
		}
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
		}
	};
	
}
