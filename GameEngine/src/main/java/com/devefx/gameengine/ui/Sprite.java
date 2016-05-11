package com.devefx.gameengine.ui;

import com.devefx.gameengine.base.Node;
import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Rect;
import com.devefx.gameengine.base.types.Tex2F;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.base.types.Vec3;
import com.devefx.gameengine.math.Mat4;
import com.devefx.gameengine.renderer.GLProgram;
import com.devefx.gameengine.renderer.GLProgramState;
import com.devefx.gameengine.renderer.QuadCommand;
import com.devefx.gameengine.renderer.Renderer;
import com.devefx.gameengine.renderer.Texture2D;

public class Sprite extends Node {

	private V3F_C4B_T2F_Quad quad;
	private BlendFunc blendFunc;
	private Texture2D texture;
	private QuadCommand quadCommand;
	
	public Sprite() {
		quad = new V3F_C4B_T2F_Quad();
	}
	
	public static Sprite create() {
		Sprite sprite = new Sprite();
		if (sprite.init()) {
			return sprite;
		}
		return null;
	}
	
	public static Sprite create(String filename) {
		Sprite sprite = new Sprite();
		if (sprite.initWithFile(filename)) {
			return sprite;
		}
		return null;
	}
	
	@Override
	public boolean init() {
		return initWithTexture(null, new Rect());
	}
	
	public boolean initWithFile(String filename) {
		try {
			Texture2D texture = new Texture2D();
			if (texture.initWithFile(filename)) {
				Rect rect = new Rect();
				rect.size = texture.getContentSize();
				return initWithTexture(texture, rect);
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public boolean initWithTexture(Texture2D texture) {
		Rect rect = new Rect();
		rect.size = texture.getContentSize();
		return initWithTexture(texture, rect);
	}
	
	public boolean initWithTexture(Texture2D texture, Rect rect) {
		blendFunc = BlendFunc.ALPHA_PREMULTIPLIED;
		
		quad.bl.colors = Color4B.WHITE;
		quad.br.colors = Color4B.WHITE;
		quad.tl.colors = Color4B.WHITE;
		quad.tr.colors = Color4B.WHITE;
		
		setGLProgramState(GLProgramState.getOrCreateWithGLProgramName(GLProgram.SHADER_NAME_POSITION_TEXTURE_COLOR_NO_MVP));
		
		setTexture(texture);
		setTextureRect(rect);
		return true;
	}
	
	public Texture2D getTexture() {
		return texture;
	}
	
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public void setTextureRect(Rect rect) {
		float x1 = position.x;
		float y1 = position.y;
		float x2 = x1 + rect.size.width;
		float y2 = y1 + rect.size.height;
		
		quad.bl.vertices = new Vec3(x1, y1, 0);
		quad.br.vertices = new Vec3(x2, y1, 0);
		quad.tl.vertices = new Vec3(x1, y2, 0);
		quad.tr.vertices = new Vec3(x2, y2, 0);
		
		setTextureCoords(rect);
	}
	
	public void setTextureCoords(Rect rect) {
		quad.bl.texCoords = new Tex2F(0, 0);
		quad.br.texCoords = new Tex2F(1, 0);
		quad.tl.texCoords = new Tex2F(0, 1);
		quad.tr.texCoords = new Tex2F(1, 1);
	}
	
	@Override
	public void draw(Renderer renderer, Mat4 transform) {
		try {
			QuadCommand cmd = new QuadCommand();
			cmd.init(globalZOrder, texture.getName(), getGLProgramState(), blendFunc, transform, quad);
			renderer.addCommand(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
