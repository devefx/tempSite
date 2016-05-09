package com.devefx.gameengine.base;

import java.util.ArrayList;
import java.util.List;

import com.devefx.gameengine.base.types.Vec2;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLProgramState;
import com.devefx.gameengine.renderer.Renderer;

public abstract class Node {
	
	protected List<Node> children;
	protected Node parent;
	protected Vec2 position;
	protected float globalOrder;
	protected GLProgramState glProgramState;
	
	public Node() {
		children = new ArrayList<Node>();
		parent = null;
		position = new Vec2(0.0f, 0.0f);
		globalOrder = 0.0f;
	}
	public void addChild(Node node) {
		children.add(node);
		node.setParent(this);
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}
	public GLProgramState getGLProgramState() {
		return glProgramState;
	}
	public void setGLProgramState(GLProgramState glProgramState) {
		this.glProgramState = glProgramState;
	}
	
	public abstract void draw(Renderer renderer, Mat4 transform);
}
