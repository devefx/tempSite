package com.devefx.gameengine.base;

import java.util.ArrayList;
import java.util.List;

import com.devefx.gameengine.base.Director.MatrixStackType;
import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.base.types.Vec2;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLProgram;
import com.devefx.gameengine.renderer.GLProgramState;
import com.devefx.gameengine.renderer.Renderer;
import com.devefx.gameengine.ui.Scene;

public class Node {
	
	public static final int INVALID_TAG = -1;
	
	protected float rotationX;	// 在 x 轴上的旋转值
	protected float rotationY;	// 在 y 轴上的旋转值
	
	protected float scaleX;		// 在 x 轴上缩放比例
	protected float scaleY;		// 在 y 轴上缩放比例
	protected float scaleZ;		// 在 z 轴上缩放比例
    
	protected Vec2 position;	// 节点的位置
	protected float positionZ;	// OpenGL的Z位置
	
	protected Vec2 anchorPoint;	// 锚点位置
	
	protected Size contentSize;			// 内容尺寸
	protected boolean contentSizeDirty;	// 是否需要更新contentSize
	
	protected Mat4 modelViewTransform;	// 节点的ModelView transform
	protected Mat4 transform;			// 变化矩阵
	protected boolean transformDirty;	// 是否需要更新变化矩阵
	
	
	protected int localZOrder;			// 本地排序，用于兄节点的排序
	protected float globalZOrder;		// 全局的Z序
	
	protected List<Node> children;	// 子节点集合
	protected Node parent;			// 父节点
	
	protected int tag;				// 标签
	protected String name;			// 名称
	
	protected Director director;	// 导演
	
	protected GLProgramState glProgramState; // OpenGL Program State
	
	protected boolean runing;	// 是否正在运行
	
	protected boolean visible;	// 是否显示
	
	protected int cameraMask;
	
	
	
	
	public Node() {
		rotationX = 0.0f;
		rotationY = 0.0f;
		scaleX = 1.0f;
		scaleY = 1.0f;
		scaleZ = 1.0f;
		position = Vec2.ZERO.clone();
		positionZ = 0.0f;
		anchorPoint = Vec2.ZERO.clone();
		contentSize = Size.ZERO.clone();
		contentSizeDirty = true;
		modelViewTransform = new Mat4();
		transform = new Mat4();
		transformDirty = true;
		localZOrder = 0;
		globalZOrder = 0.0f;
		children = new ArrayList<Node>();
		parent = null;
		tag = INVALID_TAG;
		name = null;
		glProgramState = null;
		runing = false;
		visible = true;
		director = Director.getInstance();
		cameraMask = 1;
	}
	
	public boolean init() {
		return true;
	}
	
	public void setLocalZOrder(int localZOrder) {
		this.localZOrder = localZOrder;
		
		//TODO
	}
	
	public void setGlobalZOrder(float globalZOrder) {
		this.globalZOrder = globalZOrder;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public void setScaleX(float scaleX) {
		setScale(scaleX, scaleY, scaleZ);
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setScaleY(float scaleY) {
		setScale(scaleX, scaleY, scaleZ);
	}
	
	public float getScaleZ() {
		return scaleZ;
	}
	
	public void setScaleZ(float scaleZ) {
		setScale(scaleX, scaleY, scaleZ);
	}
	
	public void setScale(float scaleX, float scaleY, float scaleZ) {
		if (this.scaleX != scaleX || this.scaleY != scaleY || this.scaleZ != scaleZ) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			this.scaleZ = scaleZ;
			transformDirty = true;
		}
	}
	
	public Vec2 getPosition() {
		return position;
	}
	
	public void setPosition(Vec2 position) {
		assert(position != null);
		setPosition(position.x, position.y);
	}
	
	public void setPosition(float x, float y) {
		if (position.x != x || position.y != y) {
			position.x = x;
			position.y = y;
			transformDirty = true;
		}
	}
	
	public float getPositionX() {
		return position.x;
	}
	
	public void setPositionX(float x) {
		setPosition(x, position.y);
	}
	
	public float getPositionY() {
		return position.y;
	}
	
	public void setPositionY(float y) {
		setPosition(position.x, y);
	}
	
	public float getPositionZ() {
		return positionZ;
	}
	
	public void setPositionZ(float positionZ) {
		if (this.positionZ != positionZ) {
			this.positionZ = positionZ;
			transformDirty = true;
		}
	}
	
	public int getChildrenCount() {
		return children.size();
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			if (visible) {
				transformDirty = true;
			}
		}
	}
	
	public boolean isRuning() {
		return runing;
	}
	
	public Vec2 getAnchorPoint() {
		return anchorPoint;
	}
	
	public void setAnchorPoint(Vec2 anchorPoint) {
		assert(anchorPoint != null);
		if (anchorPoint.equals(this.anchorPoint)) {
			this.anchorPoint = anchorPoint;
			transformDirty = true;
		}
	}
	
	public Size getContentSize() {
		return contentSize;
	}
	
	public void setContentSize(Size contentSize) {
		if (!contentSize.equals(this.contentSize)) {
			this.contentSize = contentSize;
			transformDirty = contentSizeDirty = true;
		}
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
		transformDirty = true;
	}
	
