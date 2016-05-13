package com.devefx.gameengine.renderer;

import com.devefx.gameengine.math.Mat4;

public class CustomCommand extends RenderCommand {
	
	public Collback collback;
	
	public void init(float globalZOrder, final Mat4 modelViewTransform) {
		
	}
	
	public void init(float globalZOrder) {
		
	}
	
	public void execute() {
		if (collback != null) {
			collback.draw();
		}
	}
	
	public interface Collback {
		void draw();
	}
}
