package test.demo;

import com.devefx.gameengine.base.Director;
import com.devefx.gameengine.base.Event;
import com.devefx.gameengine.base.EventListener;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.platform.desktop.GLViewImpl;

public class Test {
	
	public static void main(String[] args) throws Exception {
		
		final Director director = Director.getInstance();
		
		GLView glView = director.getOpenGLView();
		
		if (glView == null) {
			
			glView = GLViewImpl.create("OpenGLView", new Rect(0, 0, 800, 600));
			
			director.setOpenGLView(glView);
		}
		
		glView.setDesignResolutionSize(800, 600);
		
		director.setListener(new EventListener() {
			@Override
			public void init() {
				director.runWithScene(new FirstScene()); 
			}
			@Override
			public void onEvent(Event event) {
				// TODO Auto-generated method stub
			}
		});
	}
	
}
