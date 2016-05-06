package com.devefx.gameengine.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.devefx.gameengine.base.types.BlendFunc;
import com.devefx.gameengine.base.types.Color4B;
import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.base.types.Tex2F;
import com.devefx.gameengine.base.types.V3F_C4B_T2F_Quad;
import com.devefx.gameengine.base.types.Vec3;
import com.devefx.gameengine.platform.GLView;
import com.devefx.gameengine.renderer.GLProgram;
import com.devefx.gameengine.renderer.QuadCommand;
import com.devefx.gameengine.renderer.Renderer;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Director {

	protected GLView openGLView;
	protected Renderer renderer;
	
	protected Size winSizeInPoints;
	
	private static Director director;
	
	public static Director getInstance() {
		if (director == null) {
			director = new Director();
			director.init();
		}
		return director;
	}
	
	public boolean init() {
		renderer = new Renderer();
		
		return true;
	}
	
	public void setOpenGLView(GLView openGLView) {
		if (this.openGLView != openGLView) {
			
			this.openGLView = openGLView;
			
			if (openGLView != null) {
				winSizeInPoints = openGLView.getDesignResolutionSize();
				setGLDefaultValues();
			}
		}
	}
	
	public GLView getOpenGLView() {
		return openGLView;
	}
	public Size getWinSizeInPoints() {
		return winSizeInPoints;
	}
	public void setWinSizeInPoints(Size winSizeInPoints) {
		this.winSizeInPoints = winSizeInPoints;
	}
	
	public void setGLDefaultValues() {
		// TODO Auto-generated method stub
		
	}
	
	public final GLEventListener glEventListener = new GLEventListener() {
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			
		}
		@Override
		public void init(GLAutoDrawable drawable) {
			renderer.initGLView();
			
			try {
				initCreateProgram();
				renderer.addCommand(createCommand());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		@Override
		public void dispose(GLAutoDrawable drawable) {
			
		}
		@Override
		public void display(GLAutoDrawable drawable) {
			renderer.render();
		}
	};
	
	
	public static QuadCommand createCommand() throws GLException, IOException {
		QuadCommand cmd = new QuadCommand();
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource("1.jpg");
		
		Texture tex = TextureIO.newTexture(new File(url.getPath()), true);
		int textureID = tex.getTextureObject();
		
		V3F_C4B_T2F_Quad quads = new V3F_C4B_T2F_Quad();
		
		quads.bl.vertices = new Vec3(10, 10, 1);
		quads.bl.colors = new Color4B(255, 0, 0, 255);
		
		quads.br.vertices = new Vec3(40, 10, 1);
		quads.br.colors = new Color4B(0, 255, 0, 255);
		
		quads.tl.vertices = new Vec3(10, 40, 1);
		quads.tl.colors = new Color4B(0, 0, 255, 255);
		
		quads.tr.vertices = new Vec3(40, 40, 1);
		quads.tr.colors = new Color4B(255, 255, 0, 255);
		
		quads.bl.texCoords = new Tex2F(0, 0);
		quads.br.texCoords = new Tex2F(1, 0);
		quads.tl.texCoords = new Tex2F(0, 1);
		quads.tr.texCoords = new Tex2F(1, 1);
		
		cmd.init(0, textureID, new BlendFunc(0, 0), quads);
		
		return cmd;
	}
	
	static void initCreateProgram() throws IOException {
		final GL2 gl = GLContext.getCurrentGL().getGL2();
		
		GLProgram glProgram = new GLProgram();
		String vertexShaderString = readFile("shader_PositionTextureColor.vert");
		String fragmentShaderString = readFile("shader_PositionTextureColor.frag");
		
		if (gl.isGL3core()) {
			vertexShaderString = "#version 130\n"+vertexShaderString;
            fragmentShaderString = "#version 130\n"+fragmentShaderString;
		}
		
		if (glProgram.init(vertexShaderString, fragmentShaderString)) {
			glProgram.link();
			glProgram.use();
		}
		
		float[] matrix = {
				0.02f,  0.0f,   0.0f, 0.0f,
				 0.0f, 0.02f,   0.0f, 0.0f,
				 0.0f,  0.0f, -0.01f, 0.0f,
				-1.0f, -1.0f,   0.0f, 1.0f
			};
		gl.glUniformMatrix4fv(GLProgram.UNIFORM_AMBIENT_COLOR, 1, false, matrix, 0);
		
		System.out.println(gl.glGetError());
	}
	
	public static String readFile(String filename) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(filename);
		byte[] buf = new byte[is.available()];
		is.read(buf);
		return new String(buf);
	}
	
}
