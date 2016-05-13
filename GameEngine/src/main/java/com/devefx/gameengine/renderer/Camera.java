package com.devefx.gameengine.renderer;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.Node;
import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.ui.Scene;

public class Camera extends Node {
	
	public static Camera visitingCamera;
	
	protected Scene scene;
	protected Mat4 projection;
	protected int depth;
	protected Type type;
	protected CameraFlag cameraFlag;
	
	public static Camera createPerspective(float fieldOfView, float aspectRatio, float nearPlane, float farPlane) {
		Camera camera = new Camera();
		if (camera.initPerspective(fieldOfView, aspectRatio, nearPlane, farPlane)) {
			return camera;
		}
		return null;
	}
	
	public static Camera createOrthographic(float zoomX, float zoomY, float nearPlane, float farPlane) {
		Camera camera = new Camera();
		if (camera.initOrthographic(zoomX, zoomY, nearPlane, farPlane)) {
			return camera;
		}
		return null;
	}
	
	public static Camera create() {
		Camera camera = new Camera();
		if (camera.initDefault()) {
			camera.setDepth(0);
			return camera;
		}
		return null;
	}
	
	public static Camera getVisitingCamera() {
		return visitingCamera;
	}
	
	public static Camera getDefaultCamera() {
		return null;
	}
	
	private Camera() {
		projection = new Mat4();
		cameraFlag = CameraFlag.DEFAULT;
		visible = true;
	}
	
	public Type getType() {
		return type;
	}
	
	public CameraFlag getCameraFlag() {
		return cameraFlag;
	}
	
	public void setCameraFlag(CameraFlag cameraFlag) {
		this.cameraFlag = cameraFlag;
	}
	
	public Mat4 getProjectionMatrix() {
		return projection;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		if (this.depth != depth) {
			this.depth = depth;
			if (scene != null) {
				scene.setCameraOrderDirty();
			}
		}
	}
	
	@Override
	public void onEnter() {
		if (scene == null) {
		 	Scene scene = getScene();
		 	if (scene != null) {
				setScene(scene);
			}
		}
		super.onEnter();
	}
	
	public void onExit() {
		
	}
	
	public void setScene(Scene scene) {
		if (this.scene != scene) {
			if (this.scene != null) {
				this.scene.getCameras().remove(this);
			}
			if (scene != null) {
				this.scene = scene;
				scene.getCameras().add(this);
			}
		}
	}
	
	public boolean initDefault() {
		Size size = Director.getInstance().getWinSize();
		switch (Director.getInstance().getProjection()) {
		case _2D:
			initOrthographic(size.width, size.height, -1024, 1024);
			break;
		}
		return true;
	}
	
	public boolean initPerspective(float fieldOfView, float aspectRatio, float nearPlane, float farPlane) {
		Mat4.createPerspective(fieldOfView, aspectRatio, nearPlane, farPlane, projection);
		return true;
	}
	
	public boolean initOrthographic(float zoomX, float zoomY, float nearPlane, float farPlane) {
		Mat4.createOrthographicOffCenter(0, zoomX, 0, zoomY, nearPlane, farPlane, projection);
		return true;
	}
	
	public enum Type {
		PERSPECTIVE,
		ORTHOGRAPHIC
	}
	
	public enum CameraFlag {
		DEFAULT,
		USER1, USER2, USER3, USER4,
		USER5, USER6, USER7, USER8;
	}
}
