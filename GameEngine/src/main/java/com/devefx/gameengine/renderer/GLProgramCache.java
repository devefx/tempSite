package com.devefx.gameengine.renderer;

import java.util.HashMap;
import java.util.Map;

public class GLProgramCache {

	private Map<String, GLProgram> programs;
	
	private static GLProgramCache sharedGLProgramCache;
	
	public static GLProgramCache getInstance() {
		if (sharedGLProgramCache == null) {
			sharedGLProgramCache = new GLProgramCache();
			sharedGLProgramCache.init();
		}
		return sharedGLProgramCache;
	}
	
	private GLProgramCache() {
		programs = new HashMap<String, GLProgram>();
	}
	
	public void loadDefaultGLPrograms() {
		// Position Texture Color shader
		GLProgram program = new GLProgram();
		loadDefaultGLProgram(program, ShaderType.PositionTextureColor);
		programs.put(GLProgram.SHADER_NAME_POSITION_TEXTURE_COLOR, program);
		
	    // Position Texture Color without MVP shader
		program = new GLProgram();
	    loadDefaultGLProgram(program, ShaderType.PositionTextureColorNoMVP);
	    programs.put(GLProgram.SHADER_NAME_POSITION_TEXTURE_COLOR_NO_MVP, program);
		
	    // Position Texture shader
		program = new GLProgram();
	    loadDefaultGLProgram(program, ShaderType.PositionTexture);
	    programs.put(GLProgram.SHADER_NAME_POSITION_TEXTURE, program);
	}
	
	public GLProgram getGLProgram(String key) {
		return programs.get(key);
	}
	
	public void addGLProgram(GLProgram program, String key) {
		programs.put(key, program);
	}
	
	private boolean init() {
		loadDefaultGLPrograms();
		return true;
	}
	
	private void loadDefaultGLProgram(GLProgram program, ShaderType type) {
		switch (type) {
		case PositionTextureColor:
			program.initWithFilename(Shader.POSITION_TEXTURE_COLOR_VERT, Shader.POSITION_TEXTURE_COLOR_FRAG);
			break;
		case PositionTextureColorNoMVP:
			program.initWithFilename(Shader.POSITION_TEXTURE_COLOR_NO_MVP_VERT, Shader.POSITION_TEXTURE_COLOR_NO_MVP_FRAG);
			break;
		case PositionTexture:
			program.initWithFilename(Shader.POSITION_TEXTURE_VERT, Shader.POSITION_TEXTURE_FRAG);
			break;
		}
		program.link();
		program.updateUniforms();
	}
	
	enum ShaderType {
		PositionTextureColor,
		PositionTextureColorNoMVP,
		PositionTexture
	}
}
