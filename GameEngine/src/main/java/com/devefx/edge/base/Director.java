package com.devefx.edge.base;

import java.util.Stack;

import com.devefx.edge.base.MatrixControl.MatrixStackType;
import com.devefx.edge.base.types.Color4F;
import com.devefx.edge.base.types.Size;
import com.devefx.edge.base.types.Vec2;
import com.devefx.edge.math.Mat4;
import com.devefx.edge.platform.GLView;
import com.devefx.edge.renderer.GLStateCache.GL;
import com.devefx.edge.renderer.Renderer;
import com.devefx.edge.ui.Scene;
import com.devefx.edge.ui.TransitionScene;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Director {
	/**
	 * 启动器
	 */
	public interface Launch {
		void start();
	}
	/**
	 * 投影类型
	 */
	public enum Projection {
		/**
		 * 设置为2D投影
		 */
		_2D,
		/**
		 * 设置为3D投影(fovy=60, znear=0.5f and zfar=1500)
		 */
		_3D,
	}
	/** 自定义事件 */
	public static final String EVENT_AFTER_UPDATE = "director_after_update";
	public static final String EVENT_AFTER_VISIT = "director_after_visit";
	public static final String EVENT_AFTER_DRAW = "director_after_draw";
	/** Director的共享实例 */
	protected static Director director;
	/** 是否在下一个循环清理Director */
	protected boolean purgeDirectorInNextLoop;
	/** 是否在下一个循环重启Director */
	protected boolean restartDirectorInNextLoop;
	/** 调度器 */
	protected Scheduler scheduler;
	/** 更新、访问、绘画事件 */
	protected EventCustom eventAfterUpdate;
	protected EventCustom eventAfterVisit;
	protected EventCustom eventAfterDraw;
	/** 消息事件调度器 */
	protected EventDispatcher eventDispatcher;
	/** 完成最后一帧的时间 */
	protected float deltaTime;
	/** 最后更新时间 */
	protected long lastUpdate;
	/** 呈现OpenGL的窗口 */
	protected GLView openGLView;
	/** 是否暂停状态 */
	protected boolean paused;
	/** 从启动到现在过去的帧数 */
	protected long totalFrames;
	/** 正在运行的场景 */
	protected Scene runningScene;
	/** 接下来将要切换的场景 */
	protected Scene nextScene;
	/** 如果为true，被替换掉的场景将执行cleanup */
	protected boolean sendCleanupToScene;
	/** 计划处理的场景集合 */
	protected Stack<Scene> scenesStack;
	/** 投影类型 */
	protected Projection projection;
	/** 窗口的大小 */
	protected Size winSizeInPoints;
	/** 全局通知节点（不依赖Scene） */
	protected Node notificationNode;
	/** 渲染器 */
	protected Renderer renderer;
	/** 矩阵控制器 */
	protected MatrixControl matrixControl;
	/** 状态标签是否需要更新 */
	protected boolean statusLabelUpdated;
	/** 游戏启动器 */
	protected Launch launch;
	/** OpenGL画布 */
	protected GLCanvas glCanvas;
	/** 动画 */
	protected FPSAnimator animator;
	/** 动画间隔 */
	protected double animationInterval;
	protected double oldAnimationInterval;
	/** 动画是否无效 */
	protected boolean animationInvalid;
	/**
	 * 获取Director的共享实例
	 * @return Director
	 */
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
		}
		return director;
	}
	/**
	 * 初始化
	 * @return boolean
	 */
	public boolean init() {
		purgeDirectorInNextLoop = false;
		restartDirectorInNextLoop = false;
		scheduler = new Scheduler();
		eventDispatcher = new EventDispatcher();
		lastUpdate = System.nanoTime();
		scenesStack = new Stack<Scene>();
		winSizeInPoints = new Size();
		renderer = new Renderer();
		matrixControl = new MatrixControl();
		animationInterval = 1.0 / 60;
		animationInvalid = false;
		
		eventAfterUpdate = new EventCustom(EVENT_AFTER_UPDATE);
		eventAfterVisit = new EventCustom(EVENT_AFTER_VISIT);
		eventAfterDraw = new EventCustom(EVENT_AFTER_DRAW);
		
		matrixControl.initMatrixStack();
		return true;
	}
	/**
	 * 获取正在运行的场景
	 * @return Scene
	 */
	public Scene getRunningScene() {
		return runningScene;
	}
	/**
	 * 获取GLView
	 * @return GLView
	 */
	public GLView getOpenGLView() {
		return openGLView;
	}
	/**
	 * 设置GLView
	 * @param openGLView
	 */
	public void setOpenGLView(GLView glView) {
		assert(glView != null) : "opengl view should not be null";
		
		if (openGLView != glView) {
			
			openGLView = glView;
			
			statusLabelUpdated = true;
			
			if (openGLView != null) {
				
				winSizeInPoints = openGLView.getDesignResolutionSize();
				
				setGLDefaultValues();
			}
			
			renderer.initGLView();
			
			if (eventDispatcher != null) {
				eventDispatcher.setEnabled(true);
			}
		}
	}
	/**
	 * 获取投影类型
	 * @return Projection
	 */
	public Projection getProjection() {
		return projection;
	}
	/**
	 * 设置投影类型
	 * @param projection
	 */
	public void setProjection(Projection projection) {
		setViewport();
		switch (projection) {
		case _2D:
			matrixControl.loadIdentityMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
			Mat4 orthoMatrix = new Mat4();
			Mat4.createOrthographicOffCenter(0, winSizeInPoints.width, 0, winSizeInPoints.height,
					-1024, 1024, orthoMatrix);
			matrixControl.multiplyMatrix(MatrixStackType.MATRIX_STACK_PROJECTION, orthoMatrix);
			matrixControl.loadIdentityMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
			break;
		case _3D:
			
			break;
		}
		this.projection = projection;
	}
	/**
	 * 设置视口
	 */
	public void setViewport() {
		if (openGLView != null) {
			openGLView.setViewPortInPoints(0, 0, winSizeInPoints.width, winSizeInPoints.height);
		}
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isSendCleanupToScene() {
		return sendCleanupToScene;
	}
	/**
	 * 
	 * @param sendCleanupToScene
	 */
	public void setSendCleanupToScene(boolean sendCleanupToScene) {
		this.sendCleanupToScene = sendCleanupToScene;
	}
	/**
	 * 
	 * @return Size
	 */
	public Size getWinSizeInPoints() {
		return winSizeInPoints;
	}
	/**
	 * 
	 * @param winSizeInPoints
	 */
	public void setWinSizeInPoints(Size winSizeInPoints) {
		this.winSizeInPoints = winSizeInPoints;
	}
	/**
	 * 获取opengl视口大小
	 * @return Size
	 */
	public Size getVisibleSize() {
		if (openGLView != null) {
			return openGLView.getVisibleSize();
		}
		return new Size(0, 0);
	}
	/**
	 * 获取opengl视口原点
	 * @return Vec2
	 */
	public Vec2 getVisibleOrigin() {
		if (openGLView != null) {
			return openGLView.getVisibleOrigin();
		}
		return new Vec2(0, 0);
	}
	/**
	 * 结束
	 */
	public void end() {
		purgeDirectorInNextLoop = true;
	}
	/**
	 * 重置
	 */
	protected void reset() {
		if (runningScene != null) {
			runningScene.onExit();
			runningScene.cleanup();
		}
		
		runningScene = null;
		nextScene = null;
		
		scenesStack.clear();
		
		stopAnimation();
	}
	/**
	 * 清理Director
	 */
	protected void purgeDirector() {
		reset();
		
		if (openGLView != null) {
			openGLView.end();
			openGLView = null;
		}
	}
	/**
	 * 重启Director
	 */
	protected void restartDirector() {
		reset();
		
		// FIXME
	}
	/**
	 * 暂停
	 */
	public void pause() {
		if (!paused) {
			oldAnimationInterval = animationInterval;
			setAnimationInterval(1 / 4.0);
			paused = true;
		}
	}
	/**
	 * 继续
	 */
	public void resume() {
		if (paused) {
			setAnimationInterval(oldAnimationInterval);
			paused = false;
			deltaTime = 0;
		}
	}
	/**
	 * 重启
	 */
	public void restart() {
		restartDirectorInNextLoop = true;
	}
	/**
	 * 设置通知节点
	 * @param notificationNode
	 */
	public void setNotificationNode(Node notificationNode) {
		this.notificationNode = notificationNode;
	}
	/**
	 * 开始动画
	 */
	public void startAnimation() {
		if (animator == null) {
			animator = new FPSAnimator(getGLCanvas(), 
					(int) (1.0 / animationInterval), true);
		}
		animator.start();
		animationInvalid = false;
	}
	/**
	 * 停止动画
	 */
	public void stopAnimation() {
		if (animator != null) {
			animator.stop();
			animator = null;
		}
		animationInvalid = true;
	}
	/**
	 * 设置动画间隔
	 * @param interval
	 */
	public void setAnimationInterval(double interval) {
		animationInterval = interval;
		if (!animationInvalid) {
			stopAnimation();
			startAnimation();
		}
	}
	/**
	 * 从这个场景运行
	 * @param scene
	 */
	public void runWithScene(Scene scene) {
		assert(scene != null) : "This command can only be used to start the Director. There is already a scene present.";
		assert(runningScene != null) : "runningScene should be null";
		
		pushScene(scene);
		startAnimation();
	}
	/**
	 * 往场景列表添加一个场景
	 * @param scene void
	 */
	public void pushScene(Scene scene) {
		assert(scene != null) : "the scene should not null";
		sendCleanupToScene = false;
		scenesStack.push(scene);
		nextScene = scene;
	}
	/**
	 * 从场景列表中弹出一个场景
	 * @param scene void
	 */
	public void popScene(Scene scene) {
		assert(runningScene != null) : "running scene should not null";
		scenesStack.pop();
		int size = scenesStack.size();
		if (size == 0) {
			end();
		} else {
			sendCleanupToScene = true;
			nextScene = scenesStack.get(size - 1);
		}
	}
	/**
	 * 替换场景
	 * @param scene void
	 */
	public void replaceScene(Scene scene) {
		assert(scene != null) : "the scene should not be null";
		
		if (runningScene == null) {
			runWithScene(scene);
			return;
		}
		
		if (scene != nextScene) {
			
			if (scene != null) {
				if (nextScene.isRunning()) {
					nextScene.onExit();
				}
				nextScene.cleanup();
				nextScene = null;
			}
			int size = scenesStack.size();
			
			sendCleanupToScene = true;
			scenesStack.set(size - 1, scene);
			
			nextScene = scene;
		}
	}
	/**
	 * 切换到下一个场景
	 */
	protected void setNextScene() {
		
		boolean runningIsTransition = (runningScene instanceof TransitionScene);
		boolean newIsTransition = (nextScene instanceof TransitionScene);
		
		if (newIsTransition) {
			if (runningScene != null) {
				runningScene.onEnterTransitionDidFinish();
				runningScene.onExit();
				
				if (sendCleanupToScene) {
					runningScene.cleanup();
				}
			}
		}
		
		runningScene = nextScene;
		nextScene = null;
		
		if (!runningIsTransition && runningScene != null) {
			runningScene.onEnter();
			runningScene.onEnterTransitionDidFinish();
		}
	}
	/**
	 * 绘画当前场景
	 */
	public void drawScene() {
		
		calculateDeltaTime();
		
		if (openGLView != null) {
			openGLView.pollEvents();
		}
		
		if (!paused) {
			scheduler.update(deltaTime);
			eventDispatcher.dispatchEvent(eventAfterUpdate);
		}
		
		renderer.clear();
		
		if (nextScene != null) {
			setNextScene();
		}
		
		matrixControl.pushMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
		
		if (runningScene != null) {
			
			renderer.clearDrawStats();
			
			runningScene.render(renderer);
			
			eventDispatcher.dispatchEvent(eventAfterVisit);
		}
		
		if (notificationNode != null) {
			notificationNode.visit(renderer, new Mat4(), 0);
		}
		
		renderer.render();
		
		eventDispatcher.dispatchEvent(eventAfterDraw);
		
		matrixControl.popMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
		
		totalFrames++;
		
		if (openGLView != null) {
			openGLView.swapBuffers();
		}
	}
	/**
	 * 计算当前帧花掉的时间
	 */
	protected void calculateDeltaTime() {
		long now = System.nanoTime();
		
		deltaTime = (now - lastUpdate) / 1000000000.0f;
		
		if (deltaTime > 0.2f) {
			deltaTime = 1 / 60.0f;
		}
		
		lastUpdate = now;
	}
	/**
	 * 获取完成最后一帧花掉的时间
	 * @return float
	 */
	public float getDeltaTime() {
		return deltaTime;
	}
	/**
	 * 设置状态标签是否需要更新
	 * @param isStatusLabelUpdated 
	 */
	public void setStatusLabelUpdated(boolean isStatusLabelUpdated) {
		this.statusLabelUpdated = isStatusLabelUpdated;
	}
	/**
	 * 设置OpenGL默认参数
	 */
	public void setGLDefaultValues() {
		setAlphaBlending(true);
		setDepthTest(false);
		setProjection(Projection._2D);
	}
	/**
	 * 启用或停用OpenGL混合模式
	 * @param on
	 */
	public void setAlphaBlending(boolean on) {
		if (on) {
			GL.blendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL.blendFunc(GL2.GL_ONE, GL2.GL_ZERO);
		}
	}
	/**
	 * 设置清屏颜色
	 * @param clearColor
	 */
	public void setClearColor(Color4F clearColor) {
		renderer.setClearColor(clearColor);
	}
	/**
	 * 启用或停用OpenGL深度测试
	 * @param on
	 */
	public void setDepthTest(boolean on) {
		renderer.setDepthTest(on);
	}
	/**
	 * 循环线程
	 */
	public void mainLoop() {
		if (purgeDirectorInNextLoop) {
			purgeDirectorInNextLoop = false;
			purgeDirector();
		} else if (restartDirectorInNextLoop) {
			restartDirectorInNextLoop = false;
			restartDirector();
		} else if (!animationInvalid) {
			drawScene();
		}
	}
	/**
	 * 获取事件调度器
	 * @return EventDispatcher
	 */
	public EventDispatcher getEventDispatcher() {
		return eventDispatcher;
	}
	/**
	 * 设置事件调度器
	 * @param eventDispatcher
	 */
	public void setEventDispatcher(EventDispatcher eventDispatcher) {
		this.eventDispatcher = eventDispatcher;
	}
	/**
	 * 获取渲染器
	 * @return Renderer
	 */
	public Renderer getRenderer() {
		return renderer;
	}
	/**
	 * 获取矩阵控制器
	 * @return MatrixControl
	 */
	public MatrixControl getMatrixControl() {
		return matrixControl;
	}
	/**
	 * 设置游戏的启动器
	 * @param launch
	 */
	public void setLaunch(Launch launch) {
		this.launch = launch;
	}
	/**
	 * 获取OpenGL画布
	 * @return GLCanvas
	 */
	public GLCanvas getGLCanvas() {
		if (glCanvas == null) {
			// getting the capabilities object of GL2 profile
			GLProfile profile = GLProfile.get(GLProfile.GL2);
			GLCapabilities capabilities = new GLCapabilities(profile);
			// Create canvas
			glCanvas = new GLCanvas(capabilities);
			glCanvas.addGLEventListener(new GLEventListener() {
				@Override
				public void init(GLAutoDrawable drawable) {
					assert(launch != null) : "launch should not be null";
					launch.start();
				}
				@Override
				public void display(GLAutoDrawable drawable) {
					mainLoop();
				}
				@Override
				public void dispose(GLAutoDrawable drawable) {
				}
				@Override
				public void reshape(GLAutoDrawable drawable, int x, int y, int width,
						int height) {
				}
			});
		}
		return glCanvas;
	}
}
