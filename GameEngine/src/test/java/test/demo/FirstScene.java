package test.demo;

import java.net.URL;

import com.devefx.gameengine.ui.Scene;
import com.devefx.gameengine.ui.Sprite;

public class FirstScene extends Scene {
	
	public FirstScene() {
		super();
		URL url = getClass().getClassLoader().getResource("1.jpg");
		if (url != null) {
			Sprite sprite = Sprite.create(url.getPath());
			sprite.setPosition(100, 100);
			addChild(sprite);
		}
	}
}
