package test.demo;

import com.devefx.edge.base.Director;
import com.devefx.edge.base.Director.Launch;
import com.devefx.edge.base.types.Rect;
import com.devefx.edge.platform.GLView;
import com.devefx.edge.platform.desktop.GLViewImpl;

public class NewTest {

	public static void main(String[] args) {
		
		final GLView openGLView = GLViewImpl.create("NewTest",
				new Rect(0, 0, 800, 600));
		
		final Director director = Director.getInstance();
		
		director.setLaunch(new Launch() {
			@Override
			public void start() {
				director.setOpenGLView(openGLView);
				
				director.setAnimationInterval(1.0 / 60);

			}
		});
	}
}
