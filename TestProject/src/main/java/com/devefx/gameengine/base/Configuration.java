package com.devefx.gameengine.base;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

public class Configuration {
	
	private static Configuration configuration;
	
	public static Configuration getInstance() {
		if (configuration == null) {
			configuration = new Configuration();
			configuration.init();
		}
		return configuration;
	}
	
	private Configuration() {
	}
	
	public boolean supportsShareableVAO() {
		return false;
	}
	
	public void init() {
		
	}
	
	public static void bindVAO(int vaoId) {
		if (Configuration.getInstance().supportsShareableVAO()) {
			final GL2 gl = GLContext.getCurrentGL().getGL2();
			gl.glBindVertexArray(vaoId);
		}
	}
}
