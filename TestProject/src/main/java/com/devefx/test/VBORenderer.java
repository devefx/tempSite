package com.devefx.test;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class VBORenderer implements GLEventListener {

	// ������meshArrayת���ɵĻ���buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO���󼯺�
	protected IntBuffer buffersVBO = IntBuffer.allocate(1);
	
	protected IntBuffer index_list;
	
	protected int texture;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();
		/*// ���ñ�����ɫ  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// �ӵ��С
		gl.glViewport(0, 0, 100, 100);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();*/
		// �ü������꣨left��right�������꣨bottom��top����Χ�ڵ���ͼ���Ž�GL�ɼ���ͼ��
		//glu.gluOrtho2D(-1.0, 101, -1.0, 101.0);// ʹ����ϵͳ������GL���ʱ��Ļ��������������0��������500������0������500
		glu.gluOrtho2D(0f, 100f, 100f, 0);
		// ���飬������meshArray.length/2�Զ�ά����
		float[] array = {
				1, 1, 0, 1, 0, 10, 10, 1, 
				1, 1, 0, 1, 0, 25, 20, 1,
				1, 1, 0, 1, 0, 20, 30, 1,
			    
				1, 1, 1, 1, 0, 40, 40, 1,
				1, 1, 1, 1, 0, 50, 50, 1,
				1, 1, 0, 1, 0, 50, 60, 1,
			    
				1, 1, 1, 0, 0, 72, 70, 1,
				1, 1, 1, 0, 0, 80, 80, 1,
				1, 1, 1, 0, 0, 80, 90, 1
		};
		// �������Ԫ�ذ�˳������Ž�buffer��
		meshArraybuffer = FloatBuffer.allocate(array.length);
		for (int i = 0; i < array.length; i++) {
			meshArraybuffer.put(array[i]);
		}
		meshArraybuffer.flip();
		
		gl.glInterleavedArrays(GL2.GL_T2F_C3F_V3F, 0, meshArraybuffer);
		
		// ����һ��vbo
		gl.glGenBuffers(1, buffersVBO);
		// ��vbos�е�vbo[0]����
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// ��buffer������vbo(�Կ�)��  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_STATIC_DRAW);
		
		// ��������
		int[] int_array = {
			0, 1, 2,
			3, 4, 5,
			6, 7, 8
		};
		index_list = IntBuffer.allocate(int_array.length);
		for (int i = 0; i < int_array.length; i++) {
			index_list.put(int_array[i]);
		}
		index_list.flip();
		
		// ��������
		try {
			gl.glEnable(GL2.GL_TEXTURE_2D);
			Texture texture = TextureIO.newTexture(new File("f:\\1.jpg"), true);
			this.texture = texture.getTextureObject(gl);
		} catch (GLException | IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// ��GLAutoDrawable��ȡGL
		final GL2 gl = drawable.getGL().getGL2();
		// ��䱳����ɫ
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		// ������
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
		// ���ö�������
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);  
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		
		
		gl.glDrawElements(GL2.GL_TRIANGLES, meshArraybuffer.capacity() / 8, GL2.GL_UNSIGNED_INT, index_list);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub

	}

}
