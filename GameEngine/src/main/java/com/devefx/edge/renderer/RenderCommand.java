package com.devefx.edge.renderer;

public abstract class RenderCommand {
	
	protected float globalOrder;
	protected boolean isTransparent;
	protected boolean skipBatching;
	protected float depth;
	
	public float getGlobalOrder() {
		return globalOrder;
	}
	
	public boolean isTransparent() {
		return isTransparent;
	}
	
	public void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}
	
	public boolean isSkipBatching() {
		return skipBatching;
	}
	
	public void setSkipBatching(boolean skipBatching) {
		this.skipBatching = skipBatching;
	}
	
	public float getDepth() {
		return depth;
	}
}
