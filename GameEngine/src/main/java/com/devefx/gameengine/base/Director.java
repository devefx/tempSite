package com.devefx.gameengine.base;

import java.io.IOException;
import java.util.Stack;

import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.renderer.GLProgram;
import com.devefx.gameengine.renderer.GLProgramCache;
import com.devefx.gameengine.renderer.Renderer;
import com.devefx.gameengine.ui.Scene;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Director {

	protected GLView openGLView;
	protected Renderer renderer;
	
	protected Size winSizeInPoints;
	
	protected Stack<Scene> scenesStack;
	protected Scene runningScene;
	protected Scene nextScene;
	protected boolean sendCleanupToScene;
	
	protected InitializeGame initializeGame;
	
	private static Director director;
	
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
			
		}
		return director;
	}
	
	public Director() {
		scenesStack = new Stack<Scene>();
		nextScene = null;
		sendCleanupToScene = false;
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
		
		
	}
	
	public void run(InitializeGame init) {
		assert(init != null);
		startAnimation();
		initializeGame = init;
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
	
	protected void startAnimation() {
		final FPSAnimator animator = new FPSAnimator(canvas, 60, true);
		animator.start();
	}
	
	protected void drawScene() {
		
		renderer.clear();
		
		if (nextScene != null) {
			setNextScene();
		}
		
		if (runningScene != null) {
			runningScene.draw(renderer);
		}
		
		renderer.render();
	}
	
	protected void setNextScene() {
		runningScene = nextScene;
	}
	
	private GLCanvas canvas;
	public GLCanvas initGLCanvas(int width, int height) {
		GLCapabilities capabilities = new GLCapabilities(GLProfile.get(GLProfile.GL2));
		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(new GLEventListener() {
			@Override
			public void init(GLAutoDrawable drawable) {
				renderer.initGLView();
				try {
					initCreateProgram();
				} catch (Exception e) {
					e.printStackTrace();
				}
				initializeGame.init();
			}
			@Override
			public void display(GLAutoDrawable drawable) {
				drawScene();
			}
			@Override
			public void dispose(GLAutoDrawable drawable) {
				
			}
			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y, int width,
					int height) {
				
			}
		});
		canvas.setIgnoreRepaint(true);
		canvas.setSize(width, height);
		return canvas;
	}
	public GL2 getGL() {
		return GLContext.getCurrentGL().getGL2();
	}
	
	static void initCreateProgram() throws IOException {
		final GL2 gl = getInstance().getGL();
		
		GLProgramCache glProgramCache = GLProgramCache.getInstance();
		GLProgram glProgram = glProgramCache.getGLProgram(GLProgram.SHADER_NAME_POSITION_TEXTURE);
		glProgram.use();
		
		Mat4 mat4 = new Mat4();
        Mat4.createOrthographicOffCenter(0, 800, 0, 600, -1024, 1024, mat4);
		int location = gl.glGetUniformLocation(glProgram.getProgram(), "CC_MVPMatrix");
		gl.glUniformMatrix4fv(location, 1, false, mat4.m, 0);
	}
}
