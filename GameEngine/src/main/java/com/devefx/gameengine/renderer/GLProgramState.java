package com.devefx.gameengine.renderer;

import com.devefx.gameengine.math.Mat4;

public class GLProgramState {

	protected GLProgram program;
	
	public GLProgram getGLProgram() {
		return program;
	}
	
	public void setGLProgram(GLProgram program) {
		this.program = program;
	}
	
	public void apply(Mat4 modelView) {
		applyGLProgram(modelView);
		applyAttributes(true);
		applyUniforms();
	}
	
	public void updateUniformsAndAttributes() {
		
	}
	
	public void applyGLProgram(Mat4 modelView) {
		updateUniformsAndAttributes();
		
		program.use();
		program.setUniformsForBuiltins(modelView);
	}
	
	public void applyAttributes(boolean applyAttribFlags) {
		
	}
	
	public void applyUniforms() {
		
	}
}
