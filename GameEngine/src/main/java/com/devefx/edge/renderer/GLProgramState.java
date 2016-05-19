package com.devefx.edge.renderer;

import com.devefx.edge.math.Mat4;

public class GLProgramState {

	protected GLProgram glprogram;
	
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
	
	public boolean init(GLProgram glprogram) {
		this.glprogram = glprogram;
		return true;
	}
	
	public GLProgram getGLProgram() {
		return glprogram;
	}
	
	public void setGLProgram(GLProgram glprogram) {
		if (this.glprogram != glprogram) {
			init(glprogram);
		}
	}
	
	public void apply(Mat4 modelView) {
		applyGLProgram(modelView);
		applyAttributes(true);
		applyUniforms();
	}
	
	public void updateUniformsAndAttributes() {
		assert(glprogram != null) : "invalid glprogram";
		
	}
	
	public void applyGLProgram(Mat4 modelView) {
		assert(glprogram != null) : "invalid glprogram";
		updateUniformsAndAttributes();
		// set shader
		glprogram.use();
		glprogram.setUniformsForBuiltins(modelView);
	}
	
	public void applyAttributes(boolean applyAttribFlags) {
		updateUniformsAndAttributes();
		// FIXME
	}
	
	public void applyUniforms() {
		updateUniformsAndAttributes();
		// FIXME
	}
}
