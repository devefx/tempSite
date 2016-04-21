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

	// ������meshArrayת���ɵĻ���buffer 
	protected FloatBuffer meshArraybuffer;
	// VBO���󼯺�
	protected IntBuffer buffersVBO = IntBuffer.allocate(2);
	
	protected int texture;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		final GLU glu = new GLU();
		// ���ñ�����ɫ  
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		// �ӵ��С
		gl.glViewport(0, 0, 800, 600);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// �ü������꣨left��right�������꣨bottom��top����Χ�ڵ���ͼ���Ž�GL�ɼ���ͼ��
		//glu.gluOrtho2D(-1.0, 101, -1.0, 101.0);// ʹ����ϵͳ������GL���ʱ��Ļ��������������0��������500������0������500
		
		glu.gluOrtho2D(0f, 100f, 100f, 0);
		
		final boolean VBOsupported = gl.isFunctionAvailable("glGenBuffersARB") && gl.isFunctionAvailable("glBindBufferARB")  
                && gl.isFunctionAvailable("glBufferDataARB") && gl.isFunctionAvailable("glDeleteBuffersARB");  
        System.out.println("Is VBO supported : " + VBOsupported);
		
        // ���飬������meshArray.length/2�Զ�ά����
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
		// �������Ԫ�ذ�˳������Ž�buffer��
		meshArraybuffer = FloatBuffer.allocate(array.length);
		for (int i = 0; i < array.length; i++) {
			meshArraybuffer.put(array[i]);
		}
		meshArraybuffer.flip();
		// ����һ��vbo
		gl.glGenBuffers(1, buffersVBO);
		// ��vbos�е�vbo[0]����
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// ��buffer������vbo(�Կ�)��  
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
		// ��GLAutoDrawable��ȡGL
		final GL2 gl = drawable.getGL().getGL2();
		// ��䱳����ɫ
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		// ����GL�Ļ�ͼ��ɫ��Ҳ���ǻ�ˢ����ɫ
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		
		
		gl.glBindTexture(GL2.GL_TEXTURE_2D, texture);
		
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);  
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffersVBO.get(0));
		// ��size(2)������Ԫ��Ϊһ����λ�Ž�����buffer
		gl.glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
		// ��mode��ʽ���㡢�����Ρ��ߣ������黺���дӵ�first��ʼ��count������Ԫ��
		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, meshArraybuffer.capacity() / 2);
		
		// gl.glPointSize(5);  
        // gl.glDrawArrays(GL.GL_POINTS, 0, meshArray.length / 2 + 2);// ��ʱ ��ʵ��û��meshArray.length / 2 + 2����ϵͳ�Զ�����һ���㣨0,0��  
  
        // gl.glDrawArrays(GL.GL_LINES, 0, meshArray.length / 2);// ��ʱ9���㣬����4�����ߣ����һ����û����  
        // gl.glDrawArrays(GL.GL_LINES, 0, meshArray.length / 2 + 1);// ��ʱ9���㣬����5�����ߣ����һ������Զ��ӵģ�0,0��������  
  
        // gl.glDrawArrays(GL.GL_QUADS, 5, meshArray.length / 2 + 2);  
  
        // *****************************����˵��******Begin**********************************************************************  
        // glVertexPointer glDrawArrays  
        // ����buffer�й�18��float����glVertexPointer(2, GL.GL_FLOAT, 0, 0)��buffer���൱��������9(18/2)��Ԫ�ر������˴���һ�����꣩  
        // drawArray�У��ڶ���������ָ�Ӹղ�������9��Ԫ�ر����еڼ���Ԫ�ر�����ʼ�㣬������������ʾ�ӿ�ʼ��index����  
        // ȡ��Ԫ������Ϊindex��index+count-1��Ԫ�أ�count�������ԣ����������Σ���������  
        // �ڴ��ر�ע�⣺  
        // index��count���������Ϊ��������out of index �����������index��count����ʵ��buffer�к���Ԫ����������  
        // ����buffer�����һ���򼸸�Ԫ�شղ����߻������λ��ı��Σ�bufferĩβ�Ჹ��һ��Ԫ�أ�0,0��  
        // *****************************����˵��*******End***********************************************************************  
		
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

}
