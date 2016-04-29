package com.devefx.gameengine.renderer;

public abstract class RenderCommand {

	protected float globalOrder;
	
	protected boolean skipBatching;
	
	protected Type type;
	
	public enum Type {
		QUAD_COMMAND
	}
	
	public float getGlobalOrder() {
		return globalOrder;
	}
	
	public boolean isSkipBatching() {
		return skipBatching;
	}
	
	public Type getType() {
		return type;
	}
}
