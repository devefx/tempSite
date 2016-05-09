package com.devefx.gameengine.renderer;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.devefx.gameengine.base.types.Size;
import com.devefx.gameengine.renderer.GLStateCache.GL;
import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class Texture2D {

	protected int name;
	protected Size size;
	protected GLProgram shaderProgram;
	
	public boolean initWithData(byte[] data, String fileSuffix) {
		if (data != null) {
			InputStream in = new ByteArrayInputStream(data);
			try {
				final GL2 gl = GL.getGL();
				final TextureData textureData = TextureIO.newTextureData(gl.getGLProfile(), in,
						false, fileSuffix);
				updateTexture(TextureIO.newTexture(gl, textureData));
				textureData.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
	
	public boolean initWithFile(String filename) {
		return initWithFile(new File(filename));
	}
	
	public boolean initWithFile(File file) {
		if (file != null) {
			try {
		        final GL2 gl = GL.getGL();
		        final TextureData data = TextureIO.newTextureData(gl.getGLProfile(), file,
		        		false, IOUtil.getFileSuffix(file));
				updateTexture(TextureIO.newTexture(gl, data));
				data.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int getName() {
		return name;
	}
	
	public Size getContentSize() {
		return size;
	}
	
	public void release() {
		if (name != 0) {
			GL.deleteTexture(name);
			name = 0;
		}
	}
	
	public GLProgram getGLProgram() {
		return shaderProgram;
	}
	
	protected void setGLProgram(GLProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
	}
	
	protected void updateTexture(Texture texture) {
		if (texture != null) {
			release();
			name = texture.getTextureObject();
			size = new Size(texture.getWidth(), texture.getHeight());
		}
		setGLProgram(GLProgramCache.getInstance().getGLProgram(GLProgram.SHADER_NAME_POSITION_TEXTURE_COLOR_NO_MVP));
	}
}
