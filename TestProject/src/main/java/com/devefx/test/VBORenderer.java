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

	// 由数组meshArray转换成的缓存buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO对象集合
	protected IntBuffer buffersVBO = IntBuffer.allocate(1);
	
	protected IntBuffer index_list;
	
	protected int texture;
	
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
		// 数组，包含了meshArray.length/2对二维坐标
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
		// 把数组的元素按顺序逐个放进buffer中
		meshArraybuffer = FloatBuffer.allocate(array.length);
		for (int i = 0; i < array.length; i++) {
			meshArraybuffer.put(array[i]);
		}
		meshArraybuffer.flip();
		
		gl.glInterleavedArrays(GL2.GL_T2F_C3F_V3F, 0, meshArraybuffer);
		
		// 开辟一个vbo
		gl.glGenBuffers(1, buffersVBO);
		// 绑定vbos中的vbo[0]对象
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_STATIC_DRAW);
		
		// 顶点索引
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
		
		// 加载纹理
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
		// 从GLAutoDrawable获取GL
		final GL2 gl = drawable.getGL().getGL2();
		// 填充背景颜色
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		// 绑定纹理
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
		// 启用顶点数组
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
