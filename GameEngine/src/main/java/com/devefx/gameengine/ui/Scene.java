package com.devefx.gameengine.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.Director.MatrixStackType;
import com.devefx.gameengine.base.Node;
import com.devefx.gameengine.base.types.Vec2;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.Camera;
import com.devefx.gameengine.renderer.Renderer;

public class Scene extends Node {

	protected List<Camera> cameras;
	protected Camera defaultCamera;
	protected boolean cameraOrderDirty;
	
	public static Scene create() {
		Scene scene = new Scene();
		if (scene.init()) {
			return scene;
		}
		return null;
	}
	
	public Scene() {
		cameras = new ArrayList<Camera>();
		cameraOrderDirty = true;
		defaultCamera = Camera.create();
		addChild(defaultCamera);
		setAnchorPoint(new Vec2(0.5f, 0.5f));
	}
	
	public List<Camera> getCameras() {
		return cameras;
	}
	
	public void setCameraOrderDirty() {
		cameraOrderDirty = true;
	}
	
	@Override
	public void draw(Renderer renderer, Mat4 transform) {
		if (!children.isEmpty()) {
			
			for (Node node: children) {
				node.draw(renderer, transform);
			}
		}
	}
	
	public void render(Renderer renderer) {
		Director director = Director.getInstance();

		if (cameraOrderDirty) {
			Collections.sort(cameras, new Comparator<Camera>() {
				@Override
				public int compare(Camera a, Camera b) {
					return a.getDepth() - b.getDepth();
				}
			});
			cameraOrderDirty = false;
		}
		
		for (Camera camera : cameras) {
			
			if (!camera.isVisible()) {
				continue;
			}
			
			Camera.visitingCamera = camera;
			director.pushMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
			director.loadMatrix(MatrixStackType.MATRIX_STACK_PROJECTION, camera.getProjectionMatrix());
			
			//draw(renderer, getNodeToParentTransform());
			visit(renderer, getNodeToParentTransform());
			renderer.render();
			
			director.popMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
		}
		Camera.visitingCamera = null;
	}
}
