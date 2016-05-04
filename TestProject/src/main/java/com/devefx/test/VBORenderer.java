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

	// 由数组meshArray转换成的缓存buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO/VIO对象集合
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
		/*// 设置背景颜色  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// 视点大小
		gl.glViewport(0, 0, 100, 100);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();*/
		// 裁剪横坐标（left，right）纵坐标（bottom，top）范围内的视图，放进GL可见视图中
		//glu.gluOrtho2D(-1.0, 101, -1.0, 101.0);// 使坐标系统出现在GL里，此时屏幕中最左面是坐标0，右面是500，最下0，嘴上500
		glu.gluOrtho2D(0f, 100f, 100f, 0);
		
		
		indices.put((short) 0);
		indices.put((short) 1);
		indices.put((short) 2);
		indices.put((short) 3);
		indices.put((short) 2);
		indices.put((short) 1);
		indices.rewind();
		
		// 开辟一个vbo/vio
		gl.glGenBuffers(2, buffersVBO);
		// 绑定vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_DYNAMIC_DRAW);
		
		// vertices
		gl.glEnableVertexAttribArray(VERTEX_ATTRIB_POSITION);
		gl.glVertexAttribPointer(VERTEX_ATTRIB_POSITION, 3, GL2.GL_FLOAT, false, Buffers.SIZEOF_FLOAT * 3, 0);
		
		// colors
		//gl.glEnableVertexAttribArray(VERTEX_ATTRIB_COLOR);
		//gl.glVertexAttribPointer(VERTEX_ATTRIB_COLOR, 4, GL2.GL_FLOAT, true, Buffers.SIZEOF_FLOAT * 7, 12);
		
		// 开辟一个vio
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
		// 从GLAutoDrawable获取GL
		final GL2 gl = drawable.getGL().getGL2();
		// 填充背景颜色
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glColor3f(1, 1, 1);
		
		// 启用顶点数组
	//	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);  
		
		// 绑定vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 访问vbo数据
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
		
		// 绘制图形
		//gl.glDrawArrays(GL2.GL_TRIANGLES, 0, 3);
		
		// 绘制图形
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, buffersVBO.get(1));
		gl.glDrawElements(GL2.GL_TRIANGLES, 3, GL2.GL_SHORT, 0);
		
		
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
	}

}
