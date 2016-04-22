package com.devefx.gameengine.base;

import com.devefx.gameengine.core.Scene;
import com.devefx.gameengine.renderer.Renderer;

public class Director {

	private Renderer renderer;
	
	private Scene runningScene;
	private Scene nextScene;
	
	public boolean init() {
		
		setDefaultValues();
		
		// scence
		
		
		
		return true;
	}
	
	public void setDefaultValues() {
		
	}
	
	
	
	
	
	
	
	private static Director director;
	
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
		}
		return director;
	}
}
