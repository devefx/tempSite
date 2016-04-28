package com.devefx.test.demo;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.devefx.test.demo.buffer.Vertex;
import com.devefx.test.demo.buffer.VertexBuffer;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class GLRenderer2 implements GLEventListener {

	public static VertexBuffer buffer;
	
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;
	
	private float scale = 1.0f;
	private float x, y, rot;
	
	private int primitiveCount = 0;
	
	private ByteBuffer colors;
	private ByteBuffer normals;
	private ByteBuffer texCoords;
	private ByteBuffer vertices;
	
	private ShortBuffer indexList;
	
	private int texture;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glClearColor(0.0f, 0.0f, 0.4f, 1.0f);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		gl.glClearDepth(1.0f);
		gl.glDepthFunc(GL2.GL_LEQUAL);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL2.GL_BLEND);
		gl.glClearColor(0.0f, 0.0f, 0.4f, 0.0f);
		gl.glViewport(0, 0, WIDTH, HEIGHT);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		
		loadTexture("f:\\1.jpg", gl);
	}
	
	public void loadTexture(String filename, GL2 gl) {
		
		//GL2.GL_T2F_C3F_V3F
		try {
			Texture tex = TextureIO.newTexture(new File(filename), true);
			if (tex != null) {
				this.texture = tex.getTextureObject(gl);
				float tx = 0;
				float ty = 0;
				
				float x = 100;
				float y = 100;
				float z = 100;
				
				float w = tex.getWidth();
				float h = tex.getHeight();
				
				
				// color
				colors = ByteBuffer.allocateDirect(4);
				colors.put((byte) 255);
				colors.put((byte) 255);
				colors.put((byte) 255);
				colors.put((byte) 255);
				colors.flip();
				
				
				// tx ty
				texCoords = ByteBuffer.allocateDirect(2 * 4 * Buffers.SIZEOF_FLOAT);
				texCoords.putFloat(tx / w);
				texCoords.putFloat(ty / h);
				
				texCoords.putFloat((tx + w) / w);
				texCoords.putFloat(ty / h);
				
				texCoords.putFloat((tx + w) / w);
				texCoords.putFloat((ty + h) / h);
				
				texCoords.putFloat(tx / w);
				texCoords.putFloat((ty + h) / h);
				texCoords.flip();
				// x y z
				vertices = ByteBuffer.allocateDirect(3 * 4 * Buffers.SIZEOF_FLOAT);
				vertices.putFloat(x);
				vertices.putFloat(y);
				vertices.putFloat(z);
				
				vertices.putFloat(x + w);
				vertices.putFloat(y);
				vertices.putFloat(z);
				
				vertices.putFloat(x + w);
				vertices.putFloat(y + h);
				vertices.putFloat(z);
				
				vertices.putFloat(x);
				vertices.putFloat(y + h);
				vertices.putFloat(z);
				vertices.flip();
				
				// index
				indexList = ShortBuffer.allocate(4);
				indexList.put((short) 0);
				indexList.put((short) 1);
				indexList.put((short) 2);
				indexList.put((short) 3);
				indexList.flip();
				
				primitiveCount ++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// benScene
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(0, WIDTH, HEIGHT, 0, -5000.0f, 5000.0f);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		//gl.glScalef(scale, scale, 1.0f);
		//gl.glTranslatef((WIDTH / 2.0f) - x, (HEIGHT / 2.0f) - y, 0.0f);
		//gl.glRotatef(rot, 0.0f, 0.0f, 1.0f);
		//gl.glTranslatef(- (WIDTH / 2.0f), - (HEIGHT / 2.0f), 0.0f);
		// clear
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		
		

		
		
		// renderer
		for (int i = 0; i < primitiveCount; i++) {
			
			if (gl.glIsTexture(texture)) {
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
			}
			
			//gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
			
			gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
			
			//gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			

			//gl.glColorPointer(4, GL2.GL_UNSIGNED_BYTE, 0, colors);	// col
			//gl.glNormalPointer(GL2.GL_FLOAT, 0, normals);			// nx, ny
			gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texCoords);	// tx, ty
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertices);		// x, y, z
			
			gl.glDrawElements(GL2.GL_QUADS, primitiveCount * 4, GL2.GL_UNSIGNED_SHORT, indexList);
			
			
			//gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
			//gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		}
		
		
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glTexCoord2f(0, 0);gl.glVertex3f( 100.0f,  100.0f, 1.0f); // ×óÏÂ½Ç
		gl.glTexCoord2f(1, 0);gl.glVertex3f(1124.0f,  100.0f, 1.0f); // ×óÉÏ½Ç
		gl.glTexCoord2f(1, 1);gl.glVertex3f(1124.0f, 1124.0f, 1.0f); // ÓÒÉÏ½Ç
		gl.glTexCoord2f(0, 1);gl.glVertex3f( 100.0f, 1124.0f, 1.0f); // ÓÒÏÂ½Ç
		
		
		gl.glEnd();
		
		// endScene
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

}
