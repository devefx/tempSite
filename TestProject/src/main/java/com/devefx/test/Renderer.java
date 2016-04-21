package com.devefx.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.gl2.GLUgl2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Renderer implements GLEventListener {

	private int texture;
	private GLUgl2 glu = new GLUgl2();
	protected float rtri = 0f;
	
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		/*gl.glShadeModel(GL2.GL_SMOOTH);
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);*/
		gl.glEnable(GL2.GL_TEXTURE_2D);
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			InputStream inputStream = new FileInputStream("f:\\1.jpg");
			byte[] buff = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buff)) > 0) {
				outputStream.write(buff, 0, len);
			}
			inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			Texture texture = TextureIO.newTexture(inputStream, true, TextureIO.JPG);
			this.texture = texture.getTextureObject(gl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		gl.glEnable(GL2.GL_VERTEX_PROGRAM_POINT_SIZE);
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}
	
	public void display(GLAutoDrawable drawable) {
		// Generating GL object
		final GL2 gl = drawable.getGL().getGL2();
		// 清除颜色缓存和深度缓存
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		// 复位
		gl.glLoadIdentity();
		// 旋转
		//gl.glRotatef(rtri, 1.0f, 1.0f, 0.0f);
		// 绘制图元
		gl.glViewport(0, 0, 400, 400);
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f); // 左下角
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f); // 左上角
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f); // 右上角
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f); // 右下角
		
		gl.glEnd();
		
		gl.glViewport(200, 200, 400, 400);
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f); // 左下角
		gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f); // 左上角
		gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f); // 右上角
		gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f); // 右下角
		
		gl.glEnd();
		
		gl.glFlush();
		
		// 灯光
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_NORMALIZE);
		float[] ambientLight = { 0.1f, 0.0f, 0.0f, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
		float[] diffuseLight = { 1.0f, 2.0f, 1.0f, 0.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
		
		rtri +=0.2f;
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		
		if (height < 1) {
			height = 1;
		}
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(x, y, width, height);
		//gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluPerspective(45.0f, (float) width / (float) height, 1.0f, 500.0f);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		System.out.println(width + "|" + height);
	}

}
