package com.devefx.gameengine.renderer;

public abstract class RenderCommand {

	protected float globalOrder;
	
	protected boolean skipBatching;
	
	public float getGlobalOrder() {
		return globalOrder;
	}
	
	public boolean isSkipBatching() {
		return skipBatching;
	}
}
