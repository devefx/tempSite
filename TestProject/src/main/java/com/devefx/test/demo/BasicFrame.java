package com.devefx.test.demo;

import java.awt.FlowLayout;  
import java.awt.Frame;  
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;  

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
  
public class BasicFrame implements GLEventListener {  
  
    /** 
     * 数组，包含了meshArray.length/2对二维坐标 
     */  
    protected final static float[] meshArray = {
    	
	    10, 10,  
	  
	    20, 20,  
	  
	    20, 30,  
	  
	    40, 40,  
	  
	    50, 50,  
	  
	    50, 60,  
	  
	    70, 70,  
	  
	    80, 80,  
	  
	    80, 90
    };
	  
    /** 
     * 由数组meshArray转换成的缓存buffer 
     */  
    protected static ByteBuffer meshArrayBuffer;  
  
    @Override  
    public void init(final GLAutoDrawable drawable) { // 初始函数  
  
        final GL2 gl = drawable.getGL().getGL2();  
        final GLU glu = new GLU();  
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f); // 设置背景颜色  
        gl.glViewport(0, 0, 100, 100); // 视点大小  
        gl.glMatrixMode(GL2.GL_PROJECTION);  
        gl.glLoadIdentity();  
        // 裁剪横坐标（left，right）纵坐标（bottom，top）范围内的视图，放进GL可见视图中  
        glu.gluOrtho2D(-1.0, 101, -1.0, 101.0); // 使坐标系统出现在GL里，此时屏幕中最左面是坐标0，右面是500，最下0，嘴上500  
  
        final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") && gl.isFunctionAvailable("glBindBufferARB")  
                && gl.isFunctionAvailable("glBufferDataARB") && gl.isFunctionAvailable("glDeleteBuffersARB");  
        System.out.println("Is VBO supported : " + VBOsupported);  
  
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        
        meshArrayBuffer = ByteBuffer.allocateDirect(meshArray.length * Buffers.SIZEOF_FLOAT);  
        for (int i = 0; i < meshArray.length; i++) {  
            meshArrayBuffer.putFloat(meshArray[i]);// 把数组的元素按顺序逐个放进buffer中  
        }  
        meshArrayBuffer.flip();
        gl.glVertexPointer(2, GL.GL_FLOAT, 0, meshArrayBuffer);// 以size个数组元素为一个单位放进缓存buffer  
  
    }  
  
    @Override  
    public void display(final GLAutoDrawable drawable) { // 画图函数  
  
        final GL2 gl = drawable.getGL().getGL2(); // 从GLAutoDrawable获取GL  
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // 填充背景颜色  
        gl.glColor3f(1.0f, 0.0f, 0.0f); // 设置GL的画图颜色，也就是画刷的颜色  
  
        gl.glPointSize(10);  
  
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 9);// 以mode方式（点、三角形、线）画数组缓存中从第first开始的count个数据元素  
        // gl.glDrawArrays(GL.GL_POINTS, 0, 9);// 以mode方式（点、三角形、线）画数组缓存中从第first开始的count个数据元素  
  
        // ************************************************************************  
        // 此段代码完全可由gl.glDrawArrays(GL.GL_TRIANGLES, 0, 9);这句代码替换，时等价的  
        // gl.glBegin(GL.GL_TRIANGLES);  
        // gl.glArrayElement(0);  
        // gl.glArrayElement(1);  
        // gl.glArrayElement(2);  
        // gl.glArrayElement(3);  
        // gl.glArrayElement(4);  
        // gl.glArrayElement(5);  
        // gl.glArrayElement(6);  
        // gl.glArrayElement(7);  
        // gl.glArrayElement(8);  
        // gl.glEnd();  
        // ************************************************************************  
  
    }

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}  
  
  
}  