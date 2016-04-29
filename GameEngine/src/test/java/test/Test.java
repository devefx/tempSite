package test;

import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.desktop.GLViewImpl;

public class Test {
	
	public static void main(String[] args) {
		GLViewImpl glViewImpl = GLViewImpl.create("Test", new Rect(0, 0, 800, 600));
		
		System.out.println(glViewImpl);
	}
	
}
