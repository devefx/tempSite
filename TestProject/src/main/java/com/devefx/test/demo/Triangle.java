package com.devefx.test.demo;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.gl2.GLUgl2;

public class Triangle implements GLEventListener {

	// 由数组meshArray转换成的缓存buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO对象集合
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLUgl2();
		
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		gl.glViewport(0, 0, 800, 600);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		glu.gluOrtho2D(0.0f, 800.0f, 0.0f, 600.0f);

		gl.glTranslatef(100f, 0f, 0.0f);
		
		float[] vVerts = {
			 100f, 0.0f, 0.0f,
			 100f, 100.0f, 0.0f,
			 0.0f, 100f, 0.0f
		};
		meshArraybuffer = FloatBuffer.allocate(vVerts.length);
		for (float f : vVerts) {
			meshArraybuffer.put(f);
		}
		meshArraybuffer.flip();
		
		
		// 开辟一个vbo
		gl.glGenBuffers(2, buffersVBO);
		// 绑定vbos中的vbo[0]对象
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_STATIC_DRAW);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT | GL2.GL_STENCIL_BUFFER_BIT);
		
		
		
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, 0);
		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, meshArraybuffer.capacity() / 3);
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

}
