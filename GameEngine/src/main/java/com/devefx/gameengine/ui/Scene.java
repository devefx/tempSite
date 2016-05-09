package com.devefx.gameengine.ui;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.Director.MatrixStackType;
import com.devefx.gameengine.base.Node;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.Renderer;

public abstract class Scene extends Node {

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
		
		director.pushMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
		Mat4 mat4 = new Mat4();
		mat4.m = new float[] {
				1.29903817f, 0f, 0f, 0f,
				0f, 1.73205090f, 0f, 0f,
				0f, 0f, -1.02472913f, -1.00000000f,
				-519.615295f, -519.615295f, 511.343170f, 518.761902f
		};
		director.loadMatrix(MatrixStackType.MATRIX_STACK_PROJECTION, mat4);
		
		draw(renderer, new Mat4());
		renderer.render();
		
		director.popMatrix(MatrixStackType.MATRIX_STACK_PROJECTION);
	}
}
