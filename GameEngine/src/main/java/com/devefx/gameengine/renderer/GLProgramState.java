package com.devefx.gameengine.renderer;

import com.devefx.gameengine.math.Mat4;

public class GLProgramState {

	protected GLProgram program;
	
	public static GLProgramState create(GLProgram glprogram) {
		GLProgramState state = new GLProgramState();
		if (state.init(glprogram)) {
			return state;
		}
		return null;
	}
	
	public static GLProgramState getOrCreateWithGLProgramName(String glProgramName) {
		GLProgram glprogram = GLProgramCache.getInstance().getGLProgram(glProgramName);
		if(glprogram != null) {
			return getOrCreateWithGLProgram(glprogram);
		}
		return null;
	}
	
	public static GLProgramState getOrCreateWithGLProgram(GLProgram glprogram) {
		return create(glprogram);
	}
	
	public GLProgramState() {
		
	}
	
	public boolean init(GLProgram glprogram) {
		this.program = glprogram;
		return true;
	}
	
	public GLProgram getGLProgram() {
		return program;
	}
	
	public void setGLProgram(GLProgram glprogram) {
		if (this.program != glprogram) {
			init(glprogram);
		}
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
		updateUniformsAndAttributes();
		
	}
	
	public void applyUniforms() {
		updateUniformsAndAttributes();
		
	}
}
