package test.demo;

import java.net.URL;

import com.devefx.gameengine.ui.Scene;
import com.devefx.gameengine.ui.Sprite;

public class FirstScene extends Scene {
	
	public static Sprite sprite;
	
	public FirstScene() {
		super();
		URL url = getClass().getClassLoader().getResource("1.jpg");
		if (url != null) {
			sprite = Sprite.create(url.getPath());
			sprite.setPosition(100, 100);
			addChild(sprite);
			
			
			Sprite sprite1 = Sprite.create();
			sprite1.initWithTexture(sprite.getTexture());
			sprite1.setPosition(300, 200);
			addChild(sprite1);
		}
	}
}
