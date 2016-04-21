package com.devefx.gameengine.renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class RenderCommandPool<T> {
	
	private List<T> freePool = new ArrayList<T>();
	
	public T generateCommand() {
		T result = null;
		if (freePool.isEmpty()) {
			allocateCommands();
		}
		result = freePool.get(0);
		freePool.remove(0);
		return result;
	}
	
	public void pushBackCommand(T command) {
		freePool.add(command);
	}
	
	private void allocateCommands() {
		
	}
}