	public int getTag() {
		return tag;
	}
	
	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public GLProgramState getGLProgramState() {
		return glProgramState;
	}
	
	public void setGLProgramState(GLProgramState glProgramState) {
		this.glProgramState = glProgramState;
	}
	
	public GLProgram getGLProgram() {
		return glProgramState != null ? glProgramState.getGLProgram() : null;
	}
	
	public Scene getScene() {
		if (parent != null) {
			Node sceneNode = parent;
			while (sceneNode.parent != null) {
				sceneNode = sceneNode.parent;
			}
			return (Scene) sceneNode;
		}
		return null;
	}
	
	public Node getChildByTag(int tag) {
		assert(tag != INVALID_TAG);
		for (Node child : children) {
			if (child.tag == tag)
				return child;
		}
		return null;
	}
	
	public Node getChildByName(String name) {
		assert(name != null && !name.isEmpty());
		for (Node child : children) {
			if (name.equals(child.name))
				return child;
		}
		return null;
	}
	
	public void addChild(Node child) {
		assert(child != null);
		addChild(child, child.localZOrder, child.tag);
	}
	
	public void addChild(Node child, int zOrder) {
		assert(child != null);
		addChild(child, zOrder, child.name);
	}
	
	public void addChild(Node child, int localZOrder, int tag) {
		addChildHelper(child, localZOrder, tag, null);
	}

	public void addChild(Node child, int localZOrder, String name) {
		addChildHelper(child, localZOrder, INVALID_TAG, name);
	}

	protected void addChildHelper(Node child, int localZOrder, int tag, String name) {
		assert(child != null);
		assert(child.parent == null);
		child.setLocalZOrder(localZOrder);
		child.setTag(tag);
		child.setName(name);
		child.setParent(this);
		children.add(child);
	}

	public void removeChild(Node child, boolean cleanup) {
		if (children.remove(child)) {
			if (cleanup) {
				child.cleanup();
			}
			child.setParent(null);
		}
	}
	
	public void removeChildByTag(int tag, boolean cleanup) {
		assert(tag != INVALID_TAG);
		Node child = getChildByTag(tag);
		if (child != null) {
			removeChild(child, cleanup);
		}
	}

	public void removeChildByName(String name, boolean cleanup) {
		assert(tag != INVALID_TAG);
		Node child = getChildByName(name);
		if (child != null) {
			removeChild(child, cleanup);
		}
	}

	public void removeAllChildren(boolean cleanup) {
		for (Node child : children) {
			if (cleanup) {
				child.cleanup();
			}
			child.setParent(null);
		}
		children.clear();
	}
	
	public void draw() {
		draw(director.getRenderer(), modelViewTransform);
	}
	
	public void draw(Renderer renderer, Mat4 transform) {
		
	}
	
	public void cleanup() {
		
	}
	
	public void visit() {
		visit(director.getRenderer(), director.getMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW));
	}
	
	public void visit(Renderer renderer, final Mat4 parentTransform) {
		if (visible) {
			modelViewTransform = transform(parentTransform);
			
			director.pushMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
			director.loadMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW, modelViewTransform);
			
			boolean visibleByCamera = true;
			
			if (!children.isEmpty()) {
				int i = 0, n = children.size();
				for (; i < n; i++) {
					Node child = children.get(i);
					if (child != null && child.localZOrder < 0) {
						child.visit(renderer, modelViewTransform);
					} else break;
				}
				
				if (visibleByCamera) {
					draw(renderer, modelViewTransform);
				}
				
				for (; i < n; i++) {
					children.get(i).visit(renderer, modelViewTransform);
				}
			} else if (visibleByCamera) {
				draw(renderer, modelViewTransform);
			}
			
			director.popMatrix(MatrixStackType.MATRIX_STACK_MODELVIEW);
		}
	}
	
	public Mat4 transform(final Mat4 parentTransform) {
		return Mat4.multiplyMatrix(parentTransform, getNodeToParentTransform());
	}
	
	public void onEnter() {
		for (Node child : children) {
			child.onEnter();
		}
		runing = true;
	}
	
	public Mat4 getNodeToParentTransform() {
		if (transformDirty) {
			float x = position.x;
			float y = position.y;
			float z = positionZ;
			
			Mat4 translation = new Mat4();
			Mat4.createTranslation(x, y, z, translation);
			this.transform = Mat4.multiplyMatrix(new Mat4(), translation);
			this.transform.translate(-anchorPoint.x, -anchorPoint.y, 0);
			
			if (scaleX != 1.0f) {
				transform.m[0] *= scaleX;
				transform.m[1] *= scaleX;
				transform.m[2] *= scaleX;
			}
			if (scaleY != 1.0f) {
				transform.m[4] *= scaleY;
				transform.m[5] *= scaleY;
				transform.m[6] *= scaleY;
			}
			if (scaleZ != 1.0f) {
				transform.m[8] *= scaleZ;
				transform.m[9] *= scaleZ;
				transform.m[10] *= scaleZ;
			}
			
			transformDirty = false;
		}
		return transform;
	}
	
	public void setCameraMask(int mask, boolean applyChildren) {
		cameraMask = mask;
		if (applyChildren) {
			for (Node child : children) {
				child.setCameraMask(mask, applyChildren);
			}
		}
	}
	
}
