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
     * ���飬������meshArray.length/2�Զ�ά���� 
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
     * ������meshArrayת���ɵĻ���buffer 
     */  
    protected static ByteBuffer meshArrayBuffer;  
  
    @Override  
    public void init(final GLAutoDrawable drawable) { // ��ʼ����  
  
        final GL2 gl = drawable.getGL().getGL2();  
        final GLU glu = new GLU();  
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1f); // ���ñ�����ɫ  
        gl.glViewport(0, 0, 100, 100); // �ӵ��С  
        gl.glMatrixMode(GL2.GL_PROJECTION);  
        gl.glLoadIdentity();  
        // �ü������꣨left��right�������꣨bottom��top����Χ�ڵ���ͼ���Ž�GL�ɼ���ͼ��  
        glu.gluOrtho2D(-1.0, 101, -1.0, 101.0); // ʹ����ϵͳ������GL���ʱ��Ļ��������������0��������500������0������500  
  
        final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") && gl.isFunctionAvailable("glBindBufferARB")  
                && gl.isFunctionAvailable("glBufferDataARB") && gl.isFunctionAvailable("glDeleteBuffersARB");  
        System.out.println("Is VBO supported : " + VBOsupported);  
  
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        
        meshArrayBuffer = ByteBuffer.allocateDirect(meshArray.length * Buffers.SIZEOF_FLOAT);  
        for (int i = 0; i < meshArray.length; i++) {  
            meshArrayBuffer.putFloat(meshArray[i]);// �������Ԫ�ذ�˳������Ž�buffer��  
        }  
        meshArrayBuffer.flip();
        gl.glVertexPointer(2, GL.GL_FLOAT, 0, meshArrayBuffer);// ��size������Ԫ��Ϊһ����λ�Ž�����buffer  
  
    }  
  
    @Override  
    public void display(final GLAutoDrawable drawable) { // ��ͼ����  
  
        final GL2 gl = drawable.getGL().getGL2(); // ��GLAutoDrawable��ȡGL  
        gl.glClear(GL.GL_COLOR_BUFFER_BIT); // ��䱳����ɫ  
        gl.glColor3f(1.0f, 0.0f, 0.0f); // ����GL�Ļ�ͼ��ɫ��Ҳ���ǻ�ˢ����ɫ  
  
        gl.glPointSize(10);  
  
        gl.glDrawArrays(GL.GL_TRIANGLES, 0, 9);// ��mode��ʽ���㡢�����Ρ��ߣ������黺���дӵ�first��ʼ��count������Ԫ��  
        // gl.glDrawArrays(GL.GL_POINTS, 0, 9);// ��mode��ʽ���㡢�����Ρ��ߣ������黺���дӵ�first��ʼ��count������Ԫ��  
  
        // ************************************************************************  
        // �˶δ�����ȫ����gl.glDrawArrays(GL.GL_TRIANGLES, 0, 9);�������滻��ʱ�ȼ۵�  
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