package com.devefx.gameengine.ui;

import com.devefx.gameengine.base.Node;
import com.devefx.gameengine.renderer.Renderer;

public abstract class Scene extends Node {

	@Override
	public void draw(Renderer renderer) {
		if (!children.isEmpty()) {
			
			for (Node node: children) {
				node.draw(renderer);
			}
		}
	}
	
}
