package com.devefx.test;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

public class VBORenderer implements GLEventListener {

	// ������meshArrayת���ɵĻ���buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO/VIO���󼯺�
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	protected ShortBuffer indices;
	
	protected IntBuffer index_list;
	
	protected int texture;
	
	public static final int VERTEX_ATTRIB_POSITION	= 0;
	public static final int VERTEX_ATTRIB_COLOR		= 1;
	public static final int VERTEX_ATTRIB_TEX_COORD = 2;
	
	public VBORenderer() {
		// vertex buffer
		meshArraybuffer = Buffers.newDirectFloatBuffer(9);
		// index buffer
		indices = ShortBuffer.allocate(6);
	}
	
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
		
		
		indices.put((short) 0);
		indices.put((short) 1);
		indices.put((short) 2);
		indices.put((short) 3);
		indices.put((short) 2);
		indices.put((short) 1);
		indices.rewind();
		
		// ����һ��vbo/vio
		gl.glGenBuffers(2, buffersVBO);
		// ��vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// ��buffer������vbo(�Կ�)��  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_DYNAMIC_DRAW);
		
		// vertices
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Buffers.SIZEOF_FLOAT * 3, 0);
		
		// colors
		//gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
		//gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_FLOAT, true, Buffers.SIZEOF_FLOAT * 7, 12);
		
		// ����һ��vio
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, Buffers.SIZEOF_SHORT * 6, indices, GL2.GL_STATIC_DRAW);
		
		// unbind vbo/vio
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		
		
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
		gl.glColor3f(1, 1, 1);
		
		// ���ö�������
	//	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);  
		
		// ��vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// ����vbo����
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, Buffers.SIZEOF_FLOAT * 9, null, GL2.GL_DYNAMIC_DRAW);
		ByteBuffer buffer = gl.glMapBuffer(GL2.GL_ARRAY_BUFFER, GL2.GL_WRITE_ONLY);
		float[] array = {
				10, 10, 1,
				25, 20, 1,
				20, 30, 1,
		};
		for (int i = 0; i < array.length; i++) {
			buffer.putFloat(array[i]);
		}
		gl.glUnmapBuffer(GL2.GL_ARRAY_BUFFER);
		
		// ����ͼ��
		//gl.glDrawArrays(GL2.GL_TRIANGLES, 0, 3);
		
		// ����ͼ��
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glDrawElements(GL2.GL_TRIANGLES, 3, GL2.GL_SHORT, 0);
		
		
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}

}
