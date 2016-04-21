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

public class Renderer2 implements GLEventListener {

	// 由数组meshArray转换成的缓存buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO对象集合
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	
	protected int texture;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();
		// 设置背景颜色  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// 视点大小
		gl.glViewport(0, 0, 800, 600);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// 裁剪横坐标（left，right）纵坐标（bottom，top）范围内的视图，放进GL可见视图中
		//glu.gluOrtho2D(-1.0, 101, -1.0, 101.0);// 使坐标系统出现在GL里，此时屏幕中最左面是坐标0，右面是500，最下0，嘴上500
		
		glu.gluOrtho2D(0f, 100f, 100f, 0);
		
		final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") && gl.isFunctionAvailable("glBindBufferARB")  
                && gl.isFunctionAvailable("glBufferDataARB") && gl.isFunctionAvailable("glDeleteBuffersARB");  
        System.out.println("Is VBO supported : " + VBOsupported);
		
        // 数组，包含了meshArray.length/2对二维坐标
		float[] array = {
				10, 10,  
			    25, 20,  
			    20, 30, 
			    
			    40, 40,  
			    50, 50,  
			    50, 60,  
			    
			    72, 70,  
			    80, 80,  
			    80, 90  
		};
		// 把数组的元素按顺序逐个放进buffer中
		meshArraybuffer = FloatBuffer.allocate(array.length);
		for (int i = 0; i < array.length; i++) {
			meshArraybuffer.put(array[i]);
		}
		meshArraybuffer.flip();
		// 开辟一个vbo
		gl.glGenBuffers(1, buffersVBO);
		// 绑定vbos中的vbo[0]对象
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 把buffer拷贝到vbo(显卡)中  
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, meshArraybuffer.capacity() * Buffers.SIZEOF_FLOAT,
				meshArraybuffer, GL2.GL_STATIC_DRAW);
		
		
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
		// 设置GL的画图颜色，也就是画刷的颜色
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
		
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);  
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// 以size(2)个数组元素为一个单位放进缓存buffer
		gl.glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
		// 以mode方式（点、三角形、线）画数组缓存中从第first开始的count个数据元素
		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, meshArraybuffer.capacity() / 2);
		
		// gl.glPointSize(5);  
        // gl.glDrawArrays(GL.GL_POINTS, 0, meshArray.length / 2 + 2);// 此时 ，实际没有meshArray.length / 2 + 2个，系统自动补出一个点（0,0）  
  
        // gl.glDrawArrays(GL.GL_LINES, 0, meshArray.length / 2);// 此时9个点，画了4条曲线，最后一个点没用着  
        // gl.glDrawArrays(GL.GL_LINES, 0, meshArray.length / 2 + 1);// 此时9个点，画了5条曲线，最后一个点和自动加的（0,0）连线了  
  
        // gl.glDrawArrays(GL.GL_QUADS, 5, meshArray.length / 2 + 2);  
  
        // *****************************参数说明******Begin**********************************************************************  
        // glVertexPointer glDrawArrays  
        // 假设buffer中共18个float数，glVertexPointer(2, GL.GL_FLOAT, 0, 0)后buffer中相当于生成了9(18/2)个元素变量（此处是一对坐标）  
        // drawArray中，第二个参数是指从刚才生成那9个元素变量中第几个元素变量开始算，第三个参数表示从开始的index算起  
        // 取出元素索引为index至index+count-1的元素（count个），以（点线三角形）画出来。  
        // 在此特别注意：  
        // index和count参数，如果为负数或者out of index 并不报错，如果index后count超出实际buffer中含有元素数，并且  
        // 正好buffer中最后一个或几个元素凑不够线或三角形或四边形，buffer末尾会补充一个元素（0,0）  
        // *****************************参数说明*******End***********************************************************************  
		
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
